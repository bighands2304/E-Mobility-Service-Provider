package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.repositories;

import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ReservationCustomUpdate {
    void updateReservationStatus(String status, Long reservationInternalId, LocalDateTime timestamp);
    void updateReservationEnergyAmount(Double energyAmount, Long internalReservationId, LocalDateTime timestamp, Double batteryPercentage);
}
