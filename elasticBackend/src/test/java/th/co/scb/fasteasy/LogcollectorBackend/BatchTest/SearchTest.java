package th.co.scb.fasteasy.LogcollectorBackend.BatchTest;

import th.co.scb.fasteasy.LogcollectorBackend.Batch.Search;
import th.co.scb.fasteasy.LogcollectorBackend.Elastic.ElasticseachUtil;
import th.co.scb.fasteasy.LogcollectorBackend.Model.Models;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.test.MetaDataInstanceFactory;

import java.util.Deque;
import java.util.LinkedList;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class SearchTest {
    @InjectMocks
    Search search;
    @Mock
    StepContribution contribution;
    @Mock
    ChunkContext chunkContext;
    @Mock
    Models models;
    @Mock
    ElasticseachUtil elasticseachUtil;


    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    private ChunkContext createChunkContext(String key, Object objectValue) {
        StepExecution stepExecution= Mockito.mock(StepExecution.class);
        StepContext stepContext=Mockito.mock(StepContext.class);
        ChunkContext chunkContext=Mockito.mock(ChunkContext.class);
        JobExecution jobExecution= createJobExecution(key,objectValue);

        when(chunkContext.getStepContext()).thenReturn(stepContext);
        when(stepContext.getStepExecution()).thenReturn(stepExecution);
        when(stepExecution.getJobExecution()).thenReturn(jobExecution);

        return chunkContext;
    }

    private JobExecution createJobExecution(String key,Object objectValue) {
        JobExecution execution = MetaDataInstanceFactory.createJobExecution();
        execution.getExecutionContext().put(key,objectValue);
        return execution;
    }

    @Test
    public void testCheckcheckIs() throws Exception {
        Deque<String> hit = new LinkedList<>();
        hit.add("{\n" +
                "  \"_index\" : \"audit-2020.01.06\",\n" +
                "  \"_type\" : \"_doc\",\n" +
                "  \"_id\" : \"KYXZeW8BKK5SZY2F6DWS\",\n" +
                "  \"_score\" : 5.308107,\n" +
                "  \"_source\" : {\n" +
                "    \"CORRELATION_ID\" : \"50BBB92B-7FD6-49B2-9C8C-69FE1BED8DF5\"\n" +
                "  }\n" +
                "}");


        ChunkContext chunkContext = createChunkContext("hits",hit);
        when(elasticseachUtil.getFasteasyEvents(any(),any())).thenReturn(hit);
        search.execute(contribution,chunkContext);
    }

}
