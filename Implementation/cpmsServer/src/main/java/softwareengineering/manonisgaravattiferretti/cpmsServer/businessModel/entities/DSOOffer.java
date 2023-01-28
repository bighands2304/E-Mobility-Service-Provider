package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities;

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
    private Double price;
    private Double capacity;
    private String chargingPointId; // external id (visible to both emsps and dsos)
    private String dsoId;
    private String companyName;
    private OfferTimeSlot usedTimeSlot;
    private OfferTimeSlot availableTimeSlot;
    private boolean isInUse;
    private LocalTime usedStartTime;
}
