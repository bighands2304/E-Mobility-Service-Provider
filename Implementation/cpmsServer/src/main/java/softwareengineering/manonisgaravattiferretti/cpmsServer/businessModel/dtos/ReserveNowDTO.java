package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReserveNowDTO {
    @NotNull
    private Long reservationId;
    @NotNull
    private String chargingPointId;
    @NotNull
    private Integer socketId;
    @Future
    private LocalDateTime expiryDate;
}
