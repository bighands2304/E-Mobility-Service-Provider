package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalTime;
import java.util.Timer;

@Data
@Document(collection = "dsoOffers")
public class DSOOffer {
    @Id
    private String offerId;
    private double price;
    @JsonIgnore
    private boolean valid;
    private double capacity;
    private String chargingPointId; // external id (visible to both emsps and dsos)
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
    private boolean isInUse;
}
