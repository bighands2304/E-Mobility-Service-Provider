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
        update.set("valid", true);
        mongoOperations.updateFirst(query, update, DSOOffer.class);
    }

    @Override
    public void updateOfferFromDsoTokenCpIdTimeSlot(String dsoToken, String cpId, LocalTime startTime,
                                                 LocalTime endTime, Double price) {
        updateFieldDouble(dsoToken, cpId, startTime, endTime, "price", price);
    }

    @Override
    public void updateCapacityFromDsoTokenCpIdTimeSlot(String dsoToken, String cpId, LocalTime startTime,
                                                    LocalTime endTime, Double capacity) {
        updateFieldDouble(dsoToken, cpId, startTime, endTime, "capacity", capacity);
    }

    @Override
    public void updateCapacityFromDsoTokenCpId(String dsoToken, String cpId, Double capacity) {
        Query query = new Query();
        query.addCriteria(Criteria.where("dsoToken").is(dsoToken));
        query.addCriteria(Criteria.where("chargingPointId").is(cpId));
        Update update = new Update();
        update.set("capacity", capacity);
        update.set("valid", true);
        mongoOperations.updateFirst(query, update, DSOOffer.class);
    }

    private void updateFieldDouble(String dsoToken, String cpId, LocalTime startTime,
                                   LocalTime endTime, String fieldName, Double value) {
        Query query = new Query();
        query.addCriteria(Criteria.where("dsoToken").is(dsoToken));
        query.addCriteria(Criteria.where("chargingPointId").is(cpId));
        query.addCriteria(Criteria.where("startTime").is(startTime));
        query.addCriteria(Criteria.where("endTime").is(endTime));
        Update update = new Update();
        update.set(fieldName, value);
        update.set("valid", true);
        mongoOperations.updateFirst(query, update, DSOOffer.class);
    }
}
