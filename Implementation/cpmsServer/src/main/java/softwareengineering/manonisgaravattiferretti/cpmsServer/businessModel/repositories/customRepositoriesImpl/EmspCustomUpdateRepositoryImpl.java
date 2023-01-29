package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.repositories.customRepositoriesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.EmspDetails;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.repositories.EmspCustomUpdateRepository;

public class EmspCustomUpdateRepositoryImpl implements EmspCustomUpdateRepository {
    private final MongoOperations mongoOperations;

    @Autowired
    public EmspCustomUpdateRepositoryImpl(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public void updateEmspAddCpoToken(String tokenEmsp, String token) {
        Query query = new Query();
        query.addCriteria(Criteria.where("emspToken").is(tokenEmsp));
        Update update = new Update();
        update.set("cpoToken", token);
        mongoOperations.updateFirst(query, update, EmspDetails.class);
    }
}
