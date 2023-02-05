package softwareengineering.manonisgaravattiferretti.cpmsServer.socketStatusManager.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.ChargingPoint;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.DSOOffer;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.Reservation;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.ChargingPointService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.DSOOfferService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.ReservationService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.utils.EntityFromDTOConverter;
import softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.messages.chargingPointReq.dtos.MeterValue;
import softwareengineering.manonisgaravattiferretti.cpmsServer.dataWarehouse.entity.DimensionsPrimaryKey;
import softwareengineering.manonisgaravattiferretti.cpmsServer.dataWarehouse.entity.EnergyConsumption;
import softwareengineering.manonisgaravattiferretti.cpmsServer.dataWarehouse.service.EnergyConsumptionService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.emspUpdateSender.OcpiSessionSender;
import softwareengineering.manonisgaravattiferretti.cpmsServer.socketStatusManager.events.MeterValueEvent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class MeterValueListener implements ApplicationListener<MeterValueEvent> {
    private final OcpiSessionSender ocpiSessionSender;
    private final ReservationService reservationService;
    private final EnergyConsumptionService energyConsumptionService;
    private final DSOOfferService dsoOfferService;
    private final ChargingPointService chargingPointService;
    private final Logger logger = LoggerFactory.getLogger(MeterValueListener.class);

    @Autowired
    public MeterValueListener(OcpiSessionSender ocpiSessionSender, ReservationService reservationService,
                              EnergyConsumptionService energyConsumptionService, DSOOfferService dsoOfferService,
                              ChargingPointService chargingPointService) {
        this.ocpiSessionSender = ocpiSessionSender;
        this.reservationService = reservationService;
        this.energyConsumptionService = energyConsumptionService;
        this.dsoOfferService = dsoOfferService;
        this.chargingPointService = chargingPointService;
    }

    @Override
    public void onApplicationEvent(MeterValueEvent event) {
        Optional<Reservation> reservationOptional = reservationService.findReservationByInternalId(event.getReservationId());
        if (reservationOptional.isEmpty() || !reservationOptional.get().getStatus().equals("ACTIVE")) {
            return;
        }
        Double energyConsumed = event.getMeterValues().stream()
                .map(MeterValue::getSampledValue)
                .reduce(0.0, Double::sum);
        Reservation reservation = reservationOptional.get();
        double energyAmount = energyConsumed + ((reservation.getEnergyAmount() == null) ? 0.0 : reservation.getEnergyAmount());
        reservation.setEnergyAmount(energyAmount);
        reservation.setLastUpdated(LocalDateTime.now());
        reservation.setBatteryPercentage(event.getBatteryPercentage());
        reservationService.insertReservation(reservation);
        ocpiSessionSender.patchSession(EntityFromDTOConverter.chargingSessionDTOFromReservation(reservation),
                reservation.getEmspDetails());
        updateDataWarehouse(event, reservation.getSocket().getCpId());
    }

    private void updateDataWarehouse(MeterValueEvent meterValueEvent, String cpExternalId) {
        logger.info("Updating data warehouse from meter values of sockets of cp with external id = " + cpExternalId);
        Optional<ChargingPoint> chargingPointOptional = chargingPointService.findChargingPointByExternalId(cpExternalId);
        if (chargingPointOptional.isEmpty()) return;
        LocalTime localTime = LocalTime.now();
        List<DSOOffer> dsoOffers = dsoOfferService.findCurrentCpOffers(chargingPointOptional.get().getId())
                .stream().filter(dsoOffer -> dsoOffer.getAvailableTimeSlot().getStartTime().isAfter(localTime) &&
                        dsoOffer.getAvailableTimeSlot().getEndTime().isBefore(localTime)).toList();
        if (dsoOffers.size() < 1) return;
        String dsoId = dsoOffers.get(0).getDsoId();
        List<EnergyConsumption> energyConsumptions = new ArrayList<>();
        for (MeterValue meterValue: meterValueEvent.getMeterValues()) {
            DimensionsPrimaryKey dimensionsPrimaryKey = new DimensionsPrimaryKey(dsoId, cpExternalId, meterValue.getTimestamp());
            EnergyConsumption energyConsumption = new EnergyConsumption(dimensionsPrimaryKey, 0,
                    meterValue.getSampledValue(), 0);
            energyConsumptions.add(energyConsumption);
        }
        energyConsumptionService.insertBatch(energyConsumptions);
    }
}
