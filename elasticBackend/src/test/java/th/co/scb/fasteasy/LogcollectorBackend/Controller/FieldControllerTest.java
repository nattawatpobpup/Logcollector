package th.co.scb.fasteasy.LogcollectorBackend.Controller;

import th.co.scb.fasteasy.LogcollectorBackend.Batch.Search;
import th.co.scb.fasteasy.LogcollectorBackend.Model.Models;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class FieldControllerTest {

    @InjectMocks
    FieldController fieldController;
    @Mock
    JobLauncher jobLauncher;
    @Mock
    Search search;
    @Mock
    Job JobGetField;
    @Mock
    Job jobSave;
    @Mock
    Models models;
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this); // Moving this below the next line fixed it...
    }

    @Test
    public void testCheckGetAudit() throws Exception {
        fieldController.getAudit();
    }
    @Test
    public void testCheckGetFequestlog() throws Exception {
        fieldController.getFerequsetlog();
    }
    @Test
    public void testCheckGetFeapilog() throws Exception {
        fieldController.getFeApiLog();
    }

}
