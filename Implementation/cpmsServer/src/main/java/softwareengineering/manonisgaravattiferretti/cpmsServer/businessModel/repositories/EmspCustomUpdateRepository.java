package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.repositories;

import org.springframework.stereotype.Repository;

@Repository
public interface EmspCustomUpdateRepository {
    void updateEmspAddCpoToken(String tokenEmsp, String token);
}
