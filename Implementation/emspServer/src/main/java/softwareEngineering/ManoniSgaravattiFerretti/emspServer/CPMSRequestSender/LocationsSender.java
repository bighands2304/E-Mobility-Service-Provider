package softwareEngineering.ManoniSgaravattiFerretti.emspServer.CPMSRequestSender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model.ChargingPoint;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model.ChargingPointOperator;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model.Socket;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Service.ChargingPointService;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Service.SocketService;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Service.TariffService;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.OcpiDTOs.ChargingPointDTO;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.OcpiDTOs.SocketDTO;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.time.temporal.ChronoUnit.MINUTES;

@Service
public class LocationsSender {
    @Autowired
    ChargingPointService cpService;
    @Autowired
    SocketService socketService;
    @Autowired
    TariffService tariffService;

    private final String ocpiPath="/ocpi/cpo";
    private final RestTemplate restTemplate = new RestTemplate();



    @Async
    public void getCps(ChargingPointOperator cpo){
        try {
            LocalTime a= LocalTime.now();
            System.out.println("starting sleep: " + a);
            Thread.sleep(5000);
            LocalTime b= LocalTime.now();
            System.out.println("ending sleep: " + b);
            System.out.println("seconds: " + a.until(b, MINUTES));
        }catch (Exception e){
            System.out.println("Exception: " + e);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.set("Authorization", cpo.getTokenEmsp());
        HttpEntity<?> entity = new HttpEntity<>(headers);

        String urlTemplate = UriComponentsBuilder.fromHttpUrl(cpo.getCpmsUrl()+ocpiPath+"/locations").encode().toUriString();

        ParameterizedTypeReference<Page<ChargingPointDTO>> typo = new ParameterizedTypeReference<>() {};
        ResponseEntity<Page<ChargingPointDTO>> response = restTemplate.exchange(
                urlTemplate,
                HttpMethod.GET,
                entity,
                typo
        );


        List<ChargingPointDTO> cps= Objects.requireNonNull(response.getBody()).getContent();
        for (ChargingPointDTO cp: cps) {
            ChargingPoint newCp = cpService.getCPById(cp.getCpId());
            if(newCp==null){
                newCp = new ChargingPoint();
            }

            newCp.setCpId(cp.getCpId());
            newCp.setName(cp.getName());
            newCp.setAddress(cp.getName());
            newCp.setLatitude(cp.getLatitude());
            newCp.setLongitude(cp.getLongitude());
            newCp.setLastUpdate(cp.getLastUpdated());
            newCp.setCpo(cpo);

            newCp.setSockets(new ArrayList<>());
            for (SocketDTO s: cp.getSockets()) {
                Socket newSocket = socketService.getSocketById(s.getSocketId().toString());
                if (newSocket==null){
                    newSocket = new Socket();
                }

                newSocket.setSocketId(s.getSocketId().toString());
                newSocket.setAvailability(s.getAvailability());
                newSocket.setStatus(s.getStatus());
                newSocket.setType(s.getSocketType());
                newSocket.setLastUpdate(s.getLastUpdate());
                socketService.save(newSocket);
                newCp.addSocket(newSocket);
            }
            newCp.setTariffsId(cp.getTariffIds());
            for (String tid: cp.getTariffIds()) {
                newCp.addTariff(tariffService.getTariffById(tid));
            }
            cpService.save(newCp);
        }
    }

    public void getCp(ChargingPoint cp){
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.set("Authorization", cp.getCpo().getTokenEmsp());
        HttpEntity<?> entity = new HttpEntity<>(headers);

        //?date_from={DateTime}&date_to={DateTime}&offset=0&limit=10
        String urlTemplate = UriComponentsBuilder.fromHttpUrl(cp.getCpo().getCpmsUrl()+ocpiPath+"/locations/"+cp.getCpId()).encode().toUriString();

        ParameterizedTypeReference<Page<ChargingPointDTO>> typo = new ParameterizedTypeReference<>() {};
        ResponseEntity<Page<ChargingPointDTO>> response = restTemplate.exchange(
                urlTemplate,
                HttpMethod.GET,
                entity,
                typo
        );

        ChargingPointDTO cpResponse = Objects.requireNonNull(response.getBody()).getContent().get(0);

        ChargingPoint newCp = cpService.getCPById(cp.getCpId());
        if(newCp==null){
            newCp = new ChargingPoint();
        }

        newCp.setCpId(cpResponse.getCpId());
        newCp.setName(cpResponse.getName());
        newCp.setAddress(cpResponse.getName());
        newCp.setLatitude(cpResponse.getLatitude());
        newCp.setLongitude(cpResponse.getLongitude());
        newCp.setLastUpdate(cpResponse.getLastUpdated());
        newCp.setCpo(cp.getCpo());
        for (SocketDTO s: cpResponse.getSockets()) {
            Socket newSocket = socketService.getSocketById(s.getSocketId().toString());
            if (newSocket==null){
                newSocket = new Socket();
            }
            newSocket.setSocketId(s.getSocketId().toString());
            newSocket.setAvailability(s.getAvailability());
            newSocket.setStatus(s.getStatus());
            newSocket.setType(s.getSocketType());
            newSocket.setLastUpdate(s.getLastUpdate());
            socketService.save(newSocket);
            newCp.addSocket(newSocket);
        }
        newCp.setTariffsId(cpResponse.getTariffIds());
        for (String tid: cpResponse.getTariffIds()) {
            newCp.addTariff(tariffService.getTariffById(tid));
        }
        cpService.save(newCp);
    }

    public void getSocket(Socket socket, ChargingPoint cp){
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.set("Authorization", cp.getCpo().getTokenEmsp());
        HttpEntity<?> entity = new HttpEntity<>(headers);

        //?date_from={DateTime}&date_to={DateTime}&offset=0&limit=10
        String urlTemplate = UriComponentsBuilder.fromHttpUrl(cp.getCpo().getCpmsUrl()+ocpiPath+"/locations/"+ cp.getCpId() + "/" + socket.getSocketId()).encode().toUriString();

        ParameterizedTypeReference<Page<SocketDTO>> typo = new ParameterizedTypeReference<>() {};
        ResponseEntity<Page<SocketDTO>> response = restTemplate.exchange(
                urlTemplate,
                HttpMethod.GET,
                entity,
                typo
        );

        SocketDTO socketResponse = Objects.requireNonNull(response.getBody()).getContent().get(0);

        Socket newSocket = socketService.getSocketById(socketResponse.getSocketId().toString());
        if (newSocket==null){
            newSocket = new Socket();
        }
        newSocket.setSocketId(socketResponse.getSocketId().toString());
        newSocket.setAvailability(socketResponse.getAvailability());
        newSocket.setStatus(socketResponse.getStatus());
        newSocket.setType(socketResponse.getSocketType());
        newSocket.setLastUpdate(socketResponse.getLastUpdate());
        socketService.save(newSocket);
    }
}
