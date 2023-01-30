package softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model.Tariff;

@Repository
public interface TariffRepository extends MongoRepository<Tariff, String> {
    Tariff findTariffByTariffId(String tariffId);
}
