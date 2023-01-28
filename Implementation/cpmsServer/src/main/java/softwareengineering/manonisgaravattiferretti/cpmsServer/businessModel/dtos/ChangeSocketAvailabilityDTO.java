package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ChangeSocketAvailabilityDTO {
    @NotNull
    private Boolean available;
    @Future
    private LocalDateTime startingTime;
}
