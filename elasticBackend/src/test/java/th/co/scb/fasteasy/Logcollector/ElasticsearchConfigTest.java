package th.co.scb.fasteasy.Logcollector;

import th.co.scb.fasteasy.Logcollector.Elastic.ElasticsearchConfig;
import org.apache.http.Header;
import org.elasticsearch.client.RequestOptions;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ElasticsearchConfigTest {

    private ElasticsearchConfig elasticsearchConfig = new ElasticsearchConfig();

    @Test
    public void checkcreateElasticsearchOptions() {
        elasticsearchConfig.setEsUser("user");
        elasticsearchConfig.setEsPwd("password");

        RequestOptions requestOptions = elasticsearchConfig.createElasticsearchOptions();

        Header authHeader = requestOptions.getHeaders().get(0);

    }

}
