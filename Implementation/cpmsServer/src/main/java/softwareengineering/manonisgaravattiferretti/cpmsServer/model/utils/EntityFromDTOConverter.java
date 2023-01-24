package softwareengineering.manonisgaravattiferretti.cpmsServer.model.utils;

import org.springframework.beans.BeanUtils;
import softwareengineering.manonisgaravattiferretti.cpmsServer.model.dtos.CPORegistrationDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.model.entities.CPO;

public class EntityFromDTOConverter {
    public static CPO cpoFromRegistrationDTO(CPORegistrationDTO cpoRegistrationDTO) {
        CPO cpo = new CPO();
        BeanUtils.copyProperties(cpoRegistrationDTO, cpo);
        return cpo;
    }
}
