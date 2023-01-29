package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.repositories.aggregationResults;

import lombok.Data;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.Tariff;

@Data
public class TariffUnwind {
    private Tariff tariff;
}
