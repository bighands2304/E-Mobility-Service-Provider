package softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model;


import jakarta.persistence.GeneratedValue;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Document
public class ChargingPoint {
    @Id
    private String id;
    @Indexed(unique = true)
    private String cpId;
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    private LocalDateTime lastUpdate;
    @DocumentReference
    private ChargingPointOperator cpo;
    private List<Socket> sockets = new ArrayList<>();
    private List<String> tariffsId = new ArrayList<>();

    public void addSocket(Socket newSocket){ sockets.add(newSocket);}

    public void removeSocket(Socket removedSocket){ sockets.remove(removedSocket);}
}
