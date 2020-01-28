package th.co.scb.fasteasy.LogcollectorBackend.BatchTest;

import th.co.scb.fasteasy.LogcollectorBackend.Batch.Writer;
import th.co.scb.fasteasy.LogcollectorBackend.Model.Models;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Deque;
import java.util.LinkedList;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class WriterTest {

    @InjectMocks
    Writer writer;
    @Mock
    StepContribution contribution;
    @Mock
    Models models;
    @Mock
    ChunkContext chunkContext;
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this); // Moving this below the next line fixed it...
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
    public void testCheckexecute() throws Exception {
        Deque<String> hit = new LinkedList<>();
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy.MM.dd.HH.mm.ss");

        hit.add("{\n" +
                "  \"_index\" : \"audit-2020.01.06\",\n" +
                "  \"_type\" : \"_doc\",\n" +
                "  \"_id\" : \"KYXZeW8BKK5SZY2F6DWS\",\n" +
                "  \"_score\" : null,\n" +
                "  \"_source\" : {\n" +
                "    \"EVENT_CODE\" : 300,\n" +
                "    \"CORRELATION_ID\" : \"50BBB92B-7FD6-49B2-9C8C-69FE1BED8DF5\"\n" +
                "  },\n" +
                "  \"sort\" : [\n" +
                "    1578254206870\n" +
                "  ]\n" +
                "}");
        hit.add("{\n" +
                "  \"_index\" : \"audit-2020.01.06\",\n" +
                "  \"_type\" : \"_doc\",\n" +
                "  \"_id\" : \"KYXZeW8BKK5SZY2F6DWS\",\n" +
                "  \"_score\" : null,\n" +
                "  \"_source\" : {\n" +
                "    \"EVENT_CODE\" : 300,\n" +
                "    \"CORRELATION_ID\" : \"50BBB92B-7FD6-49B2-9C8C-69FE1BED8DF5\"\n" +
                "  },\n" +
                "  \"sort\" : [\n" +
                "    1578254206870\n" +
                "  ]\n" +
                "}");

        String[] field = {"EVENT_CODE","CORRELATION_ID"};
        CsvSchema.Builder builder = CsvSchema.builder();
        builder.addColumn("_index");
        builder.addColumn("_type");
        builder.addColumn("_id");
        builder.addColumn("_score");
        builder.addColumn("sort");
        for (int j = 0; j < field.length; j++) {
            builder.addColumn(field[j]);
        }

        when(models.getFolder()).thenReturn("E:/");
        when(models.getFileName()).thenReturn("TEST");
        Date date = new Date();
        File file = new File(models.getFolder() + "/"
                +ft.format(date)+ "-"
                +models.getFileName()+ ".csv");

        CsvSchema schema = builder.setUseHeader(true).build();
        Object[] hitsArray = hit.toArray();
        ChunkContext chunkContext = createChunkContext("hitsArray",hitsArray);
        when(models.getSchema()).thenReturn(schema);
        when(models.getDate()).thenReturn(date);
        writer.execute(contribution,chunkContext);
        file.delete();

    }

}
