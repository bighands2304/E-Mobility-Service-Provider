package softwareengineering.manonisgaravattiferretti.cpmsServer.cpManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.AddTariffDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.ChargingPoint;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.DSOOffer;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.SpecialOffer;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.Tariff;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.ChargingPointService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.DSOOfferService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.cpManager.events.TariffOptimizerEvent;
import softwareengineering.manonisgaravattiferretti.cpmsServer.dataWarehouse.service.OfferPriceService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PriceOptimizer {
    private final Map<String, Boolean> optimizerSet = new ConcurrentHashMap<>();
    private final ChargingPointService chargingPointService;
    private final DSOOfferService dsoOfferService;
    private final OfferPriceService offerPriceService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private static final List<String> SOCKET_TYPES = List.of("SLOW", "FAST", "RAPID");

    @Autowired
    public PriceOptimizer(ChargingPointService chargingPointService, DSOOfferService dsoOfferService,
                          OfferPriceService offerPriceService, ApplicationEventPublisher applicationEventPublisher) {
        this.chargingPointService = chargingPointService;
        this.dsoOfferService = dsoOfferService;
        this.offerPriceService = offerPriceService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Scheduled(fixedRate = 60000000)
    public void optimize() {
        for (String cpId: optimizerSet.keySet()) {
            if (optimizerSet.get(cpId)) {
                optimizeCp(cpId);
            }
        }
    }

    @Async
    void optimizeCp(String cpId) {
        Optional<ChargingPoint> chargingPointOptional = chargingPointService.findChargingPointByInternalId(cpId);
        if (chargingPointOptional.isEmpty()) return;
        Optional<DSOOffer> currentTimeOffer = getCurrentOffer(cpId);
        if (currentTimeOffer.isEmpty()) return;
        List<Tariff> currentTariffs = chargingPointOptional.get().getTariffs();
        double currentEnergyPrice = currentTimeOffer.get().getPrice();
        double meanPriceLastWeek = offerPriceService.findMeanPrice(chargingPointOptional.get().getCpId(),
                currentTimeOffer.get().getDsoId(),
                LocalDateTime.now().minus(1, ChronoUnit.WEEKS), LocalDateTime.now());
        double optimalPrice = Math.max(currentEnergyPrice * 1.5, meanPriceLastWeek);
        boolean createSpecialOffer = currentEnergyPrice < meanPriceLastWeek * 0.75;
        if (currentTariffs.size() == 0) {
            buildBasicTariffs(cpId, optimalPrice);
            return;
        }
        for (Tariff tariff: currentTariffs) {
            if (tariff instanceof SpecialOffer) {
                tariff.setEndDate(LocalDate.now());
                applicationEventPublisher.publishEvent(new TariffOptimizerEvent(this, "R", tariff, null, cpId));
            } else {
                tariff.setPrice(optimalPrice);
                applicationEventPublisher.publishEvent(new TariffOptimizerEvent(this, "M", tariff, null, cpId));
            }
        }
        if (createSpecialOffer) {
            AddTariffDTO addTariffDTO = buildSpecialOffer(cpId, currentTimeOffer.get(), currentEnergyPrice / 0.75);
            applicationEventPublisher.publishEvent(new TariffOptimizerEvent(this, "A", null, addTariffDTO, cpId));
        }
    }

    private Optional<DSOOffer> getCurrentOffer(String cpId) {
        List<DSOOffer> currentOffers = dsoOfferService.findCurrentCpOffers(cpId);
        LocalTime localTime = LocalTime.now();
        return currentOffers.stream().filter(dsoOffer ->
                dsoOffer.getAvailableTimeSlot().getStartTime().isBefore(localTime) &&
                        dsoOffer.getAvailableTimeSlot().getEndTime().isAfter(localTime)).findFirst();
    }

    private void buildBasicTariffs(String cpId, double price) {
        for (String socketType: SOCKET_TYPES) {
            AddTariffDTO addTariffDTO = new AddTariffDTO();
            addTariffDTO.setCpId(cpId);
            addTariffDTO.setPrice(price);
            addTariffDTO.setIsSpecialOffer(false);
            addTariffDTO.setStartDate(LocalDate.now());
            addTariffDTO.setStepSize(60);
            addTariffDTO.setSocketType(socketType);
            applicationEventPublisher.publishEvent(new TariffOptimizerEvent(this, "A", null, addTariffDTO, cpId));
        }
    }

    private AddTariffDTO buildSpecialOffer(String cpId, DSOOffer currentOffer, double price) {
        AddTariffDTO addTariffDTO = new AddTariffDTO();
        addTariffDTO.setCpId(cpId);
        addTariffDTO.setPrice(price);
        addTariffDTO.setStartDate(LocalDate.now());
        addTariffDTO.setIsSpecialOffer(true);
        addTariffDTO.setSocketType("FAST");
        addTariffDTO.setStepSize(60);
        addTariffDTO.setStartTime(currentOffer.getAvailableTimeSlot().getStartTime());
        addTariffDTO.setEndTime(currentOffer.getAvailableTimeSlot().getEndTime());
        return addTariffDTO;
    }

    @Scheduled(fixedRate = 600000000)
    public void fetchChargingPointsPeriodically() {
        List<ChargingPoint> chargingPoints = chargingPointService.findAll();
        for (ChargingPoint chargingPoint: chargingPoints) {
            optimizerSet.put(chargingPoint.getId(), chargingPoint.isTogglePriceOptimizer());
        }
    }

    public void switchOptimizer(String cpId, boolean toggle) {
        optimizerSet.put(cpId, toggle);
    }
}
