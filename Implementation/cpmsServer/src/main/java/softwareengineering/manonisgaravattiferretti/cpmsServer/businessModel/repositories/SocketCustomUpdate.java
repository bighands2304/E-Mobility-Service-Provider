package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.repositories;

import org.springframework.stereotype.Repository;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.ChangeSocketAvailabilityDTO;

@Repository
public interface SocketCustomUpdate {
    void updateSocketAvailability(ChangeSocketAvailabilityDTO changeSocketAvailabilityDTO);
    void updateSocketStatus(String cpInternalId, Integer socketId, String newStatus);
}
