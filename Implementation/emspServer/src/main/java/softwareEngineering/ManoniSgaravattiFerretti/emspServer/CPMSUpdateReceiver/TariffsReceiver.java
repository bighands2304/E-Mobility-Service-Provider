package softwareEngineering.ManoniSgaravattiFerretti.emspServer.CPMSUpdateReceiver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.OcpiDTOs.TariffDTO;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model.SpecialOffer;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model.Tariff;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Service.ChargingPointService;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Service.TariffService;

@RestController
@RequestMapping("/ocpi/emsp/tariffs")
public class TariffsReceiver {
    @Autowired
    TariffService tariffService;
    @Autowired
    ChargingPointService cpService;

    @PutMapping ("/{tariff_id}")
    public ResponseEntity<?> putTariff(@PathVariable String tariff_id, @RequestBody TariffDTO tariff, @RequestHeader("Authorization") String auth){
        Tariff newTariff = new Tariff();
        newTariff.setTariffId(tariff_id);
        newTariff.setPrice(tariff.getPrice());
        newTariff.setEndDate(tariff.getEndDate());
        newTariff.setSocketType(tariff.getSocketType());
        newTariff.setStartDate(tariff.getStartDate());
        newTariff.setStepSize(tariff.getStepSize());

        if(tariff.isSpecialOffer()){
            SpecialOffer newSpecialOffer = (SpecialOffer) newTariff;
            newSpecialOffer.setStartTime(tariff.getStartTime());
            newSpecialOffer.setEndTime(tariff.getEndTime());
            newSpecialOffer.setMinKWh(tariff.getMinKWh());
            newSpecialOffer.setMaxKWh(tariff.getMaxKWh());
            newSpecialOffer.setMinCurrent(tariff.getMinCurrent());
            newSpecialOffer.setMaxCurrent(tariff.getMaxCurrent());
            newSpecialOffer.setMinDuration(tariff.getMinDuration());
            newSpecialOffer.setMaxDuration(tariff.getMaxDuration());
            newSpecialOffer.setDaysOfTheWeek(tariff.getDaysOfTheWeek());

            tariffService.save(newSpecialOffer);
        }else {
            tariffService.save(newTariff);
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{tariff_id}")
    public ResponseEntity<?> deleteTariff(@PathVariable String tariff_id){
        tariffService.deleteById(tariff_id);
        return ResponseEntity.noContent().build();
    }
}
