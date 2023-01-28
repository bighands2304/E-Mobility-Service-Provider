package softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model.Socket;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model.SocketId;

@Repository
public interface SocketRepository extends MongoRepository<Socket, SocketId> {
    Socket findSocketBySocketId(String socketId);
}
