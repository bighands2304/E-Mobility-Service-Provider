package softwareEngineering.ManoniSgaravattiFerretti.emspServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/*
	Spring Initializr settings
	https://start.spring.io/#!type=maven-project&language=java&platformVersion=3.0.2&packaging=jar&jvmVersion=17&groupId=softwareEngineering.ManoniSgaravattiFerretti&artifactId=emspServer&name=emspServer&description=eMSPServer%20for%20the%20Software%20Engineering%20project&packageName=softwareEngineering.ManoniSgaravattiFerretti.emspServer&dependencies=data-jpa,lombok,security,web,data-mongodb
*/
@SpringBootApplication
public class EmspServerApplication{

	public static void main(String[] args) {
		SpringApplication.run(EmspServerApplication.class, args);
	}
}
