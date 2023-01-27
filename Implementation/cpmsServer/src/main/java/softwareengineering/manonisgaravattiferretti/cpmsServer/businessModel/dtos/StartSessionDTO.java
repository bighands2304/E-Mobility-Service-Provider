package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class StartSessionDTO {
    @NotNull
    @PositiveOrZero
    private Long reservationId;
    @NotBlank
    private String chargingPointId;
    @NotNull
    @PositiveOrZero
    private Integer socketId;
}
