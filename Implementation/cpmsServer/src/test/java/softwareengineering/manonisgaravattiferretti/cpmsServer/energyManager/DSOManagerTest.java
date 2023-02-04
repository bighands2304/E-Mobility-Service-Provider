package softwareengineering.manonisgaravattiferretti.cpmsServer.energyManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.DSOOffer;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.OfferTimeSlot;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.ChargingPointService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.DSOOfferService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.dsoHandler.openAdrDtos.PricingTimeSlotDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.energyManager.events.ToggleDsoSelectionOptimizerEvent;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DSOManagerTest {

    @Mock
    private ChargingPointService mockChargingPointService;
    @Mock
    private DSOSelectionOptimizer mockDsoSelectionOptimizer;
    @Mock
    private DSOOfferService mockDsoOfferService;
    @Mock
    private ApplicationEventPublisher mockApplicationEventPublisher;

    private DSOManager dsoManagerUnderTest;

    @BeforeEach
    void setUp() {
        dsoManagerUnderTest = new DSOManager(mockChargingPointService, mockDsoSelectionOptimizer, mockDsoOfferService,
                mockApplicationEventPublisher);
    }

    @Test
    void testChangeDsoProvider() {
        OfferTimeSlot offerTimeSlot = new OfferTimeSlot(LocalTime.of(0, 0, 0), LocalTime.of(12, 0, 0));

        DSOOffer dsoOffer = new DSOOffer();
        dsoOffer.setId("id");
        dsoOffer.setPrice(10.0);
        dsoOffer.setValid(true);
        dsoOffer.setCapacity(110.0);
        dsoOffer.setChargingPointId("chargingPointId");
        dsoOffer.setAvailableTimeSlot(offerTimeSlot);
        dsoOffer.setInUse(true);

        DSOOffer currentOffer = new DSOOffer();
        currentOffer.setId("id");
        currentOffer.setPrice(0.0);
        currentOffer.setValid(false);
        currentOffer.setCapacity(0.0);
        currentOffer.setChargingPointId("chargingPointId");
        currentOffer.setUsedTimeSlot(offerTimeSlot);
        currentOffer.setAvailableTimeSlot(offerTimeSlot);
        currentOffer.setInUse(true);
        Optional<DSOOffer> currentOfferOptional = Optional.of(currentOffer);
        when(mockDsoOfferService.findDSOOfferFromCpAndTimeSlot("chargingPointId", offerTimeSlot, true))
                .thenReturn(currentOfferOptional);

        dsoManagerUnderTest.changeDsoProvider("chargingPointId", dsoOffer, offerTimeSlot);

        verify(mockDsoOfferService).insertOffer(dsoOffer);
        verify(mockApplicationEventPublisher).publishEvent(any(ApplicationEvent.class));
    }

    @Test
    void testRefactorOffers_ValidCurrentOffers() {
        DSOOffer dsoOffer = new DSOOffer();
        dsoOffer.setId("id");
        dsoOffer.setPrice(0.4);
        dsoOffer.setValid(true);
        dsoOffer.setCapacity(45.0);
        dsoOffer.setChargingPointId("chargingPointId");
        dsoOffer.setDsoToken("dsoToken");
        OfferTimeSlot availableTimeSlot = new OfferTimeSlot();
        availableTimeSlot.setStartTime(LocalTime.of(0, 0, 0));
        availableTimeSlot.setEndTime(LocalTime.of(23, 59, 59));
        dsoOffer.setAvailableTimeSlot(availableTimeSlot);
        dsoOffer.setInUse(true);
        List<DSOOffer> currentOffers = List.of(dsoOffer);
        PricingTimeSlotDTO pricingTimeSlotDTO = new PricingTimeSlotDTO();
        pricingTimeSlotDTO.setStartTime(LocalTime.of(0, 0, 0));
        pricingTimeSlotDTO.setEndTime(LocalTime.of(23, 59, 59));
        pricingTimeSlotDTO.setUnit("W");
        pricingTimeSlotDTO.setPrice(0.44);
        List<PricingTimeSlotDTO> pricingTimeSlots = List.of(pricingTimeSlotDTO);

        dsoManagerUnderTest.refactorOffers("chargingPointId", "dsoToken", currentOffers, pricingTimeSlots);

        verify(mockDsoOfferService).updateOfferById("id", 0.44);
    }

    @Test
    void testRefactorOffers_NotValidCurrentOffers() {
        DSOOffer dsoOffer = new DSOOffer();
        dsoOffer.setId("id");
        dsoOffer.setPrice(0.0);
        dsoOffer.setValid(false);
        dsoOffer.setCapacity(0.0);
        dsoOffer.setChargingPointId("chargingPointId");
        dsoOffer.setDsoToken("dsoToken");
        List<DSOOffer> currentOffers = List.of(dsoOffer);
        PricingTimeSlotDTO pricingTimeSlotDTO1 = new PricingTimeSlotDTO();
        pricingTimeSlotDTO1.setStartTime(LocalTime.of(0, 0, 0));
        pricingTimeSlotDTO1.setEndTime(LocalTime.of(12, 0, 0));
        pricingTimeSlotDTO1.setUnit("W");
        pricingTimeSlotDTO1.setPrice(0.44);
        PricingTimeSlotDTO pricingTimeSlotDTO2 = new PricingTimeSlotDTO();
        pricingTimeSlotDTO2.setStartTime(LocalTime.of(12, 0, 0));
        pricingTimeSlotDTO2.setEndTime(LocalTime.of(23, 59, 59));
        pricingTimeSlotDTO2.setUnit("W");
        pricingTimeSlotDTO2.setPrice(0.33);
        List<PricingTimeSlotDTO> pricingTimeSlots = List.of(pricingTimeSlotDTO1, pricingTimeSlotDTO2);

        dsoManagerUnderTest.refactorOffers("chargingPointId", "dsoToken", currentOffers, pricingTimeSlots);

        verify(mockDsoOfferService).removeOffer(currentOffers);
        verify(mockDsoOfferService).insertAll(anyList());
    }
}
