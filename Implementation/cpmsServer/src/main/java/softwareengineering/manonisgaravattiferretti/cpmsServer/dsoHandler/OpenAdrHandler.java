package softwareengineering.manonisgaravattiferretti.cpmsServer.dsoHandler;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.DSOOffer;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.DSOOfferService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.dsoHandler.openAdrDtos.PricingTimeSlotDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.dsoHandler.openAdrDtos.TimeOfUsePricingDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.energyManager.DSOManager;

import java.util.List;

@RestController
public class OpenAdrHandler {
    private final DSOOfferService dsoOfferService;
    private final DSOManager dsoManager;
    private final Logger logger = LoggerFactory.getLogger(OpenAdrHandler.class);

    public OpenAdrHandler(DSOOfferService dsoOfferService, DSOManager dsoManager) {
        this.dsoOfferService = dsoOfferService;
        this.dsoManager = dsoManager;
    }

    @PostMapping("/openAdr/tou_pricing_event")
    public ResponseEntity<?> handleTimeOfUsePricingEvent(@RequestBody @Valid TimeOfUsePricingDTO timeOfUsePricingDTO,
                                                         @RequestParam String token, @RequestParam String cpId) {
        List<DSOOffer> dsoOffers =  dsoOfferService.findDsoOffersFromCpAndDso(token, cpId);
        if (dsoOffers.size() == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "dso not registered for this cp");
        }
        logger.info("Received time of use: " + timeOfUsePricingDTO.toString());
        dsoManager.refactorOffers(cpId, token, dsoOffers, timeOfUsePricingDTO.getIntervals());
        return ResponseEntity.ok().build();
    }
}
