package softwareengineering.manonisgaravattiferretti.cpmsServer.cpManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.AddTariffDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.Tariff;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.ChargingPointService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.utils.EntityFromDTOConverter;
import softwareengineering.manonisgaravattiferretti.cpmsServer.cpManager.events.TogglePriceOptimizerEvent;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PriceManager implements ApplicationListener<TogglePriceOptimizerEvent> {
    private final ChargingPointService chargingPointService;
    private final PriceOptimizer priceOptimizer;

    @Autowired
    public PriceManager(ChargingPointService chargingPointService, PriceOptimizer priceOptimizer) {
        this.chargingPointService = chargingPointService;
        this.priceOptimizer = priceOptimizer;
    }


    @Override
    public void onApplicationEvent(TogglePriceOptimizerEvent event) {
        chargingPointService.updateToggleOptimizer(event.getCpId(), "Price", event.isAutomatic());
        priceOptimizer.switchOptimizer(event.getCpId(), event.isAutomatic());
    }

    public Tariff addTariff(AddTariffDTO addTariffDTO, String cpId) {
        priceOptimizer.switchOptimizer(cpId, false);
        String id = UUID.randomUUID().toString();
        Tariff tariff = (addTariffDTO.getIsSpecialOffer()) ?
                EntityFromDTOConverter.fromAddTariffDTOToSpecialOffer(addTariffDTO, id) :
                EntityFromDTOConverter.fromAddTariffDTOToTariff(addTariffDTO, id);
        tariff.setLastUpdated(LocalDateTime.now());
        chargingPointService.addTariff(cpId, tariff);
        //todo: send put to emsp (both cp and tariff)
        return tariff;
    }

    public Tariff putTariff(AddTariffDTO addTariffDTO, String cpId, String tariffId) {
        priceOptimizer.switchOptimizer(cpId, false);
        Tariff tariff = (addTariffDTO.getIsSpecialOffer()) ?
                EntityFromDTOConverter.fromAddTariffDTOToSpecialOffer(addTariffDTO, tariffId) :
                EntityFromDTOConverter.fromAddTariffDTOToTariff(addTariffDTO, tariffId);
        tariff.setLastUpdated(LocalDateTime.now());
        chargingPointService.removeTariff(cpId, tariffId);
        chargingPointService.addTariff(cpId, tariff);
        // todo: send patch to emsp
        return tariff;
    }

    public void removeTariff(String cpId, String tariffId) {
        priceOptimizer.switchOptimizer(cpId, false);
        chargingPointService.removeTariff(cpId, tariffId);
        // todo: send delete to emsp
    }
}
