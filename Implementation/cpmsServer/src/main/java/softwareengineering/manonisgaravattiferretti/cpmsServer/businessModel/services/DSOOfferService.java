package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.repositories.DSOOfferRepository;

@Service
public class DSOOfferService {
    private final DSOOfferRepository dsoOfferRepository;

    @Autowired
    public DSOOfferService(DSOOfferRepository dsoOfferRepository) {
        this.dsoOfferRepository = dsoOfferRepository;
    }
}
