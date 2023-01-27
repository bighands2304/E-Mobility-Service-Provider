package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.IncludeBatteryDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.Battery;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.ChargingPoint;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.Tariff;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.repositories.ChargingPointRepository;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ChargingPointService {
    private final ChargingPointRepository chargingPointRepository;

    @Autowired
    public ChargingPointService(ChargingPointRepository chargingPointRepository) {
        this.chargingPointRepository = chargingPointRepository;
    }

    public void addChargingPoint(ChargingPoint chargingPoint) {
        chargingPointRepository.insert(chargingPoint);
    }

    public List<ChargingPoint> getChargingPointsOfCpo(String cpoCode) {
        return chargingPointRepository.findChargingPointsByCpoCode(cpoCode);
    }

    public Optional<ChargingPoint> findChargingPointByInternalId(String id, String cpoCode) {
        Optional<ChargingPoint> chargingPointOptional = chargingPointRepository.findById(id);
        if (chargingPointOptional.isEmpty() ||
                !chargingPointOptional.get().getCpoCode().equals(cpoCode)) {
            return Optional.empty();
        }
        return chargingPointOptional;
    }

    public Optional<ChargingPoint> findChargingPointOfCpoById(Integer id, String cpoCode) {
        return chargingPointRepository.findChargingPointByCpIdAndCpoCode(id, cpoCode);
    }

    public Optional<ChargingPoint> findChargingPointById(Integer id) {
        return chargingPointRepository.findChargingPointByCpId(id);
    }

    public Page<ChargingPoint> findAllPaginated(int offset, int limit) {
        Pageable pageable = PageRequest.of(offset, limit);
        return chargingPointRepository.findAll(pageable);
    }

    public Page<ChargingPoint> findAllLastUpdatePaginated(LocalDateTime dateFrom, LocalDateTime dateTo, int offset, int limit) {
        Pageable pageable = PageRequest.of(offset, limit);
        return chargingPointRepository.findAllByLastUpdatedBetween(dateFrom, dateTo, pageable);
    }

    public void deleteChargingPoint(String id) {
        chargingPointRepository.deleteById(id);
    }

    public void updateIncludeBattery(IncludeBatteryDTO includeBatteryDTO) {
        Optional<ChargingPoint> chargingPoint = chargingPointRepository.findById(includeBatteryDTO.getCpId());
        if (chargingPoint.isPresent()) {
            for (Battery battery: chargingPoint.get().getBatteries()) {
                if (battery.getBatteryId().equals(includeBatteryDTO.getBatteryId())
                        && battery.getStatus().equals("UNAVAILABLE")) {
                    chargingPointRepository.updateBatteryAvailability(includeBatteryDTO.getCpId(),
                            includeBatteryDTO.getBatteryId(), true);
                }
            }
            chargingPointRepository.updateBatteryEnergyFlow(includeBatteryDTO);
        }
    }

    public void updateBatteryAvailability(String id, int batteryId, boolean available) {
        chargingPointRepository.updateBatteryAvailability(id, batteryId, available);
    }

    public void addTariff(String id, Tariff tariff) {
        chargingPointRepository.addTariff(id, tariff);
    }

    public void updateToggleOptimizer(String id, String optimizerType, boolean isAutomatic) {
        chargingPointRepository.updateToggleOptimizer(id, optimizerType, isAutomatic);
    }

    public Optional<ChargingPoint> findChargingPointByAuthKey(String authKey) {
        return chargingPointRepository.findChargingPointByAuthenticationKey(authKey);
    }

    // Todo: change dso provider, add cp
}
