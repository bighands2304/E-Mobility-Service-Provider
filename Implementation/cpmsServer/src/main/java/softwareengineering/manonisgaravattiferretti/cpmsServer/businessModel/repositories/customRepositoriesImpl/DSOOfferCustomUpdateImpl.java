package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.repositories.customRepositoriesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.DSOOffer;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.repositories.DSOOfferCustomUpdate;

import java.time.LocalTime;

public class DSOOfferCustomUpdateImpl implements DSOOfferCustomUpdate {
    private final MongoOperations mongoOperations;

    @Autowired
    public DSOOfferCustomUpdateImpl(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public void updateOfferFromId(String id, Double price) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        Update update = new Update();
        update.set("price", price);
        mongoOperations.updateFirst(query, update, DSOOffer.class);
    }

    @Override
    public void updateOfferFromDsoIdCpIdTimeSlot(String dsoId, String cpId, LocalTime startTime,
                                                 LocalTime endTime, Double price) {
        updateFieldDouble(dsoId, cpId, startTime, endTime, "price", price);
    }

    @Override
    public void updateCapacityFromDsoIdCpIdTimeSlot(String dsoId, String cpId, LocalTime startTime,
                                                    LocalTime endTime, Double capacity) {
        updateFieldDouble(dsoId, cpId, startTime, endTime, "capacity", capacity);
    }

    private void updateFieldDouble(String dsoId, String cpId, LocalTime startTime,
                                   LocalTime endTime, String fieldName, Double value) {
        Query query = new Query();
        query.addCriteria(Criteria.where("dsoId").is(dsoId));
        query.addCriteria(Criteria.where("chargingPointId").is(cpId));
        query.addCriteria(Criteria.where("startTime").is(startTime));
        query.addCriteria(Criteria.where("endTime").is(endTime));
        Update update = new Update();
        update.set(fieldName, value);
        mongoOperations.updateFirst(query, update, DSOOffer.class);
    }
}
