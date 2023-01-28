package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.repositories.customRepositoriesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.ChangeSocketAvailabilityDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.Socket;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.repositories.SocketCustomUpdate;

public class SocketCustomUpdateImpl implements SocketCustomUpdate {
    private final MongoOperations mongoOperations;

    @Autowired
    public SocketCustomUpdateImpl(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public void updateSocketAvailability(ChangeSocketAvailabilityDTO changeSocketAvailabilityDTO) {
        Query query = new Query();
        query.addCriteria(Criteria.where("cpId").is(changeSocketAvailabilityDTO.getCpId()));
        query.addCriteria(Criteria.where("socketId").is(changeSocketAvailabilityDTO.getSocketId()));
        Update update = new Update();
        update.set("availability", (changeSocketAvailabilityDTO.getAvailable()) ? "AVAILABLE" : "NOT_AVAILABLE");
        mongoOperations.updateFirst(query, update, Socket.class);
    }

    @Override
    public void updateSocketStatus(String cpInternalId, Integer socketId, String newStatus) {
        Query query = new Query();
        query.addCriteria(Criteria.where("cpId").is(cpInternalId));
        query.addCriteria(Criteria.where("socketId").is(socketId));
        Update update = new Update();
        update.set("status", newStatus);
        mongoOperations.updateFirst(query, update, Socket.class);
    }
}
