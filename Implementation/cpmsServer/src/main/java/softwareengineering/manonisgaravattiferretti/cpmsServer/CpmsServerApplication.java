package softwareengineering.manonisgaravattiferretti.cpmsServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {CassandraAutoConfiguration.class})
@EnableMongoRepositories
// todo: enable this when optimizer are really ready
//@EnableScheduling
public class CpmsServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CpmsServerApplication.class, args);
    }
}
