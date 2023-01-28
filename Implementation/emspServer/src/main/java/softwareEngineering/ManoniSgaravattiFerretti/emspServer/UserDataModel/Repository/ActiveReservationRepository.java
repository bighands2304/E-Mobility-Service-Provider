package softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.ActiveReservation;

@Repository
public interface ActiveReservationRepository extends JpaRepository<ActiveReservation, Long> {
    ActiveReservation findActiveReservationBySessionId(Long sessionId);
}
