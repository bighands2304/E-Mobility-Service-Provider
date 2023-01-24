package softwareengineering.manonisgaravattiferretti.cpmsServer.model.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ChargingProfile {
    @Id
    private int id;
    private String recurrencyKind;
    private LocalDateTime validFrom;
    private LocalDateTime validTo;
    private List<ChargingSchedulePeriod> periods;
}
