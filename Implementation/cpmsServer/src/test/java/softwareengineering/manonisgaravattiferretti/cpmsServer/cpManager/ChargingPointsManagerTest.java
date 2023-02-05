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
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.CPOService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.ChargingPointService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.SocketService;

import java.time.LocalDateTime;
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
    private CPO cpo;
    private ChargingPoint chargingPoint;
    private Socket socket;
    private Tariff tariff;

    @BeforeEach
    void setup() {
        cpo = new CPO();
        cpo.setCpoCode("test login");
        cpo.setPassword("test");
        cpo = cpoRepository.save(cpo);

        this.chargingPoint = new ChargingPoint();
        this.chargingPoint.setCpId("prova");
        this.chargingPoint.setCpoCode("test login");
        this.chargingPoint.setToggleEnergyMixOptimizer(true);
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
    void testToggleOptimizer() {
        ResponseEntity<?> response = chargingPointsManager.toggleOptimizer(chargingPoint.getId(), "energyMix", false);
        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Optional<ChargingPoint> chargingPoint = chargingPointService.findChargingPointByInternalId(this.chargingPoint.getId());
        Assertions.assertTrue(chargingPoint.isPresent());
        Assertions.assertFalse(chargingPoint.get().isTogglePriceOptimizer());
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
    }

    @AfterEach
    void teardown() {
        chargingPointService.deleteChargingPoint(chargingPoint.getId());
        socketService.removeSocket(socket.getId());
        cpoRepository.deleteById(cpo.getId());
    }

}