package softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model.ChargingPointOperator;

@Repository
public interface ChargingPointOperatorRepository extends MongoRepository<ChargingPointOperator, String> {
    ChargingPointOperator findChargingPointOperatorByCpoId(String cpoId);
    ChargingPointOperator findChargingPointOperatorByToken(String token);
}
