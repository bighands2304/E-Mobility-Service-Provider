package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfferTimeSlot {
    private LocalTime startTime;
    private LocalTime endTime;
}
