package softwareengineering.manonisgaravattiferretti.cpmsServer.emspUpdateSender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.CommandResultType;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.EmspDetails;

import java.time.Duration;

@Component
public class OcpiCommandSender {
    private final Logger logger = LoggerFactory.getLogger(OcpiCommandSender.class);

    @Async
    public void sendCommandResult(EmspDetails emspDetails, Long emspReservationId,
                                  CommandResultType commandResultType, String commandType) {
        logger.info("Sending " + commandType + " to " + emspDetails.getUrl() + " with id = " + emspReservationId);
        String path = "commands/" + commandType + "/" + emspReservationId;
        WebClient.RequestBodySpec requestBodySpec = OcpiHeaderBuilder
                .buildHeader(emspDetails, HttpMethod.POST, path);
        ResponseEntity<Void> response = requestBodySpec
                .bodyValue(commandResultType)
                .retrieve()
                .toBodilessEntity()
                .block(Duration.ofSeconds(10));
        if (response != null) {
            logger.info("Received response of command " + commandType + " with id = " + emspReservationId +
                    ". Status code: " + response.getStatusCode());
        }
    }
}
