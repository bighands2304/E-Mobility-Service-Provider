package softwareengineering.manonisgaravattiferretti.cpmsServer.applicationConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "datastax.astra")
@ConfigurationPropertiesScan
@Qualifier("astraSecureBundleConfig")
public class DataStaxAstraProperties {
    private File secureConnectBundle;

    public File getSecureConnectBundle() {
        return secureConnectBundle;
    }

    public void setSecureConnectBundle(String secureConnectBundlePath) throws URISyntaxException, IOException {
        InputStream secureConnectBundleStream = this.getClass().getClassLoader().getResourceAsStream(secureConnectBundlePath);
        File tempFile = File.createTempFile("secure-connect-cpms-data-warehouse", ".zip");
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            secureConnectBundleStream.transferTo(out);
        }
        this.secureConnectBundle = new File(tempFile.getAbsolutePath());
        //this.secureConnectBundle = new File(secureConnectBundlePath);
    }
}
