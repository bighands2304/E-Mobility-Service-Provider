package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.CPO;

@Repository
public interface CPORepository extends MongoRepository<CPO, String> {
    CPO findCPOByCpoCode(String cpoCode);
}
