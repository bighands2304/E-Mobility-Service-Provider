package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.DSOOffer;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.OfferTimeSlot;

import java.util.List;
import java.util.Optional;

@Repository
public interface DSOOfferRepository extends MongoRepository<DSOOffer, String>, DSOOfferCustomUpdate {
    List<DSOOffer> findDSOOffersByChargingPointInternalId(String chargingPointId);
    Optional<DSOOffer> findDSOOfferByChargingPointIdAndAvailableTimeSlotAndInUse(String cpId,
                                                                                 OfferTimeSlot availableTimeSlot,
                                                                                 boolean inUse);
    Optional<DSOOffer> findDSOOfferByChargingPointIdAndInUse(String cpId, boolean inUse);
    List<DSOOffer> findDSOOffersByChargingPointIdAndDsoToken(String cpId, String dsoToken);
    List<DSOOffer> findDSOOffersByChargingPointInternalIdAndInUse(String cpId, boolean inUse);
}
