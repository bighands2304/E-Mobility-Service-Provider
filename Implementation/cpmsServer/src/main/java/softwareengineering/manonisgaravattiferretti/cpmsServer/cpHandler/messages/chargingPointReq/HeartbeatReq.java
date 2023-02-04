package softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.messages.chargingPointReq;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HeartbeatReq {
    private LocalDateTime timestamp;
    private String cpId;
}
