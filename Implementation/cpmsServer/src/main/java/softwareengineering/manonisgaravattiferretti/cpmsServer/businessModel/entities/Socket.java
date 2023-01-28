package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "sockets")
public class Socket {
    @Id
    private int id;
    private Integer socketId; // the id visible to the emsp
    @JsonIgnore
    private String internalCpId;
    @JsonIgnore
    private String cpId; //not the object id, but the id visible to the emsp
    @JsonIgnore
    private String cpoCode;
    private String type;
    private String status;
    private String availability;
    private LocalDateTime lastUpdated;
    private List<ChargingProfile> chargingProfiles;
}
