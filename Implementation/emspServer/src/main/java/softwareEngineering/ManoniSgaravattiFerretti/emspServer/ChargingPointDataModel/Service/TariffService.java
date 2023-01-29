package softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Service;

import org.springframework.stereotype.Service;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model.Tariff;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Repository.TariffRepository;

@Service
public class TariffService {
    TariffRepository tariffRepository;

    public Tariff getTariffById(String tariffId){return tariffRepository.findTariffByTariffId(tariffId);}
    public void deleteById(String tariffId){
        tariffRepository.delete(getTariffById(tariffId));
    }
    public Tariff save(Tariff tariff) {return tariffRepository.save(tariff);}
}
