package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class EmspChargingPointDTO {
    private String cpId;
    private String name;
    private String address;
    private Double longitude;
    private Double latitude;
    private LocalDateTime lastUpdated;
    private List<String> tariffIds;
    private List<EmspSocketDTO> sockets;
}
