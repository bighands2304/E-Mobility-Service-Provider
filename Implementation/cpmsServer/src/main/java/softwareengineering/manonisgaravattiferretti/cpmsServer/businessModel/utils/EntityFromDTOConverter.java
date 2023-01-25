package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.utils;

import org.springframework.beans.BeanUtils;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.CPORegistrationDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.EmspCredentialsDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.CPO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.EmspDetails;

public class EntityFromDTOConverter {
    public static CPO cpoFromRegistrationDTO(CPORegistrationDTO cpoRegistrationDTO) {
        CPO cpo = new CPO();
        BeanUtils.copyProperties(cpoRegistrationDTO, cpo);
        return cpo;
    }

    public static EmspDetails emspDetailsFromCredentials(EmspCredentialsDTO emspCredentials) {
        EmspDetails emspDetails = new EmspDetails();
        BeanUtils.copyProperties(emspCredentials, emspDetails);
        return emspDetails;
    }
}
