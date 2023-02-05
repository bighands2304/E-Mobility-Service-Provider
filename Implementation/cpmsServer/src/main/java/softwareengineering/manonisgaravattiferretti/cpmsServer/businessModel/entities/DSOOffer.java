package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "dsoOffers")
public class DSOOffer {
    @Id
    private String id;
    private double price;
    @JsonIgnore
    private boolean valid;
    private double capacity;
    @Indexed
    private String chargingPointId; // external id (visible to both emsps and dsos)
    @Indexed
    private String chargingPointInternalId;
    private String dsoId;
    private String companyName;
    @JsonIgnore
    private String dsoToken;
    @JsonIgnore
    private String cpoToken;
    @JsonIgnore
    private String dsoUrl;
    private OfferTimeSlot usedTimeSlot;
    private OfferTimeSlot availableTimeSlot;
    private boolean inUse;
}
