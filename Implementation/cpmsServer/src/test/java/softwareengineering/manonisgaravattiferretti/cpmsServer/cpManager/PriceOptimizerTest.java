package softwareengineering.manonisgaravattiferretti.cpmsServer.cpManager;

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
import softwareengineering.manonisgaravattiferretti.cpmsServer.cpManager.events.TariffOptimizerEvent;
import softwareengineering.manonisgaravattiferretti.cpmsServer.dataWarehouse.service.OfferPriceService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PriceOptimizerTest {

    @Mock
    private ChargingPointService mockChargingPointService;
    @Mock
    private DSOOfferService mockDsoOfferService;
    @Mock
    private OfferPriceService mockOfferPriceService;
    @Mock
    private ApplicationEventPublisher mockApplicationEventPublisher;

    private PriceOptimizer priceOptimizerUnderTest;

    @BeforeEach
    void setUp() {
        priceOptimizerUnderTest = new PriceOptimizer(mockChargingPointService, mockDsoOfferService,
                mockOfferPriceService, mockApplicationEventPublisher);
    }

    @Test
    void testOptimizeCp_SpecialOfferConvenience() {
        ChargingPoint chargingPoint = new ChargingPoint();
        chargingPoint.setId("id");
        chargingPoint.setCpId("cpId");
        Tariff tariff = new Tariff();
        tariff.setTariffId("tariffId");
        tariff.setSocketType("FAST");
        tariff.setPrice(3.0);
        chargingPoint.setTariffs(List.of(tariff));
        SpecialOffer specialOffer = new SpecialOffer();
        specialOffer.setTariffId("specialOffer");
        specialOffer.setSocketType("FAST");
        specialOffer.setPrice(2.0);
        chargingPoint.setTariffs(List.of(tariff, specialOffer));
        Optional<ChargingPoint> chargingPointOptional = Optional.of(chargingPoint);
        when(mockChargingPointService.findChargingPointByInternalId("id")).thenReturn(chargingPointOptional);

        DSOOffer dsoOffer = new DSOOffer();
        dsoOffer.setId("id");
        dsoOffer.setPrice(1.0);
        dsoOffer.setValid(true);
        dsoOffer.setDsoId("dsoId");
        OfferTimeSlot availableTimeSlot = new OfferTimeSlot();
        availableTimeSlot.setStartTime(LocalTime.of(0, 0, 0));
        availableTimeSlot.setEndTime(LocalTime.of(23, 59, 59));
        dsoOffer.setAvailableTimeSlot(availableTimeSlot);
        List<DSOOffer> dsoOffers = List.of(dsoOffer);
        when(mockDsoOfferService.findCurrentCpOffers("id")).thenReturn(dsoOffers);

        when(mockOfferPriceService.findMeanPrice(eq("cpId"), eq("dsoId"), any(LocalDateTime.class),
                any(LocalDateTime.class))).thenReturn(4.0);

        priceOptimizerUnderTest.optimizeCp("id");

        ArgumentCaptor<TariffOptimizerEvent> argumentCaptor = ArgumentCaptor.forClass(TariffOptimizerEvent.class);
        verify(mockApplicationEventPublisher, times(3)).publishEvent(argumentCaptor.capture());
        List<TariffOptimizerEvent> events = argumentCaptor.getAllValues();
        Assertions.assertEquals("M", events.get(0).getEventType());
        Assertions.assertEquals(4.0, events.get(0).getTariff().getPrice());
        Assertions.assertTrue(events.get(1).getTariff() instanceof SpecialOffer);
        Assertions.assertEquals("R", events.get(1).getEventType());
        Assertions.assertEquals("A", events.get(2).getEventType());
        Assertions.assertTrue(events.get(2).getAddTariffDTO().getIsSpecialOffer());
        Assertions.assertEquals(1 / 0.75, events.get(2).getAddTariffDTO().getPrice());
    }
}
