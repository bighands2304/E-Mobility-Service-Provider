package softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.ActiveReservation;

public interface ActiveReservationRepository extends JpaRepository<ActiveReservation, Long> {
    ActiveReservation findActiveReservationBySessionId(Long sessionId);
}
