package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ChargingProfile {
    @Id
    @Indexed
    private int id;
    private String recurrencyKind;
    private LocalDateTime validFrom;
    private LocalDateTime validTo;
    private List<ChargingSchedulePeriod> periods;
}
