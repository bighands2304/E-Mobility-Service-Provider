package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.DSOOffer;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.OfferTimeSlot;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.repositories.DSOOfferRepository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class DSOOfferService {
    private final DSOOfferRepository dsoOfferRepository;

    @Autowired
    public DSOOfferService(DSOOfferRepository dsoOfferRepository) {
        this.dsoOfferRepository = dsoOfferRepository;
    }

    public List<DSOOffer> findOffersOfCp(String cpId) {
        return dsoOfferRepository.findDSOOffersByChargingPointId(cpId);
    }

    public Optional<DSOOffer> findOfferById(String id) {
        return dsoOfferRepository.findById(id);
    }

    public void updateOfferById(String id, Double price) {
        dsoOfferRepository.updateOfferFromId(id, price);
    }

    public void updateOfferByDsoCpTimeSlot(String dsoId, String cpId, LocalTime startTime,
                                           LocalTime endTime, Double price) {
        dsoOfferRepository.updateOfferFromDsoIdCpIdTimeSlot(dsoId, cpId, startTime, endTime, price);
    }

    public void updateCapacityByDsoCpTimeSlot(String dsoId, String cpId, LocalTime startTime,
                                           LocalTime endTime, Double capacity) {
        dsoOfferRepository.updateCapacityFromDsoIdCpIdTimeSlot(dsoId, cpId, startTime, endTime, capacity);
    }

    public void insertOffer(DSOOffer dsoOffer) {
        dsoOfferRepository.save(dsoOffer);
    }

    public Optional<DSOOffer> findDSOOfferFromCpAndTimeSlot(String cpId, OfferTimeSlot offerTimeSlot, boolean inUse) {
        return dsoOfferRepository.findDSOOfferByChargingPointIdAndAvailableTimeSlotAndInUse(cpId, offerTimeSlot, inUse);
    }
}
