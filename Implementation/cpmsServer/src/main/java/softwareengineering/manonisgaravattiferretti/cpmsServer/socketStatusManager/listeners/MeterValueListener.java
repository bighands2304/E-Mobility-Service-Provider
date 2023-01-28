package softwareengineering.manonisgaravattiferretti.cpmsServer.socketStatusManager.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import softwareengineering.manonisgaravattiferretti.cpmsServer.EMSPUpdateSender;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.ReservationService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.socketStatusManager.events.MeterValueEvent;

import java.time.LocalDateTime;

@Component
public class MeterValueListener implements ApplicationListener<MeterValueEvent> {
    private final EMSPUpdateSender emspUpdateSender;
    private final ReservationService reservationService;

    @Autowired
    public MeterValueListener(EMSPUpdateSender emspUpdateSender, ReservationService reservationService) {
        this.emspUpdateSender = emspUpdateSender;
        this.reservationService = reservationService;
    }

    @Override
    public void onApplicationEvent(MeterValueEvent event) {
        Double energyConsumed = event.getMeterValues().stream()
                .map(meterValue -> Double.parseDouble(meterValue.getSampledValue()))
                .reduce(0.0, Double::sum);
        reservationService.updateSessionEnergyConsumption(energyConsumed, event.getSessionId(), LocalDateTime.now());
        // todo: update emsp
    }
}
