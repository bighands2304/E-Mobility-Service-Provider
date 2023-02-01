package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos;

import lombok.Data;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.Battery;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.Tariff;

import java.util.List;

@Data
public class AddChargingPointDTO {
    private String cpId;
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    private List<Tariff> tariffs;
    private List<AddChargingPointSocketDTO> cpSockets;
    private List<Battery> batteries;
    private String authenticationKey;
    private String connectionUrl;
}
