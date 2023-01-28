package softwareengineering.manonisgaravattiferretti.cpmsServer.cpManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.ChargingPoint;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.ChargingPointService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PriceOptimizer {
    private final Map<String, Boolean> optimizerSet = new ConcurrentHashMap<>();
    private final ChargingPointService chargingPointService;

    @Autowired
    public PriceOptimizer(ChargingPointService chargingPointService) {
        this.chargingPointService = chargingPointService;
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
        // todo
    }

    @Scheduled(fixedRate = 600000000)
    public void fetchChargingPointsPeriodically() {
        List<ChargingPoint> chargingPoints = chargingPointService.findAll();
        for (ChargingPoint chargingPoint: chargingPoints) {
            optimizerSet.put(chargingPoint.getCpId(), chargingPoint.isTogglePriceOptimizer());
        }
    }

    public void switchOptimizer(String cpId, boolean toggle) {
        optimizerSet.put(cpId, toggle);
    }
}
