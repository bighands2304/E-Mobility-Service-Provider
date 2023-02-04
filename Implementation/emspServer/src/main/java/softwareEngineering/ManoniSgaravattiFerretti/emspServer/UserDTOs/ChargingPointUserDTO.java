package softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDTOs;

import lombok.Data;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model.ChargingPointOperator;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model.Socket;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model.Tariff;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ChargingPointUserDTO {
    private String id;
    private String cpId;
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    private LocalDateTime lastUpdate;
    private ChargingPointOperator cpo;
    private List<Socket> sockets;
    private List<Tariff> tariffs;
}
