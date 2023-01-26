package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class AddTariffDTO {
    @NotBlank
    private String cpId;
    @NotBlank
    private String socketType;
    @Future
    private LocalDate startDate;
    @Future
    private LocalDate endDate;
    @NotNull
    private Double price;
    @NotNull
    private Integer stepSize;

    // Optional parameters (only for add special offer)
    public LocalTime startTime;
    public LocalTime endTime;
    public Double minKWh;
    public Double maxKWh;
    public Double minCurrent;
    public Double maxCurrent;
    public Double minDuration;
    public Double maxDuration;
    public List<String> daysOfTheWeek;
}
