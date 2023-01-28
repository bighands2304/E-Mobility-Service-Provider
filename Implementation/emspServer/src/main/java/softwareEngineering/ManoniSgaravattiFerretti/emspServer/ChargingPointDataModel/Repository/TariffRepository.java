package softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model.Tariff;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model.TariffId;

@Repository
public interface TariffRepository extends MongoRepository<Tariff, TariffId> {
    Tariff findTariffByTariffId(String tariffId);
}
