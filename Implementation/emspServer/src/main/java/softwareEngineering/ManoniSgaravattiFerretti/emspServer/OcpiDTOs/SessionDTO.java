package softwareEngineering.ManoniSgaravattiFerretti.emspServer.OcpiDTOs;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SessionDTO {
    private String sessionId;
    private Long reservationId;
    private Double batteryPercentage;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private LocalDateTime expiryDate;
    private Double kwh;
    private String chargingPointId;
    private Integer socketId;
    private String status;
    private Double totalCost;
    private LocalDateTime lastUpdated;
}
