package softwareengineering.manonisgaravattiferretti.cpmsServer.emspUpdateSender;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.EmspChargingPointDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.EmspChargingPointDTOWithId;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.EmspSocketDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.EmspDetails;

import java.time.Duration;

@Component
public class OcpiLocationsSender {

    // In all of these methods the upload to the emsp is done two times since the information is very important

    @Async
    public void putChargingPoint(EmspDetails emspDetails, String cpId, EmspChargingPointDTO emspChargingPointDTO, boolean retry) {
        try {
            ResponseEntity<Void> response = OcpiHeaderBuilder
                    .buildHeader(emspDetails, HttpMethod.PUT, "locations/" + cpId)
                    .bodyValue(emspChargingPointDTO)
                    .retrieve()
                    .toBodilessEntity()
                    .block(Duration.ofSeconds(10));
        } catch (RuntimeException e) {
            // if the timeout of the mono is reached
            if (retry) {
                putChargingPoint(emspDetails, cpId, emspChargingPointDTO, false);
            }
        }
    }

    @Async
    public void patchChargingPoint(EmspDetails emspDetails, EmspChargingPointDTOWithId emspChargingPointDTOWithId, boolean retry) {
        try {
            ResponseEntity<Void> response = OcpiHeaderBuilder
                    .buildHeader(emspDetails, HttpMethod.PATCH, "locations")
                    .bodyValue(emspChargingPointDTOWithId)
                    .retrieve()
                    .toBodilessEntity()
                    .block(Duration.ofSeconds(10));
        } catch (RuntimeException e) {
            // if the timeout of the mono is reached
            if (retry) {
                patchChargingPoint(emspDetails, emspChargingPointDTOWithId, false);
            }
        }
    }

    //todo: put socket (if needed)

    @Async
    public void patchSocket(EmspDetails emspDetails, EmspSocketDTO socketDTO, String cpId, boolean retry) {
        try {
            ResponseEntity<Void> response = OcpiHeaderBuilder
                    .buildHeader(emspDetails, HttpMethod.PATCH, "locations/" + cpId + "/" + socketDTO.getSocketId())
                    .bodyValue(socketDTO)
                    .retrieve()
                    .toBodilessEntity()
                    .block(Duration.ofSeconds(10));
        } catch (RuntimeException e) {
            // if the timeout of the mono is reached
            if (retry) {
                patchSocket(emspDetails, socketDTO, cpId, false);
            }
        }
    }
}
