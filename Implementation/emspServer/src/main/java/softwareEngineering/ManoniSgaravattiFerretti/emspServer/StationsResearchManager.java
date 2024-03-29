package softwareEngineering.ManoniSgaravattiFerretti.emspServer;

import org.apache.catalina.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.CPMSRequestSender.LocationsSender;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model.ChargingPoint;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model.Tariff;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Service.ChargingPointService;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Service.TariffService;
import org.springframework.beans.BeanUtils;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDTOs.ChargingPointUserDTO;


import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/user")
public class StationsResearchManager {
    private Mapper mapper;
    @Autowired
    ChargingPointService cpService;
    @Autowired
    LocationsSender locations;
    @Autowired
    TariffService tariffService;

    @GetMapping("/getCPsInRange/{latitude}/{longitude}/{range}")
    public ResponseEntity<?> getCpsInRange(@PathVariable Double latitude, @PathVariable Double longitude, @PathVariable Double range){
        //Search for CPs in a certain range
        List<ChargingPoint> cps= cpService.getCPsInRange(latitude, latitude+range, longitude, longitude+range);

        List<ChargingPointUserDTO> cpsDTO= new ArrayList<>();

        for (ChargingPoint cp: cps) {
            ChargingPointUserDTO cpDTO = new ChargingPointUserDTO();
            BeanUtils.copyProperties(cp, cpDTO);
            List<Tariff> tariffs = new ArrayList<>();
            for (String tariffId: cp.getTariffsId()) {
                tariffs.add(tariffService.getTariffById(tariffId));
            }
            cpDTO.setTariffs(tariffs);
            cpsDTO.add(cpDTO);
        }

        //Return the response with the list of CPs found
        return ResponseEntity.ok(cpsDTO);
    }

    @GetMapping("/getCP/{cpId}")
    public ResponseEntity<?> getCp(@PathVariable String cpId){
        //Collect the payload
        ChargingPoint cp = cpService.getCPById(cpId);

        //Fetch the status of the cp from its cpms
        locations.getCp(cp);

        ChargingPointUserDTO cpDTO = new ChargingPointUserDTO();
        BeanUtils.copyProperties(cp, cpDTO);
        List<Tariff> tariffs = new ArrayList<>();
        for (String tariffId: cp.getTariffsId()) {
            tariffs.add(tariffService.getTariffById(tariffId));
        }
        cpDTO.setTariffs(tariffs);

        //Return last information
        return ResponseEntity.ok(cpDTO);
    }

    @GetMapping("/getTariff/{tariffId}")
    public ResponseEntity<?> getTariff(@PathVariable String tariffId){
        //Return the tariff by its id
        return ResponseEntity.ok(tariffService.getTariffById(tariffId));
    }
 }
