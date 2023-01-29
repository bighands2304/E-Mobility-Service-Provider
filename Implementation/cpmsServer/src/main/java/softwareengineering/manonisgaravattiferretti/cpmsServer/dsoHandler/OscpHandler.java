package softwareengineering.manonisgaravattiferretti.cpmsServer.dsoHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.DSOOfferService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.dsoHandler.dtos.ForecastedBlockDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.dsoHandler.dtos.GroupCapacityForecastDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.dsoHandler.dtos.RegistrationDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.dsoHandler.dtos.VersionUrlDTO;

import java.util.Map;
import java.util.OptionalDouble;
import java.util.UUID;

@RestController("/ocsp/fp")
public class OscpHandler {
    private final DSOOfferService dsoOfferService;
    @Value("${cpms.path}")
    private String cpmsUrl;

    @Autowired
    public OscpHandler(DSOOfferService dsoOfferService) {
        this.dsoOfferService = dsoOfferService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerDSO(@RequestBody RegistrationDTO registrationDTO) {
        String cpoToken = UUID.randomUUID().toString();
        dsoOfferService.registerDso(registrationDTO.getDsoId(), registrationDTO.getCpId(), registrationDTO.getToken(),
                registrationDTO.getVersionUrl().getUrl(), registrationDTO.getCompanyName(), cpoToken);
        Map<String, Object> body = Map.of("token", cpoToken, "versionUrl", new VersionUrlDTO("2.0", cpmsUrl));
        return ResponseEntity.ok(body);
    }

    @PostMapping("/update_group_capacity_forecast")
    public ResponseEntity<?> handleCapacityForecastUpdate(@RequestBody GroupCapacityForecastDTO groupCapacityForecastDTO,
                                                          @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        OptionalDouble capacityMean = groupCapacityForecastDTO.getForecastedBlocks().stream()
                .mapToDouble(ForecastedBlockDTO::getCapacity)
                .average();
        capacityMean.ifPresent(capacity -> dsoOfferService.updateCapacityByDsoCp(token, groupCapacityForecastDTO.getId(), capacity));
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
