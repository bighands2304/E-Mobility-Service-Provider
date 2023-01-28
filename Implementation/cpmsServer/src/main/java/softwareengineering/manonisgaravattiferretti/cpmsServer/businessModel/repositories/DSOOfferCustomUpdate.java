package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.repositories;

import org.springframework.stereotype.Repository;

import java.time.LocalTime;

@Repository
public interface DSOOfferCustomUpdate {
    void updateOfferFromId(String id, Double price);
    void updateOfferFromDsoIdCpIdTimeSlot(String dsoId, String cpId, LocalTime startTime, LocalTime endTime, Double price);
    void updateCapacityFromDsoIdCpIdTimeSlot(String dsoId, String cpId, LocalTime startTime, LocalTime endTime, Double capacity);
}
