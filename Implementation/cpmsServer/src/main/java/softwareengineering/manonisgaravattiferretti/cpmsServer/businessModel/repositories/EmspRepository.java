package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.EmspDetails;

import java.util.Optional;

@Repository
public interface EmspRepository extends MongoRepository<EmspDetails, String>, EmspCustomUpdateRepository {
    Optional<EmspDetails> findByEmspToken(String emspToken);
    Optional<EmspDetails> findFirstByEmspTokenAndUrl(String emspToken, String url);
}
