package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities;

import lombok.Data;

import java.time.LocalTime;

@Data
public class OfferTimeSlot {
    private LocalTime startTime;
    private LocalTime endTime;
}
