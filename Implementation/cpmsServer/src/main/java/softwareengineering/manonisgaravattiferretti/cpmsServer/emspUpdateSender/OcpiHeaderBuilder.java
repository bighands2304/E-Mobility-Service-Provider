package softwareengineering.manonisgaravattiferretti.cpmsServer.emspUpdateSender;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.EmspDetails;

public class OcpiHeaderBuilder {

    public static WebClient.RequestBodySpec buildHeader(EmspDetails emspDetails, HttpMethod method, String endPoint) {
        return WebClient.create()
                .method(method)
                .uri(emspDetails.getUrl() + "/ocpi/emsp/" + endPoint)
                .header("Authorization", emspDetails.getEmspToken())
                .contentType(MediaType.APPLICATION_JSON);
    }
}
