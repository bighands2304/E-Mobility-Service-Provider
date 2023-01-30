package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDateTime;

@Data
@Document(collection = "reservations")
public class Reservation {
    @Id
    private String id;
    @Indexed
    private Long reservationIdEmsp; // id used by the emsp
    @Indexed
    private Long internalReservationId; // id used by the charging point
    @Indexed
    private Long sessionId; // id of the charging session
    private String status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double energyAmount;
    private Double totalCost;
    private LocalDateTime lastUpdated;
    private LocalDateTime expiryDate;
    @DocumentReference
    private Socket socket;
    @DocumentReference(lazy = true)
    private EmspDetails emspDetails;
}
