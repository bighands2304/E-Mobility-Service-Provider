package softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model;


import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.cglib.core.Local;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Document
public class ChargingPoint {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    @Indexed(unique = true)
    private String cpId;
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    private LocalDateTime lastUpdate;
    private ChargingPointOperator cpo;
    private List<Socket> sockets = new ArrayList<>();
    private List<Tariff> tariffs = new ArrayList<>();
    private List<String> tariffsId = new ArrayList<>();

    public void addTariff(Tariff newTariff){
        tariffs.add(newTariff);
    }

    public void addSocket(Socket newSocket){ sockets.add(newSocket);}
}
