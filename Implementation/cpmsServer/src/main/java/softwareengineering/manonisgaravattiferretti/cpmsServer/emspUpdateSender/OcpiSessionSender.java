package softwareengineering.manonisgaravattiferretti.cpmsServer.emspUpdateSender;

import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.ChargingSessionDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.EmspDetails;

import java.time.Duration;

@Component
public class OcpiSessionSender {
    @Async
    public void putSession(ChargingSessionDTO chargingSessionDTO, EmspDetails emspDetails) {
        WebClient.RequestBodySpec requestBodySpec = OcpiHeaderBuilder.buildHeader(emspDetails,
                HttpMethod.PUT, "sessions/" + chargingSessionDTO.getSessionId());
        sendSession(requestBodySpec, chargingSessionDTO);
    }

    @Async
    public void patchSession(ChargingSessionDTO chargingSessionDTO, EmspDetails emspDetails) {
        WebClient.RequestBodySpec requestBodySpec = OcpiHeaderBuilder.buildHeader(emspDetails,
                HttpMethod.PATCH, "sessions/" + chargingSessionDTO.getSessionId());
        sendSession(requestBodySpec, chargingSessionDTO);
    }

    private void sendSession(WebClient.RequestBodySpec requestBodySpec,
                                                       ChargingSessionDTO chargingSessionDTO) {
        requestBodySpec.bodyValue(chargingSessionDTO).retrieve()
                .toBodilessEntity()
                .blockOptional(Duration.ofSeconds(10));
    }
}
