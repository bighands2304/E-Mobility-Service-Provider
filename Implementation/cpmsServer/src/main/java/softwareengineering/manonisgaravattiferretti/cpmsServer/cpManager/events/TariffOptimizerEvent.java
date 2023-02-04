package softwareengineering.manonisgaravattiferretti.cpmsServer.cpManager.events;

import org.springframework.context.ApplicationEvent;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.AddTariffDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.Tariff;

public class TariffOptimizerEvent extends ApplicationEvent {
    private final String eventType;
    private final Tariff tariff;
    private final AddTariffDTO addTariffDTO;
    private final String cpId;

    public TariffOptimizerEvent(Object source, String eventType, Tariff tariff, AddTariffDTO addTariffDTO, String cpId) {
        super(source);
        this.eventType = eventType;
        this.tariff = tariff;
        this.addTariffDTO = addTariffDTO;
        this.cpId = cpId;
    }

    public String getEventType() {
        return eventType;
    }

    public Tariff getTariff() {
        return tariff;
    }

    public String getCpId() {
        return cpId;
    }

    public AddTariffDTO getAddTariffDTO() {
        return addTariffDTO;
    }
}
