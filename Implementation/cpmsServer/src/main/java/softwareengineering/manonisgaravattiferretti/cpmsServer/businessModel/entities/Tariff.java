package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Tariff {
    @Id
    private String tariffId;
    private String socketType;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double price;
    private Integer stepSize;
    @JsonIgnore
    private LocalDateTime lastUpdated;
}
