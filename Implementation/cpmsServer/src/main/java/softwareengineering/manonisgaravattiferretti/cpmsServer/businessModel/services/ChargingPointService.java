package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public Page<ChargingPoint> getChargingPointsOfCpo(String cpoCode, int offset, int limit) {
        Pageable pageable = PageRequest.of(offset, limit, Sort.by("name"));
        return chargingPointRepository.findChargingPointsByCpoCode(cpoCode, pageable);
    }

    public Optional<ChargingPoint> findChargingPointByInternalId(String id, String cpoCode) {
        Optional<ChargingPoint> chargingPointOptional = chargingPointRepository.findById(id);
        if (chargingPointOptional.isEmpty() ||
                !chargingPointOptional.get().getCpoCode().equals(cpoCode)) {
            return Optional.empty();
        }
        return chargingPointOptional;
    }

    public Optional<ChargingPoint> findChargingPointOfCpoById(String id, String cpoCode) {
        return chargingPointRepository.findChargingPointByCpIdAndCpoCode(id, cpoCode);
    }

    public Optional<ChargingPoint> findChargingPointById(String id) {
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

    public void updateIncludeBattery(IncludeBatteryDTO includeBatteryDTO, String cpId, Integer batteryId) {
        Optional<ChargingPoint> chargingPoint = chargingPointRepository.findById(cpId);
        if (chargingPoint.isPresent()) {
            for (Battery battery: chargingPoint.get().getBatteries()) {
                if (battery.getBatteryId().equals(batteryId)
                        && battery.getStatus().equals("UNAVAILABLE")) {
                    chargingPointRepository.updateBatteryAvailability(cpId, batteryId, true);
                }
            }
            chargingPointRepository.updateBatteryEnergyFlow(includeBatteryDTO, cpId, batteryId);
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

    public void removeCpById(String id) {
        chargingPointRepository.deleteById(id);
    }

    public List<ChargingPoint> findAll() {
        return chargingPointRepository.findAll();
    }

    // Todo: change dso provider, add cp
}
