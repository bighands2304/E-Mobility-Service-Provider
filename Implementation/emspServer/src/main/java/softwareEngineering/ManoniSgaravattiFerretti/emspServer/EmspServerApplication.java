package softwareEngineering.ManoniSgaravattiFerretti.emspServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class EmspServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmspServerApplication.class, args);
	}

}
