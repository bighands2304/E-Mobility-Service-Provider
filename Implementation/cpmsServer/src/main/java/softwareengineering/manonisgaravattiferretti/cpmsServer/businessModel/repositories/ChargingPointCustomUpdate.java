package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.repositories;

import org.springframework.stereotype.Repository;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.IncludeBatteryDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.Tariff;

@Repository
public interface ChargingPointCustomUpdate {
    void updateBatteryEnergyFlow(IncludeBatteryDTO includeBatteryDTO, String cpId, Integer batteryId);
    void addTariff(String id, Tariff tariff);
    void updateBatteryAvailability(String id, int batteryId, Boolean available);
    void updateToggleOptimizer(String id, String optimizerType, boolean isAutomatic);
}
