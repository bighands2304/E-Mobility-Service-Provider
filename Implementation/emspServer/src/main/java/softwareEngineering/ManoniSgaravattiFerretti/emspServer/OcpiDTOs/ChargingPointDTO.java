package softwareEngineering.ManoniSgaravattiFerretti.emspServer.OcpiDTOs;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ChargingPointDTO {
    private String cpId;
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    private LocalDateTime lastUpdated;
    private List<String> tariffIds;
    private List<SocketDTO> sockets;
}
