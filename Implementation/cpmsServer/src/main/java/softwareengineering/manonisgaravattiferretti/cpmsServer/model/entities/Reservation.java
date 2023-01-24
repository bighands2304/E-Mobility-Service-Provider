package softwareengineering.manonisgaravattiferretti.cpmsServer.model.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDateTime;

@Data
@Document(collection = "reservations")
public class Reservation {
    @Id
    private String id;
    private Long reservationId;
    private Long sessionId;
    private String status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double energyAmount;
    @DocumentReference(lazy = true)
    private Socket socket;
}
