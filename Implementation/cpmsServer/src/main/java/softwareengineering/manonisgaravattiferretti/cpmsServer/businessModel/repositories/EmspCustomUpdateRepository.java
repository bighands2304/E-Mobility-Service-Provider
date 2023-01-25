package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.repositories;

import org.springframework.stereotype.Repository;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.EmspCredentialsDTO;

@Repository
public interface EmspCustomUpdateRepository {
    void updateEmspAddCpoToken(String tokenEmsp, String token);
    void updateEmspCredentials(String tokenOld, EmspCredentialsDTO emspCredentialsDTO);
}
