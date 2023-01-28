package softwareengineering.manonisgaravattiferretti.cpmsServer.applicationConfig;

import java.io.File;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "datastax.astra")
@ConfigurationPropertiesScan
public class DataStaxAstraProperties {
    private File secureConnectBundle;

    public File getSecureConnectBundle() {
        return secureConnectBundle;
    }

    public void setSecureConnectBundle(String secureConnectBundlePath) {
        this.secureConnectBundle = new File(secureConnectBundlePath);
    }
}
