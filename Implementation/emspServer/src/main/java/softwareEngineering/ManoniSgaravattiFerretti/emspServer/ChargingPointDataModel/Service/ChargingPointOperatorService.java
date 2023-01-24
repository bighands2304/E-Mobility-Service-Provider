package softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model.ChargingPointOperator;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Repository.ChargingPointOperatorRepository;

@Service
public class ChargingPointOperatorService {
    @Autowired
    ChargingPointOperatorRepository cpoRepository;

    public ChargingPointOperator saveCPO(ChargingPointOperator cpo){return cpoRepository.save(cpo);}

    public ChargingPointOperator getCPOById(String cpoId){return cpoRepository.findChargingPointOperatorByCpoId(cpoId);}
}
