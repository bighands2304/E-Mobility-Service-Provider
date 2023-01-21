package softwareengineering.manonisgaravattiferretti.cpmsServer.model.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("chargingPoints")
public class ChargingPoint {
    @Id
    private String id;
    private String cpId;
    private String name;
    private String address;
    private String latitude;
    private String longitude;
    private boolean togglePriceOptimizer;
    private boolean toggleEnergyMixOptimizer;
    private boolean toggleDSOSelectionOptimizer;

}
