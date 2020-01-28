package th.co.scb.fasteasy.LogcollectorBackend.Elastic;

import lombok.Data;
import org.apache.commons.codec.binary.Base64;
import org.elasticsearch.client.RequestOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Data
@ConfigurationProperties(prefix = "elasticsearch")
@Configuration
public class ElasticsearchConfig {

    private String[] host;

    @Value("${es.config.user}")
    private String esUser;
    @Value("${es.config.pwd}")
    private String esPwd;

    public RequestOptions createElasticsearchOptions() {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();

        byte[] authBase64 = Base64.encodeBase64((esUser + ":" + esPwd).getBytes());
        builder.addHeader(HttpHeaders.AUTHORIZATION, "Basic " + new String(authBase64));

        return builder.build();
    }

}
