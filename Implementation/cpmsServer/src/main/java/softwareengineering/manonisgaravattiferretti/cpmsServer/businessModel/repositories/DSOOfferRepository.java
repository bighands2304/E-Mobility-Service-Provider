package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.DSOOffer;

import java.util.List;

@Repository
public interface DSOOfferRepository extends MongoRepository<DSOOffer, String>, DSOOfferCustomUpdate {
    List<DSOOffer> findDSOOffersByChargingPointId(String chargingPointId);
}
