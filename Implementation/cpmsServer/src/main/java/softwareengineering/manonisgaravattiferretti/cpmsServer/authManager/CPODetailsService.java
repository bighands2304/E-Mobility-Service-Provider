package softwareengineering.manonisgaravattiferretti.cpmsServer.authManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.CPO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.CPOService;

@Service
public class CPODetailsService implements UserDetailsService {
    private final CPOService cpoService;

    @Autowired
    public CPODetailsService(CPOService cpoService) {
        this.cpoService = cpoService;
    }

    @Override
    public CPO loadUserByUsername(String code) throws UsernameNotFoundException {
        CPO cpo = cpoService.getCPOData(code);
        if (cpo == null){
            throw new UsernameNotFoundException("CPO not found");
        }
        return new CPO(cpo);
    }
}
