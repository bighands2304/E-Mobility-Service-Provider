package softwareengineering.manonisgaravattiferretti.cpmsServer.energyManager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.ChargingPoint;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.ChargingProfile;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.ChargingSchedulePeriod;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.Socket;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.ChargingPointService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.SocketService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.dataWarehouse.service.EnergyConsumptionService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.energyManager.events.EnergyChangeEvent;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChargingProfileOptimizerTest {

    @Mock
    private ChargingPointService mockChargingPointService;
    @Mock
    private SocketService mockSocketService;
    @Mock
    private EnergyConsumptionService mockEnergyConsumptionService;

    private ChargingProfileOptimizer chargingProfileOptimizerUnderTest;

    @BeforeEach
    void setUp() {
        chargingProfileOptimizerUnderTest = new ChargingProfileOptimizer(mockChargingPointService, mockSocketService,
                mockEnergyConsumptionService);
    }

    @Test
    void testOnApplicationEvent_oneSocket() {
        EnergyChangeEvent event = new EnergyChangeEvent("source", "id");

        ChargingPoint chargingPoint = new ChargingPoint();
        chargingPoint.setId("id");
        chargingPoint.setCpId("cpId");
        Socket socket = new Socket();
        socket.setSocketId(1);
        socket.setCpId("cpId");
        socket.setType("FAST");
        ChargingProfile chargingProfile = new ChargingProfile();
        chargingProfile.setChargingProfileId(0);
        chargingProfile.setRecurrencyKind("DAILY");
        ChargingSchedulePeriod chargingSchedulePeriod = new ChargingSchedulePeriod();
        chargingSchedulePeriod.setStart(0);
        chargingSchedulePeriod.setLimit(0.0);
        chargingProfile.setPeriods(List.of(chargingSchedulePeriod));
        socket.setChargingProfiles(List.of(chargingProfile));
        chargingPoint.setSockets(List.of(socket));
        Optional<ChargingPoint> chargingPointOptional = Optional.of(chargingPoint);
        when(mockChargingPointService.findChargingPointByInternalId("id")).thenReturn(chargingPointOptional);

        when(mockEnergyConsumptionService.findMeanConsumption(eq("cpId"), any(LocalDateTime.class),
                any(LocalDateTime.class))).thenReturn(10.0);

        chargingProfileOptimizerUnderTest.onApplicationEvent(event);

        ArgumentCaptor<Socket> argumentCaptor = ArgumentCaptor.forClass(Socket.class);
        verify(mockSocketService).save(argumentCaptor.capture());
        Socket socketSaved = argumentCaptor.getValue();
        Assertions.assertEquals("cpId", socketSaved.getCpId());
        Assertions.assertEquals("FAST", socketSaved.getType());
        Assertions.assertEquals(1, socketSaved.getChargingProfiles().size());
        Assertions.assertEquals(10.0, socketSaved.getChargingProfiles().get(0).getPeriods().get(0).getLimit());
    }

    @Test
    void testOnApplicationEvent_multipleSockets() {
        EnergyChangeEvent event = new EnergyChangeEvent("source", "id");

        ChargingPoint chargingPoint = new ChargingPoint();
        chargingPoint.setId("id");
        chargingPoint.setCpId("cpId");
        Socket socket1 = new Socket();
        socket1.setSocketId(1);
        socket1.setCpId("cpId");
        socket1.setType("SLOW");
        Socket socket2 = new Socket();
        socket2.setSocketId(1);
        socket2.setCpId("cpId");
        socket2.setType("RAPID");
        ChargingProfile chargingProfile = new ChargingProfile();
        chargingProfile.setChargingProfileId(0);
        chargingProfile.setRecurrencyKind("DAILY");
        ChargingSchedulePeriod chargingSchedulePeriod = new ChargingSchedulePeriod();
        chargingSchedulePeriod.setStart(0);
        chargingSchedulePeriod.setLimit(0.0);
        chargingProfile.setPeriods(List.of(chargingSchedulePeriod));
        socket1.setChargingProfiles(List.of(chargingProfile));
        socket2.setChargingProfiles(List.of(chargingProfile));
        chargingPoint.setSockets(List.of(socket1, socket2));
        Optional<ChargingPoint> chargingPointOptional = Optional.of(chargingPoint);
        when(mockChargingPointService.findChargingPointByInternalId("id")).thenReturn(chargingPointOptional);

        when(mockEnergyConsumptionService.findMeanConsumption(eq("cpId"), any(LocalDateTime.class),
                any(LocalDateTime.class))).thenReturn(90.0);

        chargingProfileOptimizerUnderTest.onApplicationEvent(event);

        ArgumentCaptor<Socket> argumentCaptor = ArgumentCaptor.forClass(Socket.class);
        verify(mockSocketService, times(2)).save(argumentCaptor.capture());
        List<Socket> socketsSaved = argumentCaptor.getAllValues();
        Assertions.assertEquals("cpId", socketsSaved.get(0).getCpId());
        Assertions.assertEquals("SLOW", socketsSaved.get(0).getType());
        Assertions.assertEquals(1, socketsSaved.get(0).getChargingProfiles().size());
        Assertions.assertEquals(30.0, socketsSaved.get(0).getChargingProfiles().get(0).getPeriods().get(0).getLimit());
        Assertions.assertEquals("cpId", socketsSaved.get(1).getCpId());
        Assertions.assertEquals("RAPID", socketsSaved.get(1).getType());
        Assertions.assertEquals(1, socketsSaved.get(1).getChargingProfiles().size());
        Assertions.assertEquals(60.0, socketsSaved.get(1).getChargingProfiles().get(0).getPeriods().get(0).getLimit());
    }
}
