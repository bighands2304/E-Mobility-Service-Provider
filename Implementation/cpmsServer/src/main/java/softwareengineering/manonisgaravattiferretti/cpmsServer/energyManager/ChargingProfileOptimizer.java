package softwareengineering.manonisgaravattiferretti.cpmsServer.energyManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.ChargingPoint;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.ChargingProfile;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.ChargingSchedulePeriod;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.Socket;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.ChargingPointService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.SocketService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.dataWarehouse.service.EnergyConsumptionService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.energyManager.events.EnergyChangeEvent;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class ChargingProfileOptimizer implements ApplicationListener<EnergyChangeEvent> {
    private final ChargingPointService chargingPointService;
    private final SocketService socketService;
    private final EnergyConsumptionService energyConsumptionService;
    private final Logger logger = LoggerFactory.getLogger(ChargingProfileOptimizer.class);
    private static final Map<String, Double> SOCKET_TYPES = Map.of("FAST", 4.0, "RAPID", 2.0, "SLOW", 1.0);

    @Autowired
    public ChargingProfileOptimizer(ChargingPointService chargingPointService, SocketService socketService,
                                    EnergyConsumptionService energyConsumptionService) {
        this.chargingPointService = chargingPointService;
        this.socketService = socketService;
        this.energyConsumptionService = energyConsumptionService;
    }

    @Override
    public void onApplicationEvent(EnergyChangeEvent event) {
        logger.info("Optimizing charging profiles of cp " + event.getCpId());
        String cpId = event.getCpId();
        Optional<ChargingPoint> chargingPoint = chargingPointService.findChargingPointByInternalId(cpId);
        if (chargingPoint.isEmpty()) return;
        List<Socket> sockets = chargingPoint.get().getSockets();
        LocalDateTime validFrom = LocalDateTime.now();
        LocalDateTime validTo = validFrom.plus(1, ChronoUnit.DAYS);
        double divideFactor = sockets.stream().mapToDouble(socket -> SOCKET_TYPES.get(socket.getType())).sum();
        double meanConsumption = energyConsumptionService.findMeanConsumption(chargingPoint.get().getCpId(),
                validFrom.minus(1, ChronoUnit.WEEKS), validFrom);
        double limitSlow = meanConsumption / divideFactor;
        for (Socket socket: sockets) {
            ChargingProfile chargingProfile = new ChargingProfile();
            chargingProfile.setChargingProfileId(1);
            chargingProfile.setValidFrom(validFrom);
            chargingProfile.setValidTo(validTo);
            chargingProfile.setRecurrencyKind("DAILY");
            ChargingSchedulePeriod chargingSchedulePeriod = new ChargingSchedulePeriod();
            chargingSchedulePeriod.setLimit(limitSlow * SOCKET_TYPES.get(socket.getType()));
            chargingSchedulePeriod.setStart(0);
            chargingProfile.setPeriods(List.of(chargingSchedulePeriod));
            socket.setChargingProfiles(List.of(chargingProfile));
            socketService.save(socket);
        }
    }
}
