package th.co.scb.fasteasy.Logcollector.Elastic;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Configuration;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
public class AppConfig {

    public RestHighLevelClient elasticsearchClient(ElasticsearchConfig elasticsearchConfig) throws MalformedURLException {
        List<HttpHost> hosts = new ArrayList<>();
        for(String host : elasticsearchConfig.getHost()) {
            try {
                URL url = new URL(host);
                hosts.add(new HttpHost(url.getHost(), url.getPort(), url.getProtocol()));
            } catch (MalformedURLException e) {
            }
        }
        HttpHost[] restHosts = hosts.toArray(new HttpHost[0]);

        return new RestHighLevelClient(RestClient.builder(restHosts));
    }
}
