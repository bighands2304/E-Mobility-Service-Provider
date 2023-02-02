package softwareEngineering.ManoniSgaravattiFerretti.emspServer.CPMSUpdateReceiver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.OcpiDTOs.TariffDTO;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model.Tariff;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Service.TariffService;

@RestController
@RequestMapping("/ocpi/emsp/tariffs")
public class TariffsReceiver {
    @Autowired
    TariffService tariffService;

    @PutMapping ("/{tariff_id}")
    public ResponseEntity<?> putTariff(@PathVariable String tariff_id, @RequestBody TariffDTO tariff){
        Tariff newTariff = new Tariff();
        newTariff.setTariffId(tariff_id);
        newTariff.setPrice(tariff.getPrice());
        newTariff.setEndDate(tariff.getEndDate());
        newTariff.setSocketType(tariff.getSocketType());
        newTariff.setStartDate(tariff.getStartDate());
        newTariff.setStepSize(tariff.getStepSize());

        newTariff.setStartTime(tariff.getStartTime());
        newTariff.setEndTime(tariff.getEndTime());
        newTariff.setMinKWh(tariff.getMinKWh());
        newTariff.setMaxKWh(tariff.getMaxKWh());
        newTariff.setMinCurrent(tariff.getMinCurrent());
        newTariff.setMaxCurrent(tariff.getMaxCurrent());
        newTariff.setMinDuration(tariff.getMinDuration());
        newTariff.setMaxDuration(tariff.getMaxDuration());
        newTariff.setDaysOfTheWeek(tariff.getDaysOfTheWeek());

        tariffService.save(newTariff);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{tariff_id}")
    public ResponseEntity<?> deleteTariff(@PathVariable String tariff_id){
        tariffService.deleteById(tariff_id);
        return ResponseEntity.noContent().build();
    }
}
