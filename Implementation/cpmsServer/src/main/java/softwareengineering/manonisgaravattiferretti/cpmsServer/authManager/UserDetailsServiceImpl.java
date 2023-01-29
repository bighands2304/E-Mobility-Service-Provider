package softwareengineering.manonisgaravattiferretti.cpmsServer.authManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.CPO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.EmspDetails;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.CPOService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.EmspDetailsService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final CPOService cpoService;
    private final EmspDetailsService emspDetailsService;

    @Autowired
    public UserDetailsServiceImpl(CPOService cpoService, EmspDetailsService emspDetailsService) {
        this.cpoService = cpoService;
        this.emspDetailsService = emspDetailsService;
    }

    @Override
    public CPO loadUserByUsername(String code) throws UsernameNotFoundException {
        CPO cpo = cpoService.getCPOData(code);
        if (cpo == null){
            throw new UsernameNotFoundException("CPO not found");
        }
        return new CPO(cpo);
    }

    public EmspDetails loadEmspByToken(String token) throws UsernameNotFoundException {
        return emspDetailsService.findByEmspToken(token)
                .orElseThrow(() -> new UsernameNotFoundException("emsp not exists"));
    }
}
