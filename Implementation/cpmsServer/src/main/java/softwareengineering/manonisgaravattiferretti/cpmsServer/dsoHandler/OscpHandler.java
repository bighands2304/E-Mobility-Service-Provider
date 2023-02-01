package softwareengineering.manonisgaravattiferretti.cpmsServer.dsoHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.DSOOfferService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.dsoHandler.dtos.ForecastedBlockDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.dsoHandler.dtos.GroupCapacityForecastDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.dsoHandler.dtos.RegistrationDTO;

import java.util.Map;
import java.util.OptionalDouble;
import java.util.UUID;

@RestController
public class OscpHandler {
    private final DSOOfferService dsoOfferService;
    private final Logger logger = LoggerFactory.getLogger(OscpHandler.class);
    @Value("${cpms.path}")
    private String cpmsUrl;

    @Autowired
    public OscpHandler(DSOOfferService dsoOfferService) {
        logger.info("Created oscp handler");
        this.dsoOfferService = dsoOfferService;
    }

    @PostMapping("/oscp/fp/register")
    public ResponseEntity<Map<String, Object>> registerDSO(@RequestBody RegistrationDTO registrationDTO) {
        logger.info("Oscp registration received from dso of company " + registrationDTO.getCompanyName());
        String cpoToken = UUID.randomUUID().toString();
        
        dsoOfferService.registerDso(registrationDTO.getDsoId(), registrationDTO.getCpId(), registrationDTO.getToken(),
                registrationDTO.getUrl(), registrationDTO.getCompanyName(), cpoToken);
        Map<String, Object> body = Map.of("token", cpoToken, "url", cpmsUrl);
        return ResponseEntity.ok(body);
    }

    @PostMapping("/oscp/fp/update_group_capacity_forecast")
    public ResponseEntity<?> handleCapacityForecastUpdate(@RequestBody GroupCapacityForecastDTO groupCapacityForecastDTO,
                                                          @RequestParam String token) {
        OptionalDouble capacityMean = groupCapacityForecastDTO.getForecastedBlocks().stream()
                .mapToDouble(ForecastedBlockDTO::getCapacity)
                .average();
        capacityMean.ifPresent(capacity -> dsoOfferService.updateCapacityByDsoCp(token, groupCapacityForecastDTO.getId(), capacity));
        logger.info("Received a capacity forecast");
        // todo: update cassandra when done
        return ResponseEntity.ok().build();
    }

    public void sendAdjustCapacityForecast() {

    }

    public void sendGroupCapacityComplianceError() {

    }

    public void sendUpdateGroupMeasurements() {

    }
}
