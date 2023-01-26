package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.CPO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.repositories.CPORepository;

@Service
public class CPOService implements UserDetailsService {
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

    @Override
    public CPO loadUserByUsername(String code) throws UsernameNotFoundException {
        CPO cpo = getCPOData(code);
        if (cpo == null){
            throw new UsernameNotFoundException("CPO not found");
        }
        return new CPO(cpo);
    }
}
