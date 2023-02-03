package softwareengineering.manonisgaravattiferretti.cpmsServer.dsoHandler.openAdrDtos;

import lombok.Data;

import java.time.LocalTime;

@Data
public class PricingTimeSlotDTO {
    private LocalTime startTime;
    private LocalTime endTime;
    private String unit;
    private Double price;
}
