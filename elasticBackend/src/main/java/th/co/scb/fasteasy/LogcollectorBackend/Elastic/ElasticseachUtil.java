package th.co.scb.fasteasy.LogcollectorBackend.Elastic;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service

public class ElasticseachUtil {
    private static final int MAX_SEARCH_SIZE = 10000;
    @Autowired
    private AppConfig appConfig;
    private RestHighLevelClient client;
    private ElasticsearchConfig elasticsearchConfig;
    @Autowired
    public ElasticseachUtil(ElasticsearchConfig elasticsearchConfig,AppConfig appConfig) throws MalformedURLException {
        this.elasticsearchConfig = elasticsearchConfig;
        this.client=appConfig.elasticsearchClient(elasticsearchConfig);
    }
    public List<SearchHit> search(SearchSourceBuilder search, String index) throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.source(search.size(MAX_SEARCH_SIZE));
        searchRequest.scroll(TimeValue.timeValueMinutes(3L));
        SearchResponse response = client.search(searchRequest,elasticsearchConfig.createElasticsearchOptions());
        List<SearchHit> searchHits = new ArrayList<>(Arrays.asList(response.getHits().getHits()));
        String scrollId = response.getScrollId();
        while (true) {
            SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
            scrollRequest.scroll(TimeValue.timeValueSeconds(30));
            SearchResponse searchScrollResponse = client.scroll(scrollRequest, elasticsearchConfig.createElasticsearchOptions());

            if(searchScrollResponse.getHits().getHits().length == 0) {
                break;
            }
            scrollId = searchScrollResponse.getScrollId();
            searchHits.addAll(Arrays.asList(searchScrollResponse.getHits().getHits()));
        }
        return searchHits;
    }

    public BoolQueryBuilder checkIs(String is,String key, String value){
        BoolQueryBuilder check =  QueryBuilders.boolQuery();
        if ((is.equals("false") && !key.equals("")&& !value.equals(""))) {
            check.must(QueryBuilders.termQuery(key, value));
        } else if ((is.equals("true")) && !key.equals("")&& !value.equals("")) {
            check.mustNot(QueryBuilders.termQuery(key, value));
        }
        return check;
    }
    public BoolQueryBuilder setANDOR(String type,BoolQueryBuilder isft,BoolQueryBuilder issd){
        BoolQueryBuilder set = QueryBuilders.boolQuery();
        if (type.equals("and")) {
            set.must(QueryBuilders.boolQuery().must(isft).must(issd));
        } else if (type.equals("or")) {
            set.should(QueryBuilders.boolQuery().should(isft).should(issd));
        }
        return set;
    }


    public SearchSourceBuilder createSearchParameter(String[] is,String[] key, String[] value,String[] type,String[] select,
                                                     String gte, String lte) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder[] iss = new BoolQueryBuilder[5];
        for(int i=0;i<iss.length;i++){
            iss[i] = checkIs(is[i],key[i],value[i]);
        }
        BoolQueryBuilder[] step = new BoolQueryBuilder[4];
        for(int i=0;i<step.length;i++){
            if(i==0){
                step[i] = setANDOR(type[i], iss[i], iss[i+1]);
            }else {
                step[i] = setANDOR(type[i], step[i-1], iss[i+1]);
            }
        }
        QueryBuilder time = QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery("@timestamp").gte(gte).lte(lte).format("yyy-MM-dd HH:mm:ss").timeZone("+07:00"));
        BoolQueryBuilder finals = QueryBuilders.boolQuery();
        if (type[0].equals("")&&!key[0].equals("")) {
            finals.must(QueryBuilders.boolQuery().must(iss[0]).must(time));
        }else if(!type[0].equals("")&&!type[1].equals("")&&!type[2].equals("")&&!type[3].equals("")){
            finals.must(QueryBuilders.boolQuery().must(step[3]).must(time));
        }else if(!type[0].equals("")&&!type[1].equals("")&&!type[2].equals("")){
            finals.must(QueryBuilders.boolQuery().must(step[2]).must(time));
        }else if(!type[0].equals("")&&!type[1].equals("")){
            finals.must(QueryBuilders.boolQuery().must(step[1]).must(time));
        }else if (!type[0].equals("")){
            finals.must(QueryBuilders.boolQuery().must(step[0]).must(time));
        }else{
            finals.must(QueryBuilders.rangeQuery("@timestamp").gte(gte).lte(lte).format("yyy-MM-dd HH:mm:ss").timeZone("+07:00"));
        }

        searchSourceBuilder.query(finals);
        searchSourceBuilder.sort("@timestamp");
        searchSourceBuilder.fetchSource(select, null);

        return searchSourceBuilder;


    }





    public Deque<String> getFasteasyEvents(SearchSourceBuilder createdSearchParameter, String index) throws IOException {
        List<SearchHit> hits = search(createdSearchParameter,index);
        if(hits.isEmpty()) {
            return new ArrayDeque<>(0);
        }
        return hits.stream()
                .map(h -> {
                    return h.toString();
                })
                .collect(Collectors.toCollection(ArrayDeque::new));
    }

}
