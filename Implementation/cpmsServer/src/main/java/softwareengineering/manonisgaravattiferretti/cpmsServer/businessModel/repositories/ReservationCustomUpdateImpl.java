package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.Reservation;

import java.time.LocalDateTime;

public class ReservationCustomUpdateImpl implements ReservationCustomUpdate {
    private final MongoOperations mongoOperations;

    @Autowired
    public ReservationCustomUpdateImpl(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public void updateReservationStatus(String status, Long reservationInternalId, LocalDateTime timestamp) {
        Query query = new Query();
        query.addCriteria(Criteria.where("internalReservationId").is(reservationInternalId));
        Update update = new Update();
        update.set("status", status);
        update.set("lastUpdated", timestamp);
        mongoOperations.updateFirst(query, update, Reservation.class);
    }

    @Override
    public void updateReservationEnergyAmount(Double energyAmount, Long sessionId, LocalDateTime timestamp) {
        Query query = new Query();
        query.addCriteria(Criteria.where("sessionId").is(sessionId));
        Update update = new Update();
        update.set("energyAmount", energyAmount);
        update.set("lastUpdated", timestamp);
        mongoOperations.updateFirst(query, update, Reservation.class);
    }
}
