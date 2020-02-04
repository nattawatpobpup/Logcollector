package th.co.scb.fasteasy.Logcollector;


import th.co.scb.fasteasy.Logcollector.Controller.FieldController;
import th.co.scb.fasteasy.Logcollector.Elastic.Request;
import th.co.scb.fasteasy.Logcollector.Model.Models;
import th.co.scb.fasteasy.Logcollector.Model.Show;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class RequestTest {

    @InjectMocks
    Request request;
    @Mock
    Models models;
    @Mock
    FieldController fieldController;
    @Mock
    Job JobGetField;
    @Mock
    Job jobSave;
    @Mock
    JobLauncher jobLauncher;
    @Mock
    JobExecution jobExecution;
    String Folder = "E:\\";
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this); // Moving this below the next line fixed it...
    }

    @Test
    public void testCheckSortString() throws Exception {
        String[] check
                = {"","EVENT_CODE","","CORRELATIO_ID",""};
        String[] finals = {"EVENT_CODE","CORRELATIO_ID","","",""};
        assertEquals(request.sortString(check),finals);
    }
    @Test
    public void testCheckDateComplete() throws Exception {
        String Date = "2020-01-01 00:00:00";
        request.checkDate(Date, "start");
    }

    @Test
    public void testCheckDateIsNotHaveDay() throws Exception {
        Exception thrown = assertThrows(Exception.class,
                () -> request.checkDate(" 00:00:00", "start"));
        assertEquals("please insert start day", thrown.getMessage());
    }

    @Test
    public void testCheckDateIsNotHaveTime() throws Exception {
        Exception thrown = assertThrows(Exception.class,
                () -> request.checkDate("2020-01-01 ", "start"));
        assertEquals("please insert start time", thrown.getMessage());
    }

    @Test
    public void testCheckDateIsNotHaveTime2() throws Exception {
        Exception thrown = assertThrows(Exception.class,
                () -> request.checkDate("2020-01-01 :00", "start"));
        assertEquals("please insert start time", thrown.getMessage());
    }

    @Test
    public void testCheckDateIsNotHaveDate() throws Exception {
        Exception thrown = assertThrows(Exception.class,
                () -> request.checkDate("", "start"));
        assertEquals("please insert start day-time", thrown.getMessage());
    }

    @Test
    public void testCheckDateIsNotHaveDate2() throws Exception {
        Exception thrown = assertThrows(Exception.class,
                () -> request.checkDate(" :00", "start"));
        assertEquals("please insert start day-time", thrown.getMessage());
    }

    @Test
    public void testCheckSoureComplete() throws Exception {
        String[] is = {"false", "false", "false", "false", "false"};
        String[] key = {"EVENT_CODE", "", "", "", ""};
        String[] value = {"300", "", "", "", ""};
        String[] type = {"", "", "", ""};
        request.source(is, key, value, type, "2020-01-01 00:00:00", "2020-01-01 00:00:00");
    }

    @Test
    public void testCheckSoureNotHaveISKey() throws Exception {
        String[] is = {"false", "false", "false", "false", "false"};
        String[] key = {"", "", "", "", ""};
        String[] value = {"300", "", "", "", ""};
        String[] type = {"", "", "", ""};

        Exception thrown = assertThrows(Exception.class,
                () -> request.source(is, key, value, type, "2020-01-01 00:00:00", "2020-01-01 00:00:00"));
        assertEquals("please select key 1", thrown.getMessage());
    }

    @Test
    public void testCheckSoureNotHaveISValue() throws Exception {
        String[] is = {"false", "false", "false", "false", "false"};
        String[] key = {"EVENT_CODE", "", "", "", ""};
        String[] value = {"", "", "", "", ""};
        String[] type = {"", "", "", ""};
        Exception thrown = assertThrows(Exception.class,
                () -> request.source(is, key, value, type, "2020-01-01 00:00:00", "2020-01-01 00:00:00"));
        assertEquals("please insert value 1", thrown.getMessage());
    }

    @Test
    public void testCheckSoureNotHaveType() throws Exception {
        String[] is = {"false", "false", "false", "false", "false"};
        String[] key = {"EVENT_CODE", "CORRELATION_ID", "", "", ""};
        String[] value = {"300", "50BBB92B-7FD6-49B2-9C8C-69FE1BED8DF5", "", "", ""};
        String[] type = {"", "", "", ""};
        Exception thrown = assertThrows(Exception.class,
                () -> request.source(is, key, value, type, "2020-01-01 00:00:00", "2020-01-01 00:00:00"));
        assertEquals("please select function 1", thrown.getMessage());
    }

    @Test
    public void testCheckRemoveDuplicateElementsHAVEOne() throws Exception {
        String[] arr = {};
        ArrayList<String>
                list = new ArrayList<>(
                Arrays
                        .asList(arr));
        assertEquals(request.removeDuplicates(list),list);
    }

    @Test
    public void testCheckRemoveDuplicateElementsNull() throws Exception {
        String[] arr = {""};
        ArrayList<String>
                list = new ArrayList<>(
                Arrays
                        .asList(arr));
        assertEquals(request.removeDuplicates(list),list);
    }

    @Test
    public void testCheckRemoveDuplicateElements() throws Exception {
        String[] arr = {"8409F00D-F4ED-45DF-A664-99C601FA0F4C", null, "8409F00D-F4ED-45DF-A664-99C601FA0F4C", "50BBB92B-7FD6-49B2-9C8C-69FE1BED8DF5", "0f20d1af-db9a-4ca3-a2f6-d2615a9fb3e4", "0f20d1af-db9a-4ca3-a2f6-d2615a9fb3e4"};
        ArrayList<String>
                list = new ArrayList<>(
                Arrays
                        .asList(arr));
        String[] arr2 = {"8409F00D-F4ED-45DF-A664-99C601FA0F4C", "50BBB92B-7FD6-49B2-9C8C-69FE1BED8DF5", "0f20d1af-db9a-4ca3-a2f6-d2615a9fb3e4"};
        ArrayList<String>
                list2 = new ArrayList<>(
                Arrays
                        .asList(arr2));
        assertEquals(request.removeDuplicates(list),list2);
    }

    @Test
    public void testCheckSetCorrelationID() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        Show[] object = mapper.readValue("[{\n" +
                "  \"_index\" : \"audit-2020.01.06\",\n" +
                "  \"_type\" : \"_doc\",\n" +
                "  \"_id\" : \"J4XZeW8BKK5SZY2F6DWS\",\n" +
                "  \"_score\" : 1.0,\n" +
                "  \"_source\" : {\n" +
                "    \"CORRELATION_ID\" : \"8409F00D-F4ED-45DF-A664-99C601FA0F4C\"\n" +
                "  }\n" +
                "}, {\n" +
                "  \"_index\" : \"audit-2020.01.06\",\n" +
                "  \"_type\" : \"_doc\",\n" +
                "  \"_id\" : \"KIXZeW8BKK5SZY2F6DWS\",\n" +
                "  \"_score\" : 1.0,\n" +
                "  \"_source\" : { }\n" +
                "}, {\n" +
                "  \"_index\" : \"audit-2020.01.06\",\n" +
                "  \"_type\" : \"_doc\",\n" +
                "  \"_id\" : \"KYXZeW8BKK5SZY2F6DWS\",\n" +
                "  \"_score\" : 1.0,\n" +
                "  \"_source\" : {\n" +
                "    \"CORRELATION_ID\" : \"50BBB92B-7FD6-49B2-9C8C-69FE1BED8DF5\"\n" +
                "  }\n" +
                "}]", Show[].class);
        String[] correlationID = new String[object.length];
        correlationID = request.setCorrelationID(correlationID, object, "CORRELATION_ID");
        assertEquals(correlationID[0], "8409F00D-F4ED-45DF-A664-99C601FA0F4C");
        assertEquals(correlationID[1], null);
        assertEquals(correlationID[2], "50BBB92B-7FD6-49B2-9C8C-69FE1BED8DF5");

    }

    @Test
    public void testCheckFileName() throws Exception {
        assertEquals("123456789audit", request.setFileName("123456789", "audit*"));
    }

    @Test
    public void testCheckSetSchema() throws Exception {
        String[] csv = {"EVENT_CODE", "CORRELATION_ID"};
        request.setSchema(csv);
    }

    @Test
    public void  testcheckStatus() throws Exception{
        JobExecution jobExecution = new JobExecution(0L);
        jobExecution.setStatus(BatchStatus.FAILED);
        Exception thrown = assertThrows(Exception.class,
                () -> request.checkStatus(jobExecution));
        assertEquals("ERROR", thrown.getMessage());
    }

    //==============================setData========================
    @Test
    public void testChecksetData() throws Exception {
        String json = "{\"Index\":\"audit*\",\"Is1\":false,\"Key1\":\"EVENT_CODE\",\"Value1\":\"300\",\"Type1\":\"\",\"Is2\":false,\"Key2\":\"\",\"Value2\":\"\",\"Type2\":\"\",\"Is3\":false,\"Key3\":\"\",\"Value3\":\"\",\"Type3\":\"\",\"Is4\":false,\"Key4\":\"\",\"Value4\":\"\",\"Type4\":\"\",\"Is5\":false,\"Key5\":\"\",\"Value5\":\"\",\"Daygte\":\"2020-01-06\",\"Timegte\":\"00:00\",\"Daylte\":\"2020-01-07\",\"Timelte\":\"00:00\",\"selectget\":\"\",\"foldercorrelationsave\":\"\",\"foldersave\":\"\"}";
        when(models.getGte()).thenReturn("2020-01-01 00:00");
        when(models.getLte()).thenReturn("2020-01-02 00:00");
        when(models.getIndex()).thenReturn("audit*");
        request.setData(json);
    }

    @Test
    public void testChecksetData2() throws Exception {
        String json = "{\"Index\":\"audit*\",\"Is1\":false,\"Key1\":\"EVENT_CODE\",\"Value1\":\"300\",\"Type1\":\"and\",\"Is2\":false,\"Key2\":\"\",\"Value2\":\"\",\"Type2\":\"\",\"Is3\":false,\"Key3\":\"\",\"Value3\":\"\",\"Type3\":\"\",\"Is4\":false,\"Key4\":\"\",\"Value4\":\"\",\"Type4\":\"\",\"Is5\":false,\"Key5\":\"\",\"Value5\":\"\",\"Daygte\":\"2020-01-06\",\"Timegte\":\"00:00\",\"Daylte\":\"2020-01-07\",\"Timelte\":\"00:00\",\"selectget\":[\"CORRELATION_ID\",\"EVENT_CODE\"],\"foldercorrelationsave\":\"\",\"foldersave\":\"\"}";
        when(models.getGte()).thenReturn("2020-01-01 00:00");
        when(models.getLte()).thenReturn("2020-01-02 00:00");
        when(models.getIndex()).thenReturn("audit*");
        request.setData(json);
    }

    @Test
    public void testChecksetData3() throws Exception {
        String json = "{\"Index\":\"audit*\",\"Is1\":false,\"Key1\":\"EVENT_CODE\",\"Value1\":\"300\",\"Type1\":\"and\",\"Is2\":false,\"Key2\":\"\",\"Value2\":\"\",\"Type2\":\"\",\"Is3\":false,\"Key3\":\"\",\"Value3\":\"\",\"Type3\":\"\",\"Is4\":false,\"Key4\":\"\",\"Value4\":\"\",\"Type4\":\"\",\"Is5\":false,\"Key5\":\"\",\"Value5\":\"\",\"Daygte\":\"2020-01-06\",\"Timegte\":\"00:00\",\"Daylte\":\"2020-01-07\",\"Timelte\":\"00:00\",\"selectget\":[\"CORRELATION_ID\",\"EVENT_CODE\"],\"foldercorrelationsave\":\"\",\"foldersave\":\"\"}";
        when(models.getGte()).thenReturn("2020-01-01 00:00");
        when(models.getLte()).thenReturn("2020-01-02 00:00");
        when(models.getIndex()).thenReturn("");
        Exception thrown = assertThrows(Exception.class,
                () -> request.setData(json));
        assertEquals("please select index", thrown.getMessage());
    }

    @Test
    public void testChecksetDataIsNotJson() throws Exception {

        String json = "\"Index\":\"audit*\",\"Is1\":false,\"Key1\":\"\",\"Value1\":\"\",\"Type1\":\"\",\"Is2\":false,\"Key2\":\"\",\"Value2\":\"\",\"Type2\":\"\",\"Is3\":false,\"Key3\":\"\",\"Value3\":\"\",\"Type3\":\"\",\"Is4\":false,\"Key4\":\"\",\"Value4\":\"\",\"Type4\":\"\",\"Is5\":false,\"Key5\":\"\",\"Value5\":\"\",\"Daygte\":\"2020-01-06\",\"Timegte\":\"00:00\",\"Daylte\":\"2020-01-07\",\"Timelte\":\"00:00\",\"selectget\":\"\",\"foldercorrelationsave\":\"\",\"foldersave\":\"\"}";

        Exception thrown = assertThrows(Exception.class,
                () -> request.setData(json));
        assertEquals("Data input isn't JSON", thrown.getMessage());
    }
//==============================setData========================

    //==============================save========================
    @Test
    public void testCheckSaveAudit() throws Exception {
        FileWriter fw = new FileWriter(Folder+"testout.txt");
        fw.write("Welcome to javaTpoint.");
        fw.close();
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy.MM.dd.HH.mm.ss");
        JobExecution jobExecution = new JobExecution(0L);
        jobExecution.setStatus(BatchStatus.COMPLETED);
        when(jobLauncher.run(any(Job.class), any(JobParameters.class))).thenReturn(jobExecution);
        String[] field = {"EVENT_CODE", "CORRELATION_ID"};
        when(models.getGte()).thenReturn("2020-01-01 00:00:00", "2020-01-01 00:00:00");
        when(models.getLte()).thenReturn("2020-01-01 05:00:00", "2020-01-01 05:00:00");
        when(models.getDaygte()).thenReturn("2020-01-01");
        when(models.getDaylte()).thenReturn("00:00");
        when(models.getTimegte()).thenReturn("2020-01-01");
        when(models.getTimelte()).thenReturn("23:00");
        when(models.getIndex()).thenReturn("audit*");
        when(models.getFolder()).thenReturn(Folder);
        when(fieldController.getAudits()).thenReturn(field);
        when(models.getSelect()).thenReturn(field);
        when(models.getFile()).thenReturn(Folder+"testout.txt ");
        when(models.getDate()).thenReturn(new Date());
        File file = new File(models.getFolder() + "/"
                +ft.format(models.getDate())
                +".zip");
        request.save();
        file.delete();
    }

    @Test
    public void testCheckSaveFerequestlogFAILED() throws Exception {
        JobExecution jobExecution = new JobExecution(0L);
        jobExecution.setStatus(BatchStatus.FAILED);
        when(jobLauncher.run(any(Job.class), any(JobParameters.class))).thenReturn(jobExecution);
        String[] field = {"EVENT_CODE", "CORRELATION_ID"};
        when(models.getGte()).thenReturn("2020-01-01 00:00:00", "2020-01-01 00:00:00");
        when(models.getLte()).thenReturn("2020-01-01 05:00:00", "2020-01-01 05:00:00");
        when(models.getDaygte()).thenReturn("2020-01-01");
        when(models.getDaylte()).thenReturn("00:00");
        when(models.getTimegte()).thenReturn("2020-01-01");
        when(models.getTimelte()).thenReturn("23:00");
        when(models.getIndex()).thenReturn("fe-request-log*");
        when(models.getFolder()).thenReturn(Folder);
        when(fieldController.getAudits()).thenReturn(field);
        when(models.getSelect()).thenReturn(field);
        Exception thrown = assertThrows(Exception.class,
                () -> request.save());
        assertEquals("EXPORT ERROR", thrown.getMessage());
    }

    @Test
    public void testCheckSaveFeapilog() throws Exception {
        FileWriter fw = new FileWriter(Folder+"testout.txt");
        fw.write("Welcome to javaTpoint.");
        fw.close();
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy.MM.dd.HH.mm.ss");
        JobExecution jobExecution = new JobExecution(0L);
        jobExecution.setStatus(BatchStatus.COMPLETED);
        when(jobLauncher.run(any(Job.class), any(JobParameters.class))).thenReturn(jobExecution);
        String[] field = {"EVENT_CODE", "CORRELATION_ID"};
        when(models.getGte()).thenReturn("2020-01-01 00:00:00", "2020-01-01 00:00:00");
        when(models.getLte()).thenReturn("2020-01-01 05:00:00", "2020-01-01 05:00:00");
        when(models.getDaygte()).thenReturn("2020-01-01");
        when(models.getDaylte()).thenReturn("00:00");
        when(models.getTimegte()).thenReturn("2020-01-01");
        when(models.getTimelte()).thenReturn("23:00");
        when(models.getIndex()).thenReturn("fe-api-log*");
        when(models.getFolder()).thenReturn(Folder);
        when(fieldController.getAudits()).thenReturn(field);
        when(models.getSelect()).thenReturn(field);
        when(models.getFile()).thenReturn(Folder+"testout.txt ");
        when(models.getDate()).thenReturn(new Date());
        File file = new File(models.getFolder() + "/"
                +ft.format(models.getDate())
                +".zip");
        request.save();
        file.delete();
    }

    @Test
    public void testCheckSaveNotHavePath() throws Exception {
        when(models.getFolder()).thenReturn("");
        Exception thrown = assertThrows(Exception.class,
                () -> request.save());

        assertEquals("please insert path",thrown.getMessage());
    }
//==============================save========================

    //==============================saveCorrelationID========================
    @Test
    public void testChecksaveCorrelationIDAudit() throws Exception {
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
        FileWriter fw = new FileWriter(Folder+"testout.txt");
        fw.write("Welcome to javaTpoint.");
        fw.close();
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy.MM.dd.HH.mm.ss");
        JobExecution jobExecution = new JobExecution(0L);
        jobExecution.setStatus(BatchStatus.COMPLETED);
        when(jobLauncher.run(any(Job.class), any(JobParameters.class))).thenReturn(jobExecution);
        String[] field = {"EVENT_CODE","CORRELATION_ID"};
        when(models.getFolder()).thenReturn(Folder);
        when(models.getGte()).thenReturn("2020-01-01 00:00:00","2020-01-01 00:00:00");
        when(models.getLte()).thenReturn("2020-01-01 05:00:00","2020-01-01 05:00:00");
        when(models.getDaygte()).thenReturn("2020-01-01");
        when(models.getDaylte()).thenReturn("00:00");
        when(models.getTimegte()).thenReturn("2020-01-01");
        when(models.getTimelte()).thenReturn("23:00");
        when(models.getIndex()).thenReturn("audit*");
        when(fieldController.getAudits()).thenReturn(field);
        when(fieldController.getFerequsetlogs()).thenReturn(field);
        when(fieldController.getFeapilogs()).thenReturn(field);
        when(models.getSelect()).thenReturn(field);
        when(models.getHits()).thenReturn(hit);
        when(models.getFile()).thenReturn(Folder+"testout.txt ");
        when(models.getDate()).thenReturn(new Date());
        File file = new File(models.getFolder() + "/"
                +ft.format(models.getDate())+"-50BBB92B-7FD6-49B2-9C8C-69FE1BED8DF5"
                +".zip");
        request.saveCorrelationID();
        file.delete();
    }
    @Test
    public void testChecksaveCorrelationIDFerequestlog() throws Exception {
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
        String[] field = {"EVENT_CODE","CORRELATION_ID"};
        when(models.getFolder()).thenReturn(Folder);
        when(models.getGte()).thenReturn("2020-01-01 00:00:00","2020-01-01 00:00:00");
        when(models.getLte()).thenReturn("2020-01-01 05:00:00","2020-01-01 05:00:00");
        when(models.getDaygte()).thenReturn("2020-01-01");
        when(models.getDaylte()).thenReturn("00:00");
        when(models.getTimegte()).thenReturn("2020-01-01");
        when(models.getTimelte()).thenReturn("23:00");
        when(models.getIndex()).thenReturn("fe-request-log*");
        when(fieldController.getAudits()).thenReturn(field);
        when(fieldController.getFerequsetlogs()).thenReturn(field);
        when(fieldController.getFeapilogs()).thenReturn(field);
        when(models.getSelect()).thenReturn(field);
        when(models.getHits()).thenReturn(hit);
        request.saveCorrelationID();
    }
    @Test
    public void testChecksaveCorrelationIDFeapilog() throws Exception {
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
        String[] field = {"EVENT_CODE","CORRELATION_ID"};
        when(models.getFolder()).thenReturn(Folder);
        when(models.getGte()).thenReturn("2020-01-01 00:00:00","2020-01-01 00:00:00");
        when(models.getLte()).thenReturn("2020-01-01 05:00:00","2020-01-01 05:00:00");
        when(models.getDaygte()).thenReturn("2020-01-01");
        when(models.getDaylte()).thenReturn("00:00");
        when(models.getTimegte()).thenReturn("2020-01-01");
        when(models.getTimelte()).thenReturn("23:00");
        when(models.getIndex()).thenReturn("fe-api-log*");
        when(fieldController.getAudits()).thenReturn(field);
        when(fieldController.getFerequsetlogs()).thenReturn(field);
        when(fieldController.getFeapilogs()).thenReturn(field);
        when(models.getSelect()).thenReturn(field);
        when(models.getHits()).thenReturn(hit);
        request.saveCorrelationID();
    }
    @Test
    public void testChecksaveCorrelationIDNotHavePath() throws Exception {
        when(models.getFolder()).thenReturn("");
        Exception thrown = assertThrows(Exception.class,
                () -> request.saveCorrelationID());
        assertEquals("please insert path",thrown.getMessage());
    }
//==============================saveCorrelationID========================

}
