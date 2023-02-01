package softwareengineering.manonisgaravattiferretti.cpmsServer.cpManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.AddTariffDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.*;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.ChargingPointService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.EmspDetailsService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.ReservationService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.utils.EntityFromDTOConverter;
import softwareengineering.manonisgaravattiferretti.cpmsServer.cpManager.events.TogglePriceOptimizerEvent;
import softwareengineering.manonisgaravattiferretti.cpmsServer.emspUpdateSender.OcpiTariffSender;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PriceManager implements ApplicationListener<TogglePriceOptimizerEvent> {
    private final ChargingPointService chargingPointService;
    private final PriceOptimizer priceOptimizer;
    private final OcpiTariffSender ocpiTariffSender;
    private final EmspDetailsService emspDetailsService;
    private final ReservationService reservationService;

    @Autowired
    public PriceManager(ChargingPointService chargingPointService, PriceOptimizer priceOptimizer,
                        OcpiTariffSender ocpiTariffSender, EmspDetailsService emspDetailsService,
                        ReservationService reservationService) {
        this.chargingPointService = chargingPointService;
        this.priceOptimizer = priceOptimizer;
        this.ocpiTariffSender = ocpiTariffSender;
        this.emspDetailsService = emspDetailsService;
        this.reservationService = reservationService;
    }


    @Override
    public void onApplicationEvent(TogglePriceOptimizerEvent event) {
        chargingPointService.updateToggleOptimizer(event.getCpId(), "Price", event.isAutomatic());
        priceOptimizer.switchOptimizer(event.getCpId(), event.isAutomatic());
    }

    public Tariff addTariff(AddTariffDTO addTariffDTO, String cpId) {
        priceOptimizer.switchOptimizer(cpId, false);
        String id = UUID.randomUUID().toString();
        Tariff tariff = (addTariffDTO.getIsSpecialOffer()) ?
                EntityFromDTOConverter.fromAddTariffDTOToSpecialOffer(addTariffDTO, id) :
                EntityFromDTOConverter.fromAddTariffDTOToTariff(addTariffDTO, id);
        tariff.setLastUpdated(LocalDateTime.now());
        chargingPointService.addTariff(cpId, tariff);
        List<EmspDetails> emspDetailsList = emspDetailsService.findAll();
        for (EmspDetails emspDetails: emspDetailsList) {
            if (emspDetails.getUrl() != null) {
                ocpiTariffSender.putTariff(addTariffDTO, id, emspDetails);
            }
        }
        return tariff;
    }

    public Tariff putTariff(AddTariffDTO addTariffDTO, String cpId, String tariffId) {
        priceOptimizer.switchOptimizer(cpId, false);
        Tariff tariff = (addTariffDTO.getIsSpecialOffer()) ?
                EntityFromDTOConverter.fromAddTariffDTOToSpecialOffer(addTariffDTO, tariffId) :
                EntityFromDTOConverter.fromAddTariffDTOToTariff(addTariffDTO, tariffId);
        tariff.setLastUpdated(LocalDateTime.now());
        chargingPointService.removeTariff(cpId, tariffId);
        chargingPointService.addTariff(cpId, tariff);
        // todo: send patch to emsp
        return tariff;
    }

    public void removeTariff(String cpId, String tariffId) {
        priceOptimizer.switchOptimizer(cpId, false);
        chargingPointService.removeTariff(cpId, tariffId);
        List<EmspDetails> emspDetailsList = emspDetailsService.findAll();
        for (EmspDetails emspDetails: emspDetailsList) {
            ocpiTariffSender.deleteTariff(tariffId, emspDetails);
        }
    }

    public Double applyTariff(Long reservationId, String cpInternalId) {
        Optional<ChargingPoint> chargingPoint = chargingPointService.findChargingPointByInternalId(cpInternalId);
        Optional<Reservation> reservation = reservationService.findReservationByInternalId(reservationId);
        if (chargingPoint.isEmpty() || reservation.isEmpty()) {
            return 0.0;
        }
        String socketType = reservation.get().getSocket().getType();
        Tariff bestTariff;
        List<Tariff> tariffs = chargingPoint.get().getTariffs().stream()
                .filter(tariff -> tariff.getSocketType().equals(socketType)).toList();
        long sessionDuration = Duration.between(reservation.get().getStartTime(), reservation.get().getEndTime()).getSeconds();
        Optional<SpecialOffer> bestSpecialOffer = tariffs.stream().filter(tariff -> tariff instanceof SpecialOffer)
                .map(tariff -> (SpecialOffer) tariff)
                .filter(specialOffer -> matches(specialOffer, reservation.get(), sessionDuration))
                .reduce(((specialOffer1, specialOffer2) ->
                        (specialOffer1.getPrice() < specialOffer2.getPrice()) ? specialOffer1 : specialOffer2));
        if (bestSpecialOffer.isPresent()) {
            bestTariff = bestSpecialOffer.get();
        } else {
            bestTariff = tariffs.stream().filter(tariff -> !(tariff instanceof SpecialOffer))
                    .reduce((tariff1, tariff2) ->
                            (tariff1.getPrice() < tariff2.getPrice()) ? tariff1 : tariff2)
                    .orElseGet(() -> {
                         Tariff fakeTariff = new Tariff();
                         fakeTariff.setPrice(0.0);
                         return fakeTariff;
                    });
        }
        return bestTariff.getPrice() * sessionDuration;
    }

    private boolean matches(SpecialOffer specialOffer, Reservation reservation, long sessionDuration) {
        if ((specialOffer.getMaxDuration() != null && sessionDuration > specialOffer.getMaxDuration()) ||
                (specialOffer.getMinDuration() != null && sessionDuration < specialOffer.getMinDuration())) {
            return false;
        }
        if ((specialOffer.getStartTime() != null && reservation.getStartTime().toLocalTime().isBefore(specialOffer.getStartTime())) ||
                (specialOffer.getEndTime() != null && reservation.getEndTime().toLocalTime().isAfter(specialOffer.getEndTime()))) {
            return false;
        }
        if ((specialOffer.getMinKWh() != null && reservation.getEnergyAmount() < specialOffer.getMinKWh()) ||
                (specialOffer.getMaxKWh() != null && reservation.getEnergyAmount() > specialOffer.getMaxKWh())) {
            return false;
        }
        return specialOffer.getDaysOfTheWeek() == null ||
                specialOffer.getDaysOfTheWeek().contains(LocalDate.now().getDayOfWeek().toString());
    }
}
