package softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model;


import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "ChargingPoint")
public class ChargingPoint {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String cpId;
    private String name;
    private String address;
    private String latitude;
    private String longitude;
    private ChargingPointOperator cpo;
    private List<Socket> sockets = new ArrayList<>();
    private List<Tariff> tariffs = new ArrayList<>();

    public void addTariff(Tariff newTariff){
        tariffs.add(newTariff);
    }
}
