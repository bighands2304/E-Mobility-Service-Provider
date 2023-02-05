package softwareengineering.manonisgaravattiferretti.cpmsServer.dsoHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.ChargingPoint;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.ChargingPointService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.DSOOfferService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.dsoHandler.oscpDtos.ForecastedBlockDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.dsoHandler.oscpDtos.GroupCapacityForecastDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.dsoHandler.oscpDtos.RegistrationDTO;

import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.UUID;

@RestController
public class OscpHandler {
    private final DSOOfferService dsoOfferService;
    private final Logger logger = LoggerFactory.getLogger(OscpHandler.class);
    private final ChargingPointService chargingPointService;
    @Value("${cpms.path}")
    private String cpmsUrl;

    @Autowired
    public OscpHandler(DSOOfferService dsoOfferService, ChargingPointService chargingPointService) {
        this.dsoOfferService = dsoOfferService;
        this.chargingPointService = chargingPointService;
    }

    @PostMapping("/oscp/fp/register")
    public ResponseEntity<Map<String, Object>> registerDSO(@RequestBody RegistrationDTO registrationDTO) {
        logger.info("Oscp registration received from dso of company " + registrationDTO.getCompanyName());
        String cpoToken = UUID.randomUUID().toString();
        Optional<ChargingPoint> chargingPointOptional = chargingPointService.findChargingPointByExternalId(registrationDTO.getCpId());
        if (chargingPointOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "charging point not found");
        }
        logger.info("Registered dso");
        dsoOfferService.registerDso(registrationDTO.getDsoId(), registrationDTO.getCpId(), registrationDTO.getToken(),
                registrationDTO.getUrl(), registrationDTO.getCompanyName(), cpoToken, chargingPointOptional.get().getId());
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
        return ResponseEntity.ok().build();
    }
}
