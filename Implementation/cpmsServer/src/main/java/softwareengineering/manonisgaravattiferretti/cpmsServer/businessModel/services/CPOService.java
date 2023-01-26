package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.CPO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.repositories.CPORepository;

@Service
public class CPOService {
    private final CPORepository cpoRepository;

    @Autowired
    public CPOService(CPORepository cpoRepository) {
        this.cpoRepository = cpoRepository;
    }

    public CPO getCPOData(String cpoCode) {
        return cpoRepository.findCPOByCpoCode(cpoCode);
    }

    public void insertCPO(CPO cpo) {
        cpoRepository.save(cpo);
    }
}
