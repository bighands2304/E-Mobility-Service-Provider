package softwareEngineering.ManoniSgaravattiFerretti.emspServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model.ChargingPoint;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Service.ChargingPointService;

import java.util.List;

@RestController
@RequestMapping("/OCPIReceiver")
public class CPMSUpdateReceiver {
    @Autowired
    ChargingPointService chargingPointService;

    @GetMapping("/getCPs")
    public List<ChargingPoint> getCPs(){
        return chargingPointService.getAllCPs();
    }

}
