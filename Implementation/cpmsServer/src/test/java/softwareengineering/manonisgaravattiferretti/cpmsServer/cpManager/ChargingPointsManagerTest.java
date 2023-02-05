package softwareengineering.manonisgaravattiferretti.cpmsServer.cpManager;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import softwareengineering.manonisgaravattiferretti.cpmsServer.authManager.LoginManager;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.*;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.*;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.repositories.CPORepository;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.ChargingPointService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.DSOOfferService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.SocketService;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ChargingPointsManagerTest {
    @Autowired
    ChargingPointService chargingPointService;
    @Autowired
    ChargingPointsManager chargingPointsManager;
    @Autowired
    LoginManager loginManager;
    @Autowired
    SocketService socketService;
    @Autowired
    CPORepository cpoRepository;
    @Autowired
    DSOOfferService dsoOfferService;
    private CPO cpo;
    private ChargingPoint chargingPoint;
    private Socket socket;
    private Tariff tariff;
    private DSOOffer dsoOffer;

    @BeforeEach
    void setup() {
        Optional<ChargingPoint> chargingPointOptional = chargingPointService.findChargingPointByExternalId("prova");
        chargingPointOptional.ifPresent(chargingPoint1 -> chargingPointService.deleteChargingPoint(chargingPoint1.getId()));
        cpo = new CPO();
        cpo.setCpoCode("test login");
        cpo.setPassword("test");
        cpo = cpoRepository.save(cpo);

        this.chargingPoint = new ChargingPoint();
        this.chargingPoint.setCpId("prova");
        this.chargingPoint.setCpoCode("test login");
        this.chargingPoint.setToggleEnergyMixOptimizer(true);
        this.chargingPoint.setToggleDSOSelectionOptimizer(true);
        this.chargingPoint.setTogglePriceOptimizer(true);
        Battery battery = new Battery();
        battery.setBatteryId(1);
        this.chargingPoint.setBatteries(List.of(battery));
        socket = new Socket();
        socket.setSocketId(1);
        socket.setCpId("prova");
        socket.setStatus("AVAILABLE");
        socket.setCpoCode("test login");
        socket.setStatus("RESERVED");
        socket.setAvailability("AVAILABLE");
        socket.setType("FAST");
        socket = socketService.save(socket);
        this.chargingPoint.setSockets(List.of(socket));
        this.tariff = new Tariff();
        tariff.setTariffId(UUID.randomUUID().toString());
        tariff.setSocketType("FAST");
        tariff.setPrice(10.0);
        chargingPoint.setTariffs(List.of(tariff));
        chargingPointService.addChargingPoint(this.chargingPoint);

        dsoOffer = new DSOOffer();
        dsoOffer.setChargingPointId("prova");
        dsoOffer.setChargingPointInternalId(chargingPoint.getId());
        dsoOffer.setDsoId("abc");
        dsoOffer.setInUse(false);
        dsoOffer.setValid(true);
        dsoOffer.setPrice(5.0);
        dsoOffer.setAvailableTimeSlot(new OfferTimeSlot(LocalTime.MIN, LocalTime.MAX));
        dsoOffer = dsoOfferService.insertOffer(dsoOffer);
    }

    @Test
    void testChargingPointAbsent() {
        Assertions.assertThrows(ResponseStatusException.class, () -> chargingPointsManager.getCPById(cpo, "not an id"));
    }

    @Test
    void getSocketsTest() {
        ResponseEntity<Iterable<Socket>> response = chargingPointsManager.getSockets(cpo, chargingPoint.getId());
        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        List<Socket> sockets = (List<Socket>) response.getBody();
        Assertions.assertNotNull(sockets);
        Assertions.assertEquals(1, sockets.size());
        Assertions.assertEquals(1, sockets.get(0).getSocketId());
    }

    @Test
    void getSocketTest() {
        ResponseEntity<Socket> response = chargingPointsManager.getSocketInfo(cpo, chargingPoint.getId(), socket.getSocketId());
        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Socket socket1 = response.getBody();
        Assertions.assertNotNull(socket1);
        Assertions.assertEquals(socket.getId(), socket1.getId());
    }

    @Test
    void addChargingPointTest() {
        AddChargingPointDTO addChargingPointDTO = new AddChargingPointDTO();
        addChargingPointDTO.setCpId("test add");
        addChargingPointDTO.setSockets(List.of());
        addChargingPointDTO.setTariffs(List.of());
        ResponseEntity<?> response = chargingPointsManager.addChargingPoint(addChargingPointDTO, cpo);
        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Optional<ChargingPoint> chargingPoint = chargingPointService.findChargingPointByExternalId("test add");
        Assertions.assertTrue(chargingPoint.isPresent());
        chargingPointService.deleteChargingPoint(chargingPoint.get().getId());
    }

    @Test
    void deleteChargingPointTest() {
        ResponseEntity<?> response = chargingPointsManager.deleteChargingPoint(cpo, chargingPoint.getId());
        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Optional<ChargingPoint> chargingPoint = chargingPointService.findChargingPointByExternalId("test add");
        Assertions.assertTrue(chargingPoint.isEmpty());
        List<Socket> socket = socketService.findCpSocketsById(this.chargingPoint.getCpId());
        Assertions.assertEquals(0, socket.size());
    }

    @Test
    void includeBatteryTest() {
        IncludeBatteryDTO includeBatteryDTO = new IncludeBatteryDTO();
        includeBatteryDTO.setMinLevel(10.0);
        includeBatteryDTO.setMaxLevel(80.0);
        includeBatteryDTO.setPercent(30.0);
        ResponseEntity<?> response = chargingPointsManager.includeBattery(chargingPoint.getId(), 1, includeBatteryDTO, cpo);
        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Optional<ChargingPoint> chargingPoint = chargingPointService.findChargingPointByExternalId("prova");
        Assertions.assertTrue(chargingPoint.isPresent());
        Assertions.assertEquals("IDLE", chargingPoint.get().getBatteries().get(0).getStatus());
        Assertions.assertEquals(10.0, chargingPoint.get().getBatteries().get(0).getMinLevel());
        Assertions.assertEquals(80.0, chargingPoint.get().getBatteries().get(0).getMaxLevel());
        Assertions.assertEquals(30.0, chargingPoint.get().getBatteries().get(0).getPercent());
        // energy mix optimizer should be switched off due to manual intervention
        Assertions.assertFalse(chargingPoint.get().isToggleEnergyMixOptimizer());
    }

    @Test
    void includeBatteryTest_wrongLevels() {
        IncludeBatteryDTO includeBatteryDTO = new IncludeBatteryDTO();
        includeBatteryDTO.setMinLevel(80.0);
        includeBatteryDTO.setMaxLevel(10.0);
        includeBatteryDTO.setPercent(30.0);
        Assertions.assertThrows(ResponseStatusException.class, () -> chargingPointsManager.includeBattery(
                chargingPoint.getId(), 1, includeBatteryDTO, cpo));
    }

    @Test
    void changeSocketAvailabilityTest() {
        ChangeSocketAvailabilityDTO changeSocketAvailabilityDTO = new ChangeSocketAvailabilityDTO(false,
                LocalDateTime.now().plus(10, ChronoUnit.HOURS));
        ResponseEntity<?> response = chargingPointsManager
                .updateSocketAvailability(chargingPoint.getId(), 1, changeSocketAvailabilityDTO, cpo);
        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Optional<ChargingPoint> chargingPoint = chargingPointService.findChargingPointByExternalId("prova");
        Assertions.assertTrue(chargingPoint.isPresent());
        Assertions.assertEquals(1, chargingPoint.get().getSockets().size());
        Socket socket1 = chargingPoint.get().getSockets().get(0);
        Assertions.assertEquals(1, socket1.getSocketId());
        Assertions.assertEquals("NOT_AVAILABLE", socket1.getAvailability());
    }

    @Test
    void changeBatteryAvailabilityTest() {
        ResponseEntity<?> response = chargingPointsManager.changeBatteryAvailability(chargingPoint.getId(), 1,
                false, cpo);
        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Optional<ChargingPoint> chargingPoint = chargingPointService.findChargingPointByExternalId("prova");
        Assertions.assertTrue(chargingPoint.isPresent());
        Assertions.assertEquals(1, chargingPoint.get().getBatteries().size());
        Battery battery = chargingPoint.get().getBatteries().get(0);
        Assertions.assertEquals(1, battery.getBatteryId());
        Assertions.assertEquals("UNAVAILABLE", battery.getStatus());
    }

    @Test
    void getTariffsTest() {
        ResponseEntity<?> response = chargingPointsManager.getTariffs(cpo, chargingPoint.getId());
        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Optional<ChargingPoint> chargingPoint = chargingPointService.findChargingPointByExternalId("prova");
        Assertions.assertTrue(chargingPoint.isPresent());
        Assertions.assertEquals(1, chargingPoint.get().getTariffs().size());
        Tariff tariff1 = chargingPoint.get().getTariffs().get(0);
        Assertions.assertEquals(tariff.getTariffId(), tariff1.getTariffId());
        Assertions.assertEquals(10.0, tariff1.getPrice());
    }

    @Test
    void getTariffTest() {
        ResponseEntity<Tariff> response = chargingPointsManager.getTariff(cpo, chargingPoint.getId(), tariff.getTariffId());
        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Optional<ChargingPoint> chargingPoint = chargingPointService.findChargingPointByExternalId("prova");
        Assertions.assertTrue(chargingPoint.isPresent());
        Assertions.assertEquals(1, chargingPoint.get().getTariffs().size());
        Tariff tariff1 = response.getBody();
        Assertions.assertNotNull(tariff1);
        Assertions.assertEquals(tariff.getTariffId(), tariff1.getTariffId());
        Assertions.assertEquals(10.0, tariff1.getPrice());
    }

    @Test
    void deleteTariffTest() {
        ResponseEntity<?> response = chargingPointsManager.deleteTariff(chargingPoint.getId(), tariff.getTariffId(), cpo);
        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Optional<ChargingPoint> chargingPoint = chargingPointService.findChargingPointByExternalId("prova");
        Assertions.assertTrue(chargingPoint.isPresent());
        Assertions.assertEquals(0, chargingPoint.get().getTariffs().size());
        Assertions.assertFalse(chargingPoint.get().isTogglePriceOptimizer());
    }

    @Test
    void addTariff() {
        AddTariffDTO addTariffDTO = new AddTariffDTO();
        addTariffDTO.setSocketType("SLOW");
        addTariffDTO.setIsSpecialOffer(false);
        ResponseEntity<?> response = chargingPointsManager.addNewTariff(chargingPoint.getId(), cpo, addTariffDTO);
        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Optional<ChargingPoint> chargingPoint = chargingPointService.findChargingPointByExternalId("prova");
        Assertions.assertTrue(chargingPoint.isPresent());
        Assertions.assertEquals(2, chargingPoint.get().getTariffs().size());
        Tariff insertedTariff = chargingPoint.get().getTariffs().get(1);
        Assertions.assertEquals("SLOW", insertedTariff.getSocketType());
        Assertions.assertFalse(chargingPoint.get().isTogglePriceOptimizer());
    }

    @Test
    void modifyTariffTest() {
        AddTariffDTO addTariffDTO = new AddTariffDTO();
        addTariffDTO.setSocketType("SLOW");
        addTariffDTO.setPrice(11.0);
        addTariffDTO.setIsSpecialOffer(false);
        ResponseEntity<?> response = chargingPointsManager.putTariff(chargingPoint.getId(), tariff.getTariffId(), addTariffDTO, cpo);
        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Optional<ChargingPoint> chargingPoint = chargingPointService.findChargingPointByExternalId("prova");
        Assertions.assertTrue(chargingPoint.isPresent());
        Assertions.assertEquals(1, chargingPoint.get().getTariffs().size());
        Tariff tariff1 = chargingPoint.get().getTariffs().get(0);
        Assertions.assertEquals(tariff.getTariffId(), tariff1.getTariffId());
        Assertions.assertEquals("SLOW", tariff1.getSocketType());
        Assertions.assertEquals(11.0, tariff1.getPrice());
        Assertions.assertFalse(chargingPoint.get().isTogglePriceOptimizer());
    }

    @Test
    void modifyTariff_wrongDto() {
        Assertions.assertThrows(ResponseStatusException.class, () -> chargingPointsManager.putTariff(
                chargingPoint.getId(), "test", new AddTariffDTO(), cpo));
    }

    @Test
    void getDsoOffers() {
        ResponseEntity<Iterable<DSOOfferDTO>> response = chargingPointsManager.getChargingPointDsoOffers(chargingPoint.getId(), cpo);
        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        List<DSOOfferDTO> dsoOffers = (List<DSOOfferDTO>) response.getBody();
        Assertions.assertNotNull(dsoOffers);
        Assertions.assertEquals(1, dsoOffers.size());
        Assertions.assertEquals("abc", dsoOffers.get(0).getDsoId());
        Assertions.assertEquals(dsoOffer.getId(), dsoOffers.get(0).getOfferId());
        Assertions.assertEquals(5.0, dsoOffers.get(0).getPrice());
    }

    @Test
    void changeDsoProvider() {
        OfferTimeSlot offerTimeSlot = new OfferTimeSlot(LocalTime.MIN, LocalTime.of(23, 59, 59));
        ResponseEntity<?> response = chargingPointsManager.changeDsoProvider(chargingPoint.getId(),
                dsoOffer.getId(), offerTimeSlot, cpo);
        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Optional<DSOOffer> dsoOffer1 = dsoOfferService.findOfferById(dsoOffer.getId());
        Assertions.assertTrue(dsoOffer1.isPresent());
        Assertions.assertEquals(dsoOffer.getDsoId(), dsoOffer1.get().getDsoId());
        Assertions.assertEquals(dsoOffer.getPrice(), dsoOffer1.get().getPrice());
        Assertions.assertTrue(dsoOffer1.get().isInUse());
        Assertions.assertEquals(offerTimeSlot, dsoOffer1.get().getUsedTimeSlot());
        Optional<ChargingPoint> chargingPoint = chargingPointService.findChargingPointByExternalId("prova");
        Assertions.assertTrue(chargingPoint.isPresent());
        // dso selection optimizer should be switched off due to manual intervention
        Assertions.assertFalse(chargingPoint.get().isToggleDSOSelectionOptimizer());
    }

    @Test
    void testTogglePriceOptimizer() {
        ResponseEntity<?> response = chargingPointsManager.toggleOptimizer(chargingPoint.getId(), "price", false);
        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Optional<ChargingPoint> chargingPoint = chargingPointService.findChargingPointByInternalId(this.chargingPoint.getId());
        Assertions.assertTrue(chargingPoint.isPresent());
        Assertions.assertFalse(chargingPoint.get().isTogglePriceOptimizer());
    }

    @Test
    void testToggleDsoOptimizer() {
        ResponseEntity<?> response = chargingPointsManager.toggleOptimizer(chargingPoint.getId(), "dsoSelection", false);
        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Optional<ChargingPoint> chargingPoint = chargingPointService.findChargingPointByInternalId(this.chargingPoint.getId());
        Assertions.assertTrue(chargingPoint.isPresent());
        Assertions.assertFalse(chargingPoint.get().isToggleDSOSelectionOptimizer());
    }

    @Test
    void testToggleEnergyMixOptimizer() {
        ResponseEntity<?> response = chargingPointsManager.toggleOptimizer(chargingPoint.getId(), "energyMix", false);
        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Optional<ChargingPoint> chargingPoint = chargingPointService.findChargingPointByInternalId(this.chargingPoint.getId());
        Assertions.assertTrue(chargingPoint.isPresent());
        Assertions.assertFalse(chargingPoint.get().isToggleEnergyMixOptimizer());
    }

    @AfterEach
    void teardown() {
        chargingPointService.deleteChargingPoint(chargingPoint.getId());
        socketService.removeSocket(socket.getId());
        cpoRepository.deleteById(cpo.getId());
        dsoOfferService.removeOffer(List.of(dsoOffer));
    }

}