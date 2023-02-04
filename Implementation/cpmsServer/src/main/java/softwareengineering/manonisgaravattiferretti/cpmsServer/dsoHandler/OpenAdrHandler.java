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
import softwareengineering.manonisgaravattiferretti.cpmsServer.dataWarehouse.entity.DimensionsPrimaryKey;
import softwareengineering.manonisgaravattiferretti.cpmsServer.dataWarehouse.entity.OfferPrice;
import softwareengineering.manonisgaravattiferretti.cpmsServer.dataWarehouse.service.OfferPriceService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.dsoHandler.openAdrDtos.PricingTimeSlotDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.dsoHandler.openAdrDtos.TimeOfUsePricingDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.energyManager.DSOManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.OptionalDouble;

@RestController
public class OpenAdrHandler {
    private final DSOOfferService dsoOfferService;
    private final DSOManager dsoManager;
    private final Logger logger = LoggerFactory.getLogger(OpenAdrHandler.class);
    private final OfferPriceService offerPriceService;

    public OpenAdrHandler(DSOOfferService dsoOfferService, DSOManager dsoManager, OfferPriceService offerPriceService) {
        this.dsoOfferService = dsoOfferService;
        this.dsoManager = dsoManager;
        this.offerPriceService = offerPriceService;
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
        OptionalDouble meanPriceOpt = timeOfUsePricingDTO.getIntervals().stream()
                .mapToDouble(PricingTimeSlotDTO::getPrice).average();
        meanPriceOpt.ifPresent(meanPrice -> {
            OfferPrice offerPrice = new OfferPrice();
            offerPrice.setPrice(meanPrice);
            DimensionsPrimaryKey dimensionsPrimaryKey = new DimensionsPrimaryKey(dsoOffers.get(0).getDsoId(),
                    cpId, LocalDateTime.now());
            offerPrice.setDimensionsPrimaryKey(dimensionsPrimaryKey);
            offerPriceService.insert(offerPrice);
        });
        return ResponseEntity.ok().build();
    }
}
