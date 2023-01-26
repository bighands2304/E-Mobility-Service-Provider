package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.ChargingPoint;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.repositories.ChargingPointRepository;

import java.time.LocalDateTime;
import java.util.List;
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
}
