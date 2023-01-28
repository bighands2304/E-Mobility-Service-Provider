package softwareEngineering.ManoniSgaravattiFerretti.emspServer.CPMSUpdateReceiver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Service.TariffService;

@RestController
@RequestMapping("/ocpi/emsp/2.2/tariffs")
public class Tariffs {
    @Autowired
    TariffService tariffService;

    @GetMapping("/{tariff_id}")
    public ResponseEntity<?> getTariff(@PathVariable String tariff_id){
        //TODO return tariff json
        return ResponseEntity.ok("Tariff:");
    }

    @PutMapping ("/{tariff_id}")
    public ResponseEntity<?> putTariff(@PathVariable String tariff_id){
        //TODO return tariff json
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{tariff_id")
    public ResponseEntity<?> deleteTariff(@PathVariable String tariff_id){
        tariffService.delete(tariff_id);
        return ResponseEntity.noContent().build();
    }
}
