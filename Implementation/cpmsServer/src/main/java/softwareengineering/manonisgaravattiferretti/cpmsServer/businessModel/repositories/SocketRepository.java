package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.Socket;

import java.util.List;
import java.util.Optional;

@Repository
public interface SocketRepository extends MongoRepository<Socket, String> {
    Optional<Socket> findSocketBySocketId(Integer socketId);
    List<Socket> findSocketsByCpId(Integer cpId);
}
