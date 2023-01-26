package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.repositories;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.ChangeSocketAvailabilityDTO;

public class SocketCustomUpdateImpl implements SocketCustomUpdate {
    @Override
    public void updateSocketAvailability(ChangeSocketAvailabilityDTO changeSocketAvailabilityDTO) {
        Query query = new Query();
        query.addCriteria(Criteria.where("cpId").is(changeSocketAvailabilityDTO.getCpId()));
        query.addCriteria(Criteria.where("socketId").is(changeSocketAvailabilityDTO.getSocketId()));
        Update update = new Update();
        update.set("availability", (changeSocketAvailabilityDTO.getAvailable()) ? "AVAILABLE" : "NOT_AVAILABLE");
    }
}
