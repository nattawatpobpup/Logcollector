package th.co.scb.fasteasy.Logcollector.Controller;


import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import th.co.scb.fasteasy.Logcollector.Model.Models;
import th.co.scb.fasteasy.Logcollector.Model.Show;
import th.co.scb.fasteasy.Logcollector.Elastic.Request;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class RequestController {
    @Autowired
    JobLauncher jobLauncher;
    @Qualifier("JobSave")
    @Autowired
    Job jobSave;
    @Qualifier("JobGet")
    @Autowired
    Job JobGetField;
    @Autowired
    private Request request;
    @Autowired
    private Models models;
    ObjectMapper mapper = new ObjectMapper();

    @PostMapping("/gets")
    public ResponseEntity<?> gets(@RequestBody String data) throws Exception {
        request.setData(data);
        JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
        JobExecution execution = jobLauncher.run(JobGetField, jobParameters);
        Show[] object = mapper.readValue(String.valueOf(models.getHits()), Show[].class);
        request.checkStatus(execution);

        return new ResponseEntity<>(object, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody String data) throws Exception {
        request.setData(data);
        request.save();
        JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
        JobExecution execution = jobLauncher.run(JobGetField, jobParameters);
        Show[] object = mapper.readValue(String.valueOf(models.getHits()), Show[].class);
        request.checkStatus(execution);
        return new ResponseEntity<>(object, HttpStatus.OK);
    }

    @PostMapping("/saveCorrelationID")
    public ResponseEntity<?> savecorrelationID(@RequestBody String data) throws Exception {
        request.setData(data);
        return new ResponseEntity<>(request.saveCorrelationID(), HttpStatus.OK);
    }

}
