package softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.ChargingPoint;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.ChargingPointService;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionsManager {
    private final Map<String, String> chargingPointsSessionIds = new ConcurrentHashMap<>();
    private final Map<String, String> sessionIdsToCp = new ConcurrentHashMap<>();
    private final ChargingPointService chargingPointService;
    private final Logger logger = LoggerFactory.getLogger(SessionsManager.class);

    @Autowired
    public SessionsManager(ChargingPointService chargingPointService) {
        this.chargingPointService = chargingPointService;
    }

    public boolean authenticateChargingPoint(String token, String sessionId) {
        logger.info("trying to register cp with token = " + token);
        if (token == null) {
            return false;
        }
        Optional<ChargingPoint> chargingPointOptional = chargingPointService.findChargingPointByAuthKey(token);
        if (chargingPointOptional.isPresent()) {
            chargingPointsSessionIds.put(chargingPointOptional.get().getCpId(), sessionId);
            sessionIdsToCp.put(sessionId, chargingPointOptional.get().getCpId());
            logger.info("token is valid: cp = {}, sessionId = {}", chargingPointOptional.get().getCpId(), sessionId);
            return true;
        }
        return false;
    }

    public void updateSessionId(String cpId, String sessionId) {
        sessionIdsToCp.put(sessionId, cpId);
        chargingPointsSessionIds.put(cpId, sessionId);
    }

    public String getChargingPointFromSession(String sessionId) {
        return sessionIdsToCp.get(sessionId);
    }

    public String getSessionIdFromChargingPointId(String cpId) {
        return chargingPointsSessionIds.get(cpId);
    }
}
