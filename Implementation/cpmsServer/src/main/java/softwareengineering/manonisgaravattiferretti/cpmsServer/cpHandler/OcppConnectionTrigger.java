package softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.ChargingPoint;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.ChargingPointService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.DSOOfferService;

import java.time.Duration;
import java.util.List;

@Service
public class OcppConnectionTrigger {
    private final ChargingPointService chargingPointService;
    private final DSOOfferService dsoOfferService;
    private final Logger logger = LoggerFactory.getLogger(OcppConnectionTrigger.class);
    @Value("${ocpp-tester.path}")
    String cpTestingPath;
    @Value("${enable-testing-enviroment}")
    boolean enableTesting;
    @Value("${delete-offers-startup}")
    boolean dropDsoAtStartup;

    @Autowired
    public OcppConnectionTrigger(ChargingPointService chargingPointService, DSOOfferService dsoOfferService) {
        this.chargingPointService = chargingPointService;
        this.dsoOfferService = dsoOfferService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void connectToAllCp() {
        if (!enableTesting) return;
        if (dropDsoAtStartup) {
            dsoOfferService.clear();
        }
        logger.info("trying to open websocket connections with the charging points");
        List<ChargingPoint> cps = chargingPointService.findAll();
        try {
            for (ChargingPoint chargingPoint : cps) {
                logger.info("trying to open websocket connections with cp with id = " + chargingPoint.getCpId());
                ResponseEntity<Void> response = triggerConnection(chargingPoint);
                if (response == null) {
                    logger.info("wasn't able to open the connections");
                }
            }
        } catch (WebClientRequestException e) {
            logger.info("the ocpp testing driver is not online, will not be able to send messages to charging points");
        }
    }

    public ResponseEntity<Void> triggerConnection(ChargingPoint chargingPoint) {
        return WebClient.create()
                .post()
                .uri(cpTestingPath + "/open_connection?token=" + chargingPoint.getAuthenticationKey() +
                        "&cp_id=" + chargingPoint.getCpId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(chargingPoint)
                .retrieve()
                .toBodilessEntity()
                .block(Duration.ofSeconds(10));
    }
}
