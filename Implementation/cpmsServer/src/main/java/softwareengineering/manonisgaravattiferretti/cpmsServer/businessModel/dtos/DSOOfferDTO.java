package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.OfferTimeSlot;

@Data
public class DSOOfferDTO {
    private String offerId;
    private double price;
    private boolean valid;
    private double capacity;
    private String chargingPointId;
    private String chargingPointInternalId;
    private String dsoId;
    private String companyName;
    private OfferTimeSlot usedTimeSlot;
    private OfferTimeSlot availableTimeSlot;
    private boolean isInUse;
}
