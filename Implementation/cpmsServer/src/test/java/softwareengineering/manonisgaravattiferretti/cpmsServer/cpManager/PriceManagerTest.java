package softwareengineering.manonisgaravattiferretti.cpmsServer.cpManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.AddTariffDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.*;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.ChargingPointService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.EmspDetailsService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.ReservationService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.cpManager.events.TogglePriceOptimizerEvent;
import softwareengineering.manonisgaravattiferretti.cpmsServer.emspUpdateSender.OcpiTariffSender;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PriceManagerTest {
    @Mock
    private ChargingPointService mockChargingPointService;
    @Mock
    private PriceOptimizer mockPriceOptimizer;
    @Mock
    private OcpiTariffSender mockOcpiTariffSender;
    @Mock
    private EmspDetailsService mockEmspDetailsService;
    @Mock
    private ReservationService mockReservationService;

    private PriceManager priceManagerUnderTest;

    @BeforeEach
    void setUp() {
        priceManagerUnderTest = new PriceManager(mockChargingPointService, mockPriceOptimizer, mockOcpiTariffSender,
                mockEmspDetailsService, mockReservationService);
    }

    @Test
    void testRemoveTariff() {
        final EmspDetails emspDetails = new EmspDetails();
        emspDetails.setId("id");
        emspDetails.setEmspToken("emspToken");
        emspDetails.setUrl("url");
        emspDetails.setCpoToken("cpoToken");
        final List<EmspDetails> emspDetailsList = List.of(emspDetails);
        when(mockEmspDetailsService.findAll()).thenReturn(emspDetailsList);

        priceManagerUnderTest.removeTariff("cpId", "tariffId");

        verify(mockPriceOptimizer).switchOptimizer("cpId", false);
        verify(mockChargingPointService).removeTariff("cpId", "tariffId");
        verify(mockOcpiTariffSender).deleteTariff("tariffId", emspDetails);
    }

    @Test
    void testApplyTariff() {
        ChargingPoint chargingPoint = new ChargingPoint();
        chargingPoint.setId("id");
        chargingPoint.setCpId("cpId");
        Tariff tariff = new Tariff();
        tariff.setTariffId("tariffId");
        tariff.setSocketType("FAST");
        tariff.setStartDate(LocalDate.of(2020, 1, 1));
        tariff.setPrice(10.0);
        chargingPoint.setTariffs(List.of(tariff));
        Optional<ChargingPoint> chargingPointOptional = Optional.of(chargingPoint);
        when(mockChargingPointService.findChargingPointByExternalId("cpId")).thenReturn(chargingPointOptional);

        Reservation reservation = new Reservation();
        reservation.setId("id");
        reservation.setReservationIdEmsp(0L);
        reservation.setInternalReservationId(1L);
        reservation.setStatus("COMPLETED");
        reservation.setStartTime(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        reservation.setEndTime(LocalDateTime.of(2020, 1, 1, 1, 0, 0));
        reservation.setEnergyAmount(10.0);
        reservation.setTotalCost(0.0);
        Socket socket = new Socket();
        socket.setId("socketId");
        socket.setType("FAST");
        reservation.setSocket(socket);
        Optional<Reservation> reservationOptional = Optional.of(reservation);
        when(mockReservationService.findReservationByInternalId(1L)).thenReturn(reservationOptional);

        final Double result = priceManagerUnderTest.applyTariff(1L, "cpId");
        assertThat(result).isEqualTo(100.0, within(0.0001));
    }

    @Test
    void testApplyTariff_ChargingPointServiceReturnsAbsent() {
        when(mockChargingPointService.findChargingPointByExternalId("cpId")).thenReturn(Optional.empty());

        final Reservation reservation = new Reservation();
        reservation.setId("id");
        reservation.setInternalReservationId(0L);
        final Socket socket = new Socket();
        socket.setId("id");
        reservation.setSocket(socket);
        final Optional<Reservation> reservationOptional = Optional.of(reservation);
        when(mockReservationService.findReservationByInternalId(0L)).thenReturn(reservationOptional);
        final Double result = priceManagerUnderTest.applyTariff(0L, "cpId");

        assertThat(result).isEqualTo(0.0, within(0.0001));
    }

    @Test
    void testApplyTariff_ReservationServiceReturnsAbsent() {
        final ChargingPoint chargingPoint = new ChargingPoint();
        chargingPoint.setId("id");
        chargingPoint.setCpId("cpId");
        final Tariff tariff = new Tariff();
        tariff.setTariffId("tariffId");
        chargingPoint.setTariffs(List.of(tariff));
        final Optional<ChargingPoint> chargingPointOptional = Optional.of(chargingPoint);
        when(mockChargingPointService.findChargingPointByExternalId("cpId")).thenReturn(chargingPointOptional);

        when(mockReservationService.findReservationByInternalId(0L)).thenReturn(Optional.empty());

        final Double result = priceManagerUnderTest.applyTariff(0L, "cpId");

        assertThat(result).isEqualTo(0.0, within(0.0001));
    }
}
