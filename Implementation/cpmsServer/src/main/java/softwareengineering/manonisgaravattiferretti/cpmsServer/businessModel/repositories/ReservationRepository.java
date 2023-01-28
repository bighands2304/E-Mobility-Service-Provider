package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.EmspDetails;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.Reservation;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.repositories.aggregationResults.MaxSessionId;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ReservationRepository extends MongoRepository<Reservation, String>, ReservationCustomUpdate {
    Optional<Reservation> findReservationByReservationIdEmspAndEmspDetails(Long reservationId, EmspDetails emspDetails);
    Optional<Reservation> findReservationBySessionId(Long sessionId);
    Optional<Reservation> findReservationByInternalReservationId(Long internalReservationId);
    Page<Reservation> findReservationsByLastUpdatedBetween(LocalDateTime dateFrom, LocalDateTime dateTo, Pageable pageable);
    @Aggregation(pipeline = {"""
            {
                $group:  {
                    '_id': true,\s
                    'maxSessionId': {$max: 'sessionId'}\s
                }
            }
            """, """
            {
                $project: {
                    '_id': 0,
                    'maxSessionId': 1
                }
            }
            """
    })
    AggregationResults<MaxSessionId> maxSessionId();
}
