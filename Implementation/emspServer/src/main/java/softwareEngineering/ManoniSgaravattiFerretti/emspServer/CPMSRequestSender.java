package softwareEngineering.ManoniSgaravattiFerretti.emspServer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model.ChargingPoint;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Service.ChargingPointOperatorService;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Service.ChargingPointService;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.ActiveReservation;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.Reservation;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RestController
@RequestMapping("/ocpi/emsp/2.2")
public class CPMSRequestSender {
    @Autowired
    ChargingPointOperatorService cpoService;
    @Autowired
    ChargingPointService cpService;
    RestTemplate restTemplate = new RestTemplate();
    ObjectMapper objectMapper = new ObjectMapper();
    HttpHeaders headers = new HttpHeaders();
    @Value("${emsp.path}")
    private String emspPath;

    private String ocpiPath="/ocpi/cpo";

    @PostMapping("/credentialsSender")
    public void registerToCPO(@RequestBody Map<String,String> payload){
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("url", emspPath);

        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        Map map = new HashMap<>();
        try {
            map = objectMapper.readValue(restTemplate.postForObject(payload.get("cpmsUrl"), request, String.class), Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println(map);
    }

    public Reservation reserve(Reservation reservation){
        ChargingPoint cp = cpService.getCPBySocketId(reservation.getSocketId());

        String cpmsUrl = cp.getCpo().getCpmsUrl() + ocpiPath + "/commands/RESERVE_NOW";

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("reservationId", reservation.getId());
        requestBody.put("chargingPointId", cp.getCpId());
        requestBody.put("socketId", reservation.getSocketId());
        requestBody.put("expiryDate", LocalDateTime.now().plusMinutes(20));

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", cp.getCpo().getToken());

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        Map map = new HashMap<>();
        try {
            map = objectMapper.readValue(restTemplate.postForObject(cpmsUrl, request, String.class), Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        reservation.setStartTime(LocalDateTime.now());

        return reservation;
    }

    public void startSession(ActiveReservation reservation){
        ChargingPoint cp = cpService.getCPBySocketId(reservation.getSocketId());

        String cpmsUrl = cp.getCpo().getCpmsUrl() + ocpiPath + "/commands/START_SESSION";

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("reservationId", reservation.getId());
        requestBody.put("chargingPointId", cp.getCpId());
        requestBody.put("socketId", reservation.getSocketId());

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", cp.getCpo().getToken());

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        Map map = new HashMap<>();
        try {
            map = objectMapper.readValue(restTemplate.postForObject(cpmsUrl, request, String.class), Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

/*
//GET

    RestTemplate restTemplate = new RestTemplate();

    String response = restTemplate.getForObject("https://www.example.com", String.class);
    ObjectMapper objectMapper = new ObjectMapper();
    Map<String, Object> map = objectMapper.readValue(response, Map.class);
    System.out.println(map);

//POST

    RestTemplate restTemplate = new RestTemplate();

    Map<String, Object> requestBody = new HashMap<>();
    requestBody.put("key1", "value1");
    requestBody.put("key2", "value2");

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

    String response = restTemplate.postForObject("https://www.example.com", request, String.class);
    ObjectMapper objectMapper = new ObjectMapper();
    Map<String, Object> map = objectMapper.readValue(response, Map.class);
    System.out.println(map);

 */
