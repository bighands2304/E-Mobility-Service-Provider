package softwareEngineering.ManoniSgaravattiFerretti.emspServer.CPMSRequestSender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model.ChargingPoint;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Service.ChargingPointService;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.OcpiDTOs.SessionDTO;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.Reservation;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Service.ReservationService;

@Service
public class CommandsSender {
    @Autowired
    ReservationService reservationService;
    @Autowired
    ChargingPointService cpService;
    private final String ocpiPath="/ocpi/cpo";
    private final RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<String> reserveNow(ChargingPoint cp, Reservation reservation) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.set("Authorization", cp.getCpo().getTokenEmsp());

        //setting the session parameters
        SessionDTO session = new SessionDTO();
        session.setReservationId(reservation.getId());
        session.setChargingPointId(cp.getCpId());
        session.setSocketId(reservation.getSocketId());
        session.setExpiryDate(reservation.getExpiryDate());

        HttpEntity<?> entity = new HttpEntity<>(session, headers);

        String urlTemplate = UriComponentsBuilder.fromHttpUrl(cp.getCpo().getCpmsUrl() + ocpiPath + "/commands/RESERVE_NOW").encode().toUriString();

        return restTemplate.exchange(
                urlTemplate,
                HttpMethod.POST,
                entity,
                String.class
        );

    }

    public ResponseEntity<String> startSession(Reservation reservation) {
        ChargingPoint cp= cpService.getCPById(reservation.getCpId());

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.set("Authorization", cp.getCpo().getTokenEmsp());

        SessionDTO session = new SessionDTO();
        session.setReservationId(reservation.getId());
        session.setChargingPointId(cp.getCpId());
        session.setSocketId(reservation.getSocketId());

        HttpEntity<?> entity = new HttpEntity<>(session, headers);

        String urlTemplate = UriComponentsBuilder.fromHttpUrl(cp.getCpo().getCpmsUrl() + ocpiPath + "/commands/START_SESSION").encode().toUriString();

        return restTemplate.exchange(
                urlTemplate,
                HttpMethod.POST,
                entity,
                String.class
        );
    }

    public ResponseEntity<String> stopSession(Reservation reservation) {
        ChargingPoint cp= cpService.getCPById(reservation.getCpId());

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.set("Authorization", cp.getCpo().getTokenEmsp());

        HttpEntity<?> entity = new HttpEntity<>(headers);

        String urlTemplate = UriComponentsBuilder.fromHttpUrl(cp.getCpo().getCpmsUrl() + ocpiPath + "/commands/STOP_SESSION/"+reservation.getSessionId()).encode().toUriString();

        return restTemplate.exchange(
                urlTemplate,
                HttpMethod.POST,
                entity,
                String.class
        );
    }

    public ResponseEntity<String> cancelReservation(Reservation reservation) {
        ChargingPoint cp= cpService.getCPById(reservation.getCpId());

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.set("Authorization", cp.getCpo().getTokenEmsp());

        HttpEntity<?> entity = new HttpEntity<>(headers);

        String urlTemplate = UriComponentsBuilder.fromHttpUrl(cp.getCpo().getCpmsUrl() + ocpiPath + "/commands/CANCEL_RESERVATION/"+reservation.getId()).encode().toUriString();

        return restTemplate.exchange(
                urlTemplate,
                HttpMethod.POST,
                entity,
                String.class
        );
    }
}
