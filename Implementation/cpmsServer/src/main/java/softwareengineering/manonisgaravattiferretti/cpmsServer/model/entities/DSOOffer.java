package softwareengineering.manonisgaravattiferretti.cpmsServer.model.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "dsoOffers")
public class DSOOffer {
    @Id
    private Long offerId;
    private Double price;
    private Double capacity;
    private String chargingPointId;
    private DSO dso;
}
