package th.co.scb.fasteasy.LogcollectorBackend.Controller;

import th.co.scb.fasteasy.LogcollectorBackend.Batch.Get;
import th.co.scb.fasteasy.LogcollectorBackend.Elastic.Request;
import th.co.scb.fasteasy.LogcollectorBackend.Model.Models;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;

import java.util.Deque;
import java.util.LinkedList;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class RequestControllerTest {
    @InjectMocks
    RequestController requestController;
    @Mock
    JobLauncher jobLauncher;
    @Mock
    Get get;
    @Mock
    Job JobGetField;
    @Mock
    Job jobSave;
    @Mock
    Models models;
    @Mock
    Request request;


    @Mock
    JobExecution jobExecution;
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this); // Moving this below the next line fixed it...
    }

    @Test
    public void testCheckGets() throws Exception {
        String json = "{\"Index\":\"audit*\",\"Is1\":false,\"Key1\":\"EVENT_CODE\",\"Value1\":\"300\",\"Type1\":\"\",\"Is2\":false,\"Key2\":\"\",\"Value2\":\"\",\"Type2\":\"\",\"Is3\":false,\"Key3\":\"\",\"Value3\":\"\",\"Type3\":\"\",\"Is4\":false,\"Key4\":\"\",\"Value4\":\"\",\"Type4\":\"\",\"Is5\":false,\"Key5\":\"\",\"Value5\":\"\",\"Daygte\":\"2020-01-06\",\"Timegte\":\"00:00\",\"Daylte\":\"2020-01-07\",\"Timelte\":\"00:00\",\"selectget\":\"\",\"foldercorrelationsave\":\"\",\"foldersave\":\"\"}";
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
        JobExecution jobExecution = new JobExecution(0L);
        jobExecution.setStatus(BatchStatus.COMPLETED);
        when(jobLauncher.run(any(Job.class), any(JobParameters.class))).thenReturn(jobExecution);
        when(models.getHits()).thenReturn(hit);
        requestController.gets(json);
    }
    @Test
    public void testCheckSave() throws Exception {
        String json = "{\"Index\":\"audit*\",\"Is1\":false,\"Key1\":\"EVENT_CODE\",\"Value1\":\"300\",\"Type1\":\"\",\"Is2\":false,\"Key2\":\"\",\"Value2\":\"\",\"Type2\":\"\",\"Is3\":false,\"Key3\":\"\",\"Value3\":\"\",\"Type3\":\"\",\"Is4\":false,\"Key4\":\"\",\"Value4\":\"\",\"Type4\":\"\",\"Is5\":false,\"Key5\":\"\",\"Value5\":\"\",\"Daygte\":\"2020-01-06\",\"Timegte\":\"00:00\",\"Daylte\":\"2020-01-07\",\"Timelte\":\"00:00\",\"selectget\":\"\",\"foldercorrelationsave\":\"\",\"foldersave\":\"\"}";
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
        JobExecution jobExecution = new JobExecution(0L);
        jobExecution.setStatus(BatchStatus.COMPLETED);
        when(jobLauncher.run(any(Job.class), any(JobParameters.class))).thenReturn(jobExecution);
        when(models.getHits()).thenReturn(hit);
        requestController.save(json);
    }
    @Test
    public void testCheckSavecorrelationID() throws Exception {
        String json = "{\"Index\":\"audit*\",\"Is1\":false,\"Key1\":\"EVENT_CODE\",\"Value1\":\"300\",\"Type1\":\"\",\"Is2\":false,\"Key2\":\"\",\"Value2\":\"\",\"Type2\":\"\",\"Is3\":false,\"Key3\":\"\",\"Value3\":\"\",\"Type3\":\"\",\"Is4\":false,\"Key4\":\"\",\"Value4\":\"\",\"Type4\":\"\",\"Is5\":false,\"Key5\":\"\",\"Value5\":\"\",\"Daygte\":\"2020-01-06\",\"Timegte\":\"00:00\",\"Daylte\":\"2020-01-07\",\"Timelte\":\"00:00\",\"selectget\":\"\",\"foldercorrelationsave\":\"\",\"foldersave\":\"\"}";
        requestController.savecorrelationID(json);
    }
}
