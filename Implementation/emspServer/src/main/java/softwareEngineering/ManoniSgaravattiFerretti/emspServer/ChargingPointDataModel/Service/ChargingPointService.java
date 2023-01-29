package softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model.ChargingPoint;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model.Socket;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Repository.ChargingPointRepository;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Repository.SocketRepository;

import java.util.Collection;
import java.util.List;

@Service
public class ChargingPointService {
    @Autowired
    ChargingPointRepository chargingPointRepository;
    @Autowired
    SocketRepository socketRepository;

    public List<ChargingPoint> getAllCPs(){
        return chargingPointRepository.findAll();
    }
    public List<ChargingPoint> getCPsInRange(Double latitude_start, Double latitude_end, Double longitude_start, Double longitude_end){return chargingPointRepository.findAllByLatitudeBetweenAndLongitudeBetween(latitude_start, latitude_end, longitude_start, longitude_end);}
    public void save(ChargingPoint cp) {chargingPointRepository.save(cp);}
    public ChargingPoint getCPById(String cpId){ return  chargingPointRepository.findChargingPointByCpId(cpId);}

}
