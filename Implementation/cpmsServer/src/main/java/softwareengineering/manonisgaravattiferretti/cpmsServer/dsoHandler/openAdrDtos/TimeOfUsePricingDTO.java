package softwareengineering.manonisgaravattiferretti.cpmsServer.dsoHandler.openAdrDtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TimeOfUsePricingDTO {
    private LocalDateTime timestamp;
    @NotEmpty
    private List<PricingTimeSlotDTO> intervals;
}
