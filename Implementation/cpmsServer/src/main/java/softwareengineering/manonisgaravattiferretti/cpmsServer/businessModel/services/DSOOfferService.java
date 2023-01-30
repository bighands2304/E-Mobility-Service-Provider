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

    public List<DSOOffer> findOffersOfCp(String cpInternalId) {
        return dsoOfferRepository.findDSOOffersByChargingPointId(cpInternalId).stream().filter(DSOOffer::isValid).toList();
    }

    public Optional<DSOOffer> findOfferById(String id) {
        return dsoOfferRepository.findById(id);
    }

    public void updateOfferById(String id, Double price) {
        dsoOfferRepository.updateOfferFromId(id, price);
    }

    public void updateOfferByDsoCpTimeSlot(String dsoToken, String cpId, LocalTime startTime,
                                           LocalTime endTime, Double price) {
        dsoOfferRepository.updateOfferFromDsoTokenCpIdTimeSlot(dsoToken, cpId, startTime, endTime, price);
    }

    public void updateCapacityByDsoCpTimeSlot(String dsoToken, String cpId, LocalTime startTime,
                                              LocalTime endTime, Double capacity) {
        dsoOfferRepository.updateCapacityFromDsoTokenCpIdTimeSlot(dsoToken, cpId, startTime, endTime, capacity);
    }

    public void updateCapacityByDsoCp(String dsoToken, String cpId, Double capacity) {
        dsoOfferRepository.updateCapacityFromDsoTokenCpId(dsoToken, cpId, capacity);
    }

    public void insertOffer(DSOOffer dsoOffer) {
        dsoOfferRepository.save(dsoOffer);
    }

    public Optional<DSOOffer> findDSOOfferFromCpAndTimeSlot(String cpId, OfferTimeSlot offerTimeSlot, boolean inUse) {
        return dsoOfferRepository.findDSOOfferByChargingPointInternalIdAndAvailableTimeSlotAndInUse(cpId, offerTimeSlot, inUse);
    }

    public void registerDso(String dsoId, String cpId, String dsoToken, String dsoUrl, String companyName, String cpoToken) {
        DSOOffer dsoOffer = new DSOOffer();
        dsoOffer.setDsoId(dsoId);
        dsoOffer.setDsoUrl(dsoUrl);
        dsoOffer.setDsoToken(dsoToken);
        dsoOffer.setCpoToken(cpoToken);
        dsoOffer.setCompanyName(companyName);
        dsoOffer.setChargingPointId(cpId);
        dsoOffer.setValid(false);
        dsoOffer.setAvailableTimeSlot(new OfferTimeSlot(LocalTime.MIN, LocalTime.MAX));
        insertOffer(dsoOffer);
    }
}
