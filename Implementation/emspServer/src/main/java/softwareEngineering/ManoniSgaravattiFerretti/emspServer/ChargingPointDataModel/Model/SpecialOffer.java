package softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalTime;

@Data
public class SpecialOffer extends Tariff{
    public LocalTime startTime;
    public LocalTime endTime;
    public Double minKWh;
    public Double maxKWh;
    public Double minCurrent;
    public Double maxCurrent;
    public Double minDuration;
    public Double maxDuration;
    public String daysOfTheWeek;
}
