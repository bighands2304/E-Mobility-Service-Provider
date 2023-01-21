package softwareengineering.manonisgaravattiferretti.cpmsServer.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("cpos")
@NoArgsConstructor
public class CPO {
    @Id
    private String id;
    @Getter
    private String cpoCode;
    @Getter
    private String iban;
    @Getter
    private String password;

    public CPO(String cpoCode, String iban, String password) {
        this.cpoCode = cpoCode;
        this.iban = iban;
        this.password = password;
    }
}
