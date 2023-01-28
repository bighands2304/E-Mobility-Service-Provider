package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities;

import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
public class SpecialOffer extends Tariff{
    private LocalTime startTime;
    private LocalTime endTime;
    private Double minKWh;
    private Double maxKWh;
    private Double minCurrent;
    private Double maxCurrent;
    private Double minDuration;
    private Double maxDuration;
    private List<String> daysOfTheWeek;
}
