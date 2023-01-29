package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.Socket;

import java.util.List;
import java.util.Optional;

@Repository
public interface SocketRepository extends MongoRepository<Socket, String>, SocketCustomUpdate {
    Optional<Socket> findSocketByCpIdAndSocketId(String cpId, Integer socketId);
    Optional<Socket> findSocketByInternalCpIdAndSocketId(String cpId, Integer socketId);
    List<Socket> findSocketsByCpId(String cpId);
    List<Socket> findSocketsByInternalCpId(String cpInternalId);
}
