package softwareengineering.manonisgaravattiferretti.cpmsServer.cpManager.listeners;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.AddTariffDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.cpManager.PriceManager;
import softwareengineering.manonisgaravattiferretti.cpmsServer.cpManager.events.TariffOptimizerEvent;

@Component
public class TariffOptimizerEventListener implements ApplicationListener<TariffOptimizerEvent> {
    private final PriceManager priceManager;

    @Autowired
    public TariffOptimizerEventListener(PriceManager priceManager) {
        this.priceManager = priceManager;
    }

    @Override
    public void onApplicationEvent(TariffOptimizerEvent event) {
        switch (event.getEventType()) {
            case "R" -> priceManager.removeTariff(event.getCpId(), event.getTariff().getTariffId());
            case "M" -> {
                AddTariffDTO addTariffDTO = new AddTariffDTO();
                BeanUtils.copyProperties(event.getTariff(), addTariffDTO);
                priceManager.putTariff(addTariffDTO, event.getCpId(), event.getTariff().getTariffId());
            }
            case "A" -> priceManager.addTariff(event.getAddTariffDTO(), event.getCpId());
        }
    }
}
