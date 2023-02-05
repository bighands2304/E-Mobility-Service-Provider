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
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.ChargingPoint;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.DSOOffer;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.OfferTimeSlot;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.Tariff;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.ChargingPointService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.DSOOfferService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.cpManager.events.TariffOptimizerEvent;
import softwareengineering.manonisgaravattiferretti.cpmsServer.dataWarehouse.service.EnergyConsumptionService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.energyManager.events.DSOOptimizerEvent;

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
class DSOSelectionOptimizerTest {

    @Mock
    private ChargingPointService mockChargingPointService;
    @Mock
    private DSOOfferService mockDsoOfferService;
    @Mock
    private EnergyConsumptionService mockEnergyConsumptionService;
    @Mock
    private ApplicationEventPublisher mockApplicationEventPublisher;

    private DSOSelectionOptimizer dsoSelectionOptimizerUnderTest;

    @BeforeEach
    void setUp() {
        dsoSelectionOptimizerUnderTest = new DSOSelectionOptimizer(mockChargingPointService, mockDsoOfferService,
                mockEnergyConsumptionService, mockApplicationEventPublisher);
    }

    @Test
    void testOptimizeCp_oneOfferNotFulfillConsumption() {
        when(mockEnergyConsumptionService.findMeanConsumption(eq("cpIdExternal"), any(LocalDateTime.class),
                any(LocalDateTime.class))).thenReturn(100.0);

        ChargingPoint chargingPoint = new ChargingPoint();
        chargingPoint.setId("cpId");
        chargingPoint.setCpId("cpIdExternal");
        when(mockChargingPointService.findChargingPointByInternalId("cpId")).thenReturn(Optional.of(chargingPoint));

        DSOOffer dsoOffer1 = new DSOOffer();
        dsoOffer1.setId("id1");
        dsoOffer1.setPrice(10.0);
        dsoOffer1.setValid(true);
        dsoOffer1.setCapacity(95.0);
        dsoOffer1.setChargingPointId("cpIdExternal");
        dsoOffer1.setChargingPointInternalId("cpId");
        OfferTimeSlot availableTimeSlot = new OfferTimeSlot();
        availableTimeSlot.setStartTime(LocalTime.of(0, 0, 0));
        availableTimeSlot.setEndTime(LocalTime.MAX);
        dsoOffer1.setAvailableTimeSlot(availableTimeSlot);
        dsoOffer1.setInUse(true);
        DSOOffer dsoOffer2 = new DSOOffer();
        dsoOffer2.setId("id2");
        dsoOffer2.setPrice(10.0);
        dsoOffer2.setValid(true);
        dsoOffer2.setCapacity(105.0);
        dsoOffer2.setChargingPointId("cpIdExternal");
        dsoOffer2.setChargingPointInternalId("cpId");
        dsoOffer2.setAvailableTimeSlot(availableTimeSlot);
        dsoOffer2.setInUse(false);
        when(mockDsoOfferService.findOffersOfCp("cpId")).thenReturn(List.of(dsoOffer1, dsoOffer2));

        dsoSelectionOptimizerUnderTest.optimizeCp("cpId", LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                LocalDateTime.of(2020, 1, 1, 1, 0, 0));

        ArgumentCaptor<DSOOptimizerEvent> argumentCaptor = ArgumentCaptor.forClass(DSOOptimizerEvent.class);
        verify(mockApplicationEventPublisher).publishEvent(argumentCaptor.capture());
        DSOOptimizerEvent dsoOptimizerEvent = argumentCaptor.getValue();
        // the second should be preferred since it fulfill the requirements of the cp
        Assertions.assertEquals(dsoOffer2, dsoOptimizerEvent.getDsoOffer());
    }

    @Test
    void testOptimizeCp_priceComparison() {
        when(mockEnergyConsumptionService.findMeanConsumption(eq("cpIdExternal"), any(LocalDateTime.class),
                any(LocalDateTime.class))).thenReturn(100.0);

        ChargingPoint chargingPoint = new ChargingPoint();
        chargingPoint.setId("cpId");
        chargingPoint.setCpId("cpIdExternal");
        when(mockChargingPointService.findChargingPointByInternalId("cpId")).thenReturn(Optional.of(chargingPoint));

        DSOOffer dsoOffer1 = new DSOOffer();
        dsoOffer1.setId("id1");
        dsoOffer1.setPrice(10.0);
        dsoOffer1.setValid(true);
        dsoOffer1.setCapacity(105.0);
        dsoOffer1.setChargingPointId("cpIdExternal");
        dsoOffer1.setChargingPointInternalId("cpId");
        OfferTimeSlot availableTimeSlot = new OfferTimeSlot();
        availableTimeSlot.setStartTime(LocalTime.of(0, 0, 0));
        availableTimeSlot.setEndTime(LocalTime.MAX);
        dsoOffer1.setAvailableTimeSlot(availableTimeSlot);
        dsoOffer1.setInUse(true);
        DSOOffer dsoOffer2 = new DSOOffer();
        dsoOffer2.setId("id2");
        dsoOffer2.setPrice(9.0);
        dsoOffer2.setValid(true);
        dsoOffer2.setCapacity(105.0);
        dsoOffer2.setChargingPointId("cpIdExternal");
        dsoOffer2.setChargingPointInternalId("cpId");
        dsoOffer2.setAvailableTimeSlot(availableTimeSlot);
        dsoOffer2.setInUse(false);
        when(mockDsoOfferService.findOffersOfCp("cpId")).thenReturn(List.of(dsoOffer1, dsoOffer2));

        dsoSelectionOptimizerUnderTest.optimizeCp("cpId", LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                LocalDateTime.of(2020, 1, 1, 0, 0, 0));

        ArgumentCaptor<DSOOptimizerEvent> argumentCaptor = ArgumentCaptor.forClass(DSOOptimizerEvent.class);
        verify(mockApplicationEventPublisher).publishEvent(argumentCaptor.capture());
        DSOOptimizerEvent dsoOptimizerEvent = argumentCaptor.getValue();
        // the second should be preferred (both are feasible for the capacity) since its price is better
        Assertions.assertEquals(dsoOffer2, dsoOptimizerEvent.getDsoOffer());
    }
}
