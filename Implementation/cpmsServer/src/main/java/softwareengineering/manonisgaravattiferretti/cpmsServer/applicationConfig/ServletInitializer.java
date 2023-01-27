package softwareengineering.manonisgaravattiferretti.cpmsServer.applicationConfig;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import softwareengineering.manonisgaravattiferretti.cpmsServer.CpmsServerApplication;

public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(CpmsServerApplication.class);
    }

}
