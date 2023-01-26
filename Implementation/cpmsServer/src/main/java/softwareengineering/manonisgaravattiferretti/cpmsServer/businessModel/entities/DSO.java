package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "dsos")
public class DSO {
    @Id
    private String id;
    private String companyName;
    private List<DSOOffer> offers;
}
