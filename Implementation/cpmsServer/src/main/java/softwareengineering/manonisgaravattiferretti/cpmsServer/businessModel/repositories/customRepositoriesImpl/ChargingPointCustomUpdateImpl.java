package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.repositories.customRepositoriesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.ChangeSocketAvailabilityDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.IncludeBatteryDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.ChargingPoint;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.Tariff;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.repositories.ChargingPointCustomUpdate;

public class ChargingPointCustomUpdateImpl implements ChargingPointCustomUpdate {
    private final MongoOperations mongoOperations;

    @Autowired
    public ChargingPointCustomUpdateImpl(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public void updateBatteryEnergyFlow(IncludeBatteryDTO includeBatteryDTO, String cpId, Integer batteryId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(cpId));
        query.addCriteria(Criteria.where("batteries.batteryId").is(batteryId));
        Update update = new Update();
        update.set("batteries.$.minLevel", includeBatteryDTO.getMinLevel());
        update.set("batteries.$.maxLevel", includeBatteryDTO.getMaxLevel());
        update.set("batteries.$.percent", includeBatteryDTO.getPercent());
        mongoOperations.updateFirst(query, update, ChargingPoint.class);
    }

    @Override
    public void addTariff(String id, Tariff tariff) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        Update update = new Update();
        update.addToSet("tariffs", tariff);
        mongoOperations.updateFirst(query, update, ChargingPoint.class);
    }

    @Override
    public void updateBatteryAvailability(String id, int batteryId, Boolean available) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        Update update = new Update();
        update.set("batteries.$.status", (available) ? "IDLE" : "UNAVAILABLE");
        mongoOperations.updateFirst(query, update, ChargingPoint.class);
    }

    @Override
    public void updateToggleOptimizer(String id, String optimizerType, boolean isAutomatic) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        Update update = new Update();
        update.set("toggle" + optimizerType + "Optimizer", isAutomatic);
        mongoOperations.updateFirst(query, update, ChargingPoint.class);
    }
}
