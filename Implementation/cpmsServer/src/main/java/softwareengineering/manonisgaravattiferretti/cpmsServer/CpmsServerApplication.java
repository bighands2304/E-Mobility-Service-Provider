package softwareengineering.manonisgaravattiferretti.cpmsServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraDataAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, CassandraDataAutoConfiguration.class})
@EnableMongoRepositories
public class CpmsServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CpmsServerApplication.class, args);
    }

}
