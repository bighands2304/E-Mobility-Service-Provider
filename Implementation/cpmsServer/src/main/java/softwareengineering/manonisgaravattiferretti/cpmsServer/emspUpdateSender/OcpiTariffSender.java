package softwareengineering.manonisgaravattiferretti.cpmsServer.emspUpdateSender;

import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.AddTariffDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.EmspDetails;

import java.time.Duration;

@Component
public class OcpiTariffSender {
    @Async
    public void putTariff(AddTariffDTO tariff, String tariffId, EmspDetails emspDetails) {
        OcpiHeaderBuilder.buildHeader(emspDetails,
                HttpMethod.PUT, "tariffs/" + tariffId)
                .bodyValue(tariff)
                .retrieve()
                .toBodilessEntity()
                .blockOptional(Duration.ofSeconds(10));
    }

    @Async
    public void deleteTariff(String tariffId, EmspDetails emspDetails) {
        OcpiHeaderBuilder.buildHeader(emspDetails,
                        HttpMethod.DELETE, "tariffs/" + tariffId)
                .retrieve()
                .toBodilessEntity()
                .blockOptional(Duration.ofSeconds(10));
    }
}
