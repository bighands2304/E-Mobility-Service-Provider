package softwareengineering.manonisgaravattiferretti.cpmsServer.energyManager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.*;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.ChargingPointService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.DSOOfferService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.dataWarehouse.service.EnergyConsumptionService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.energyManager.events.DSOOptimizerEvent;
import softwareengineering.manonisgaravattiferretti.cpmsServer.energyManager.events.EnergyMixOptimizerEvent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EnergyMixOptimizerTest {
    @Mock
    private ChargingPointService mockChargingPointService;
    @Mock
    private EnergyConsumptionService mockEnergyConsumptionService;
    @Mock
    private DSOOfferService mockDsoOfferService;
    @Mock
    private ApplicationEventPublisher mockApplicationEventPublisher;

    private EnergyMixOptimizer energyMixOptimizerUnderTest;

    @BeforeEach
    void setUp() {
        energyMixOptimizerUnderTest = new EnergyMixOptimizer(mockChargingPointService, mockEnergyConsumptionService,
                mockDsoOfferService, mockApplicationEventPublisher);
    }

    @Test
    void testOptimizeCp() {
        ChargingPoint chargingPoint = new ChargingPoint();
        chargingPoint.setId("id");
        chargingPoint.setCpId("cpId");
        final Battery battery = new Battery();
        battery.setBatteryId(0);
        chargingPoint.setBatteries(List.of(battery));
        final Optional<ChargingPoint> chargingPointOptional = Optional.of(chargingPoint);
        when(mockChargingPointService.findChargingPointByInternalId("id")).thenReturn(chargingPointOptional);

        when(mockEnergyConsumptionService.findMeanConsumption(eq("cpId"), any(LocalDateTime.class),
                any(LocalDateTime.class))).thenReturn(100.0);

        final DSOOffer dsoOffer = new DSOOffer();
        dsoOffer.setId("id");
        dsoOffer.setPrice(0.0);
        dsoOffer.setValid(false);
        dsoOffer.setCapacity(90.0);
        final OfferTimeSlot availableTimeSlot = new OfferTimeSlot();
        availableTimeSlot.setStartTime(LocalTime.of(0, 0, 0));
        availableTimeSlot.setEndTime(LocalTime.MAX);
        dsoOffer.setAvailableTimeSlot(availableTimeSlot);
        final List<DSOOffer> dsoOffers = List.of(dsoOffer);
        when(mockDsoOfferService.findCurrentCpOffers("id")).thenReturn(dsoOffers);

        energyMixOptimizerUnderTest.optimizeCp("id", LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                LocalDateTime.of(2020, 1, 1, 0, 0, 0));

        ArgumentCaptor<EnergyMixOptimizerEvent> argumentCaptor = ArgumentCaptor.forClass(EnergyMixOptimizerEvent.class);
        verify(mockApplicationEventPublisher).publishEvent(argumentCaptor.capture());
        EnergyMixOptimizerEvent event = argumentCaptor.getValue();
        Assertions.assertEquals(0, event.getBatteryId());
        Assertions.assertEquals("id", event.getCpId());
        // mean consumption is 100, current dso capacity is 90, so batteries should provide 10 percent
        // since there is only one battery, this have to fulfill all
        Assertions.assertEquals(0.1, event.getIncludeBatteryDTO().getPercent());
    }
}
