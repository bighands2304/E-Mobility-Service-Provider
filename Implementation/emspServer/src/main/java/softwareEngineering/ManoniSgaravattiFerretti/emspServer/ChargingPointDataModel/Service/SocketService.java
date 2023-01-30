package softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Service;

import org.springframework.stereotype.Service;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model.Socket;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Repository.SocketRepository;

@Service
public class SocketService {
    SocketRepository socketRepository;
    public Socket getSocketById(String socketId){ return socketRepository.findSocketBySocketId(socketId);}
    public void save(Socket socket){ socketRepository.save(socket);}
}
