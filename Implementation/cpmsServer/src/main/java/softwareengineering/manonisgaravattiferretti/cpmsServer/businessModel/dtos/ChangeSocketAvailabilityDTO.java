package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChangeSocketAvailabilityDTO {
    @NotBlank
    private String cpId;
    @NotBlank
    private String socketId;
    @NotNull
    private Boolean available;
    @Future
    private LocalDateTime startingTime;
}
