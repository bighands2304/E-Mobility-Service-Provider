package softwareengineering.manonisgaravattiferretti.cpmsServer.model.entities;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "sockets")
public class Socket {
    private int socketId;
    private int cpId; //not the object id, but the id visible to the emsp
    private String type;
    private String status;
    private String availability;
    private LocalDateTime lastUpdated;
    private List<ChargingProfile> chargingProfiles;
}
