package th.co.scb.fasteasy.LogcollectorBackend.Batch;

import th.co.scb.fasteasy.LogcollectorBackend.Elastic.ElasticseachUtil;
import th.co.scb.fasteasy.LogcollectorBackend.Elastic.Request;
import th.co.scb.fasteasy.LogcollectorBackend.Model.Models;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.batch.core.*;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import java.util.Deque;

@Slf4j
@Component
@RestController
public class Search implements Tasklet {

    @Autowired
    private ElasticseachUtil elasticseachUtil;
    @Autowired
    private Models models;
    @Autowired
    private Request request;
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception{
        SearchSourceBuilder createSearchParameter = elasticseachUtil.createSearchParameter(models.getIs(),models.getKey(),
                models.getValue(),models.getType(),models.getSelect(),models.getGte(),models.getLte());
        Deque<String> hits = elasticseachUtil.getFasteasyEvents(createSearchParameter,models.getIndex());
        models.setHits(hits);
        Object[] hitsArray = hits.toArray();
        //Convert to JSON and put to JSONArray
        chunkContext.getStepContext()
                .getStepExecution()
                .getJobExecution()
                .getExecutionContext()
                .put("hitsArray",hitsArray);
        chunkContext.getStepContext()
                .getStepExecution()
                .getJobExecution()
                .getExecutionContext()
                .put("hits",hits);

        return RepeatStatus.FINISHED;
    }
}
