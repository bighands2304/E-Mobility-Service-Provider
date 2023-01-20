package softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.CPMSUpdateReceiver;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model.ChargingPoint;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Repository.ChargingPointRepository;

import java.util.List;

@Service
public class ChargingPointService {
    @Autowired
    ChargingPointRepository chargingPointRepository;

    public List<ChargingPoint> getAllCPs(){
        return chargingPointRepository.findAll();
    }


}
