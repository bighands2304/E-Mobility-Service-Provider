package softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model.Tariff;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Repository.TariffRepository;

@Service
public class TariffService {
    @Autowired
    TariffRepository tariffRepository;

    public Tariff getTariffById(String tariffId){return tariffRepository.findTariffByTariffId(tariffId);}
    public void delete(String tariffId){
        tariffRepository.delete(getTariffById(tariffId));
    }
}
