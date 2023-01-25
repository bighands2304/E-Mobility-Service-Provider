package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.ChargingPoint;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.repositories.ChargingPointRepository;

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

    public Optional<ChargingPoint> findChargingPointById(String id, String cpoCode) {
        return chargingPointRepository.findChargingPointByCpIdAndCpoCode(id, cpoCode);
    }
}
