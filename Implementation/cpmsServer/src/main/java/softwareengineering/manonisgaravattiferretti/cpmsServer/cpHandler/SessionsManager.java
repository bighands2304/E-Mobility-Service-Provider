package softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler;

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
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final Map<Integer, String> chargingPointsSessionIds = new ConcurrentHashMap<>();
    private final ChargingPointService chargingPointService;

    @Autowired
    public SessionsManager(ChargingPointService chargingPointService) {
        this.chargingPointService = chargingPointService;
    }

    public boolean authenticateChargingPoint(String token, String sessionId) {
        if (token == null) {
            return false;
        }
        Optional<ChargingPoint> chargingPointOptional = chargingPointService.findChargingPointByAuthKey(token);
        if (chargingPointOptional.isPresent()) {
            chargingPointsSessionIds.put(chargingPointOptional.get().getCpId(), sessionId);
            return true;
        }
        return false;
    }

    public void registerSession(String sessionId, WebSocketSession session) {
        sessions.put(sessionId, session);
    }

    public WebSocketSession getSessionFromChargingPointId(Integer cpId) {
        return sessions.get(chargingPointsSessionIds.get(cpId));
    }

    public WebSocketSession getSessionFromSessionId(String sessionId) {
        return sessions.get(sessionId);
    }

    public String getSessionIdFromChargingPointId(Integer cpId) {
        return chargingPointsSessionIds.get(cpId);
    }
}
