package softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model.Socket;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Repository.SocketRepository;

@Service
public class SocketService {
    @Autowired
    SocketRepository socketRepository;
    public Socket getSocketById(String socketId){ return socketRepository.findSocketBySocketId(socketId);}
}
