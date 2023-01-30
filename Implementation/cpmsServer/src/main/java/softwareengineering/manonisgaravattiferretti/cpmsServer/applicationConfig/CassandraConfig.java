package softwareengineering.manonisgaravattiferretti.cpmsServer.applicationConfig;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.core.CassandraTemplate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@Configuration
public class CassandraConfig {
    @Value("${astra.client-id}")
    String username;
    @Value("${astra.clientSecret}")
    String password;
    @Value("${astra.database-id}")
    String databaseId;
    @Value("${astra.cloud-region}")
    String databaseRegion;
    @Value("${astra.keyspace}")
    String keyspaceName;
    private final Logger logger = LoggerFactory.getLogger(CassandraConfig.class);

    @Bean
    public CqlSessionBuilderCustomizer sessionBuilderCustomizer(@Qualifier("astraSecureBundleConfig") DataStaxAstraProperties astraProperties) {
        Path bundle = astraProperties.getSecureConnectBundle().toPath();
        return builder -> builder.withCloudSecureConnectBundle(bundle);
    }

    @Bean
    public CqlSessionBuilder cqlSessionBuilder(CqlSessionBuilderCustomizer customizer) throws SSLException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        logger.info("Creating cqlSessionBuilder");
        CqlSessionBuilder cqlSessionBuilder = CqlSession.builder();
        customizer.customize(cqlSessionBuilder);

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(KeyStore.getInstance("JKS"));
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustManagers, null);
        String host = databaseId + "-" + databaseRegion + ".db.astra.datastax.com";

        cqlSessionBuilder.withAuthCredentials(username, password);
        cqlSessionBuilder.addContactPoint(new InetSocketAddress(host, 29042));
        cqlSessionBuilder.withSslContext(sslContext);
        cqlSessionBuilder.withKeyspace(keyspaceName);

        logger.info("Created cqlSessionBuilder");
        return cqlSessionBuilder;
    }

    @Bean
    public CqlSession cqlSession(CqlSessionBuilder cqlSessionBuilder) {
        return cqlSessionBuilder.build();
    }

    @Bean
    public CassandraTemplate cassandraTemplate(CqlSession cqlSession) {
        return new CassandraTemplate(cqlSession);
    }
}
