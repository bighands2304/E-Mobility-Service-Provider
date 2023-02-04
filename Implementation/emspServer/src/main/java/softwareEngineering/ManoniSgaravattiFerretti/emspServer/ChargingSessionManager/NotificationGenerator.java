package softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingSessionManager;

import lombok.Data;

import java.util.Map;

@Data
public class NotificationGenerator {
    private Long to;
    private String title;
    private String body;
    private Map<String,String> data;
}
