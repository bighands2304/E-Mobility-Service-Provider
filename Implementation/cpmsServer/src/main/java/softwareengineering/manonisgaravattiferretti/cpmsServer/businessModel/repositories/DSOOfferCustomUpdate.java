package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.repositories;

import org.springframework.stereotype.Repository;

import java.time.LocalTime;

@Repository
public interface DSOOfferCustomUpdate {
    void updateOfferFromId(String id, Double price);
    void updateOfferFromDsoTokenCpIdTimeSlot(String dsoToken, String cpId, LocalTime startTime, LocalTime endTime, Double price);
    void updateCapacityFromDsoTokenCpIdTimeSlot(String dsoToken, String cpId, LocalTime startTime, LocalTime endTime, Double capacity);
    void updateCapacityFromDsoTokenCpId(String dsoToken, String cpId, Double capacity);
}
