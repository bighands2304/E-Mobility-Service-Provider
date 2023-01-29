package softwareEngineering.ManoniSgaravattiFerretti.emspServer.CPMSRequestSender;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model.ChargingPoint;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.OcpiDTOs.SessionDTO;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.ActiveReservation;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.Reservation;

@Service
public class CommandsSender {
    private final String ocpiPath="/ocpi/cpo";
    private final RestTemplate restTemplate = new RestTemplate();

    public void reserveNow(ChargingPoint cp, Reservation reservation) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.set("Authorization", cp.getCpo().getTokenEmsp());

        SessionDTO session = new SessionDTO();
        session.setReservationId(reservation.getId());
        session.setChargingPointId(cp.getCpId());
        session.setSocketId(reservation.getSocketId());
        session.setExpiryDate(reservation.getStartTime().plusMinutes(20));

        HttpEntity<?> entity = new HttpEntity<>(session, headers);

        String urlTemplate = UriComponentsBuilder.fromHttpUrl(cp.getCpo().getCpmsUrl() + ocpiPath + "/commands/RESERVE_NOW").encode().toUriString();

        restTemplate.exchange(
                urlTemplate,
                HttpMethod.POST,
                entity,
                String.class
        );
    }

    public void startSession(ActiveReservation reservation, ChargingPoint cp) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.set("Authorization", cp.getCpo().getTokenEmsp());

        SessionDTO session = new SessionDTO();
        session.setReservationId(reservation.getId());
        session.setChargingPointId(cp.getCpId());
        session.setSocketId(reservation.getSocketId());

        HttpEntity<?> entity = new HttpEntity<>(session, headers);

        String urlTemplate = UriComponentsBuilder.fromHttpUrl(cp.getCpo().getCpmsUrl() + ocpiPath + "/commands/START_SESSION").encode().toUriString();

        restTemplate.exchange(
                urlTemplate,
                HttpMethod.POST,
                entity,
                String.class
        );
    }
}
