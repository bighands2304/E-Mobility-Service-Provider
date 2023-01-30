package softwareengineering.manonisgaravattiferretti.cpmsServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import softwareengineering.manonisgaravattiferretti.cpmsServer.applicationConfig.DataStaxAstraProperties;

import java.nio.file.Path;

@SpringBootApplication(exclude = {CassandraAutoConfiguration.class})
//@EnableConfigurationProperties(DataStaxAstraProperties.class)
@EnableMongoRepositories
@EnableScheduling
public class CpmsServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CpmsServerApplication.class, args);
    }
}
