package softwareengineering.manonisgaravattiferretti.cpmsServer.model.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import softwareengineering.manonisgaravattiferretti.cpmsServer.model.entities.CPO;

public interface CPORepository extends MongoRepository<CPO, String> {
    CPO findCPOByCpoCode(String cpoCode);
}
