package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Data
@Document(collection = "chargingPoints")
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
    private List<Tariff> tariffs;
    @DocumentReference
    private List<Socket> sockets;
    private List<Battery> batteries;
    //TODO: check if it is ok to leave just the code
    // (since the performance is better like this, because no lookups are needed)
    //@DocumentReference(lazy = true)
    //private CPO cpo;
    @JsonIgnore
    private String cpoCode;
}
