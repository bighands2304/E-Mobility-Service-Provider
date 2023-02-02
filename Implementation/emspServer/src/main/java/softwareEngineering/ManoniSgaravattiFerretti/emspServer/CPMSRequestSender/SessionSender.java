package softwareEngineering.ManoniSgaravattiFerretti.emspServer.CPMSRequestSender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model.ChargingPointOperator;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.OcpiDTOs.RestResponsePage;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.OcpiDTOs.SessionDTO;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.ActiveReservation;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.DeletedReservation;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.EndedReservation;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.Reservation;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Service.ReservationService;

import java.util.List;
import java.util.Objects;

@Service
public class SessionSender {
    @Autowired
    ReservationService reservationService;
    private final String ocpiPath="/ocpi/cpo";
    private final RestTemplate restTemplate = new RestTemplate();

    public void getSessions(ChargingPointOperator cpo) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.set("Authorization", cpo.getTokenEmsp());
        HttpEntity<?> entity = new HttpEntity<>(headers);

        String urlTemplate = UriComponentsBuilder.fromHttpUrl(cpo.getCpmsUrl() + ocpiPath + "/sessions").encode().toUriString();

        ParameterizedTypeReference<RestResponsePage<SessionDTO>> typo = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<RestResponsePage<SessionDTO>> response = restTemplate.exchange(
                urlTemplate,
                HttpMethod.GET,
                entity,
                typo
        );

        List<SessionDTO> sessions= Objects.requireNonNull(response.getBody()).getContent();

        for (SessionDTO s: sessions) {
            Reservation newSession = reservationService.getReservationById(s.getReservationId());
            newSession.setStartTime(s.getStartDateTime());
            newSession.setId(s.getReservationId());
            newSession.setSocketId(s.getSocketId());
            switch (s.getStatus()) {
                case "ACTIVE", "RESERVATION" -> {
                    ActiveReservation newActiveSession = (ActiveReservation) newSession;
                    newActiveSession.setSessionId(Long.parseLong(s.getSessionId()));
                    reservationService.save(newActiveSession);
                }
                case "COMPLETED" -> {
                    EndedReservation newEndedReservation = (EndedReservation) newSession;
                    newEndedReservation.setEndTime(s.getEndDateTime());
                    newEndedReservation.setPrice(s.getTotalCost());
                    newEndedReservation.setEnergyAmount(s.getKwh());
                    reservationService.save(newEndedReservation);
                }
                case "INVALID" -> {
                    DeletedReservation newDeletedReservation = (DeletedReservation) newSession;
                    newDeletedReservation.setDeletionTime(s.getEndDateTime());
                    reservationService.save(newDeletedReservation);
                }
            }
        }
    }

    public void getSession(ChargingPointOperator cpo, ActiveReservation reservation) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.set("Authorization", cpo.getTokenEmsp());
        HttpEntity<?> entity = new HttpEntity<>(headers);

        String urlTemplate = UriComponentsBuilder.fromHttpUrl(cpo.getCpmsUrl() + ocpiPath + "/sessions/" + reservation.getSessionId()).encode().toUriString();

        ParameterizedTypeReference<RestResponsePage<SessionDTO>> typo = new ParameterizedTypeReference<>() {};
        ResponseEntity<RestResponsePage<SessionDTO>> response = restTemplate.exchange(
                urlTemplate,
                HttpMethod.GET,
                entity,
                typo
        );

        SessionDTO session= Objects.requireNonNull(response.getBody()).getContent().get(0);

        Reservation newSession = reservationService.getReservationById(session.getReservationId());
        newSession.setStartTime(session.getStartDateTime());
        newSession.setId(session.getReservationId());
        newSession.setSocketId(session.getSocketId());
        switch (session.getStatus()) {
            case "ACTIVE", "RESERVATION" -> {
                ActiveReservation newActiveSession = (ActiveReservation) newSession;
                newActiveSession.setSessionId(Long.parseLong(session.getSessionId()));
                reservationService.save(newActiveSession);
            }
            case "COMPLETED" -> {
                EndedReservation newEndedReservation = (EndedReservation) newSession;
                newEndedReservation.setEndTime(session.getEndDateTime());
                newEndedReservation.setPrice(session.getTotalCost());
                newEndedReservation.setEnergyAmount(session.getKwh());
                reservationService.save(newEndedReservation);
            }
            case "INVALID" -> {
                DeletedReservation newDeletedReservation = (DeletedReservation) newSession;
                newDeletedReservation.setDeletionTime(session.getEndDateTime());
                reservationService.save(newDeletedReservation);
            }
        }
    }
}