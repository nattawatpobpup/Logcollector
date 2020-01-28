package th.co.scb.fasteasy.LogcollectorBackend.Elastic;

import th.co.scb.fasteasy.LogcollectorBackend.Controller.FieldController;
import th.co.scb.fasteasy.LogcollectorBackend.Model.Models;
import th.co.scb.fasteasy.LogcollectorBackend.Model.Show;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

@Service
public class Request {
    @Autowired
    JobLauncher jobLauncher;
    @Qualifier("JobSave")
    @Autowired
    Job jobSave;
    @Qualifier("JobGet")
    @Autowired
    Job JobGetField;
    @Autowired
    private Models models;
    @Autowired
    private FieldController fieldController;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private ObjectMapper mapper = new ObjectMapper();

    public String[] sortString(String[] sort){
        int count = 0;
        String[] temp = new String[sort.length];
        for(int i=0;i<temp.length;i++){
            temp[i] = "";
        }
        for(int i=0;i<sort.length;i++){
            if(!sort[i].equals("")){
                temp[count] = sort[i];
                count++;
            }
        }
        return temp;
    }
    public void source(String[] is,String[]key,String[]value,String[]type,String gte,String lte) throws Exception {

        checkDate(gte,"start");
        checkDate(lte,"final");

        for (int i=0;i<value.length;i++){
            if(i!=0){
                if((!value[i].equals("")&&!key[i].equals(""))&&type[i-1].equals("")){
                    throw new Exception("please select function "+(i));
                }
            }
            if(value[i].equals("")&&!key[i].equals("")){
                throw new Exception("please insert value "+(i+1));
            }else if(!value[i].equals("")&&key[i].equals("")){
                throw new Exception("please select key "+(i+1));
            }
        }

    }
    public void checkDate(String date,String status) throws Exception {
        if(!date.matches("([0-9]{4})-([0-9]{2})-([0-9]{2}) ([0-9]{2}):([0-9]{2}):([0-9]{2})")) {

            if (date.matches(" :([0-9]{2})")||date.matches("")) {
                throw new Exception("please insert day-time "+status);
            } else if (date.matches("([0-9]{4})-([0-9]{2})-([0-9]{2}) :([0-9]{2})")||date.matches("([0-9]{4})-([0-9]{2})-([0-9]{2}) ")) {
                throw new Exception("please insert time "+status);
            } else if (date.matches(" ([0-9]{2}):([0-9]{2}):([0-9]{2})")){
                throw new Exception("please insert day "+status);
            }

        }
    }
    public <T> ArrayList<T> removeDuplicates(ArrayList<T> list) {
        ArrayList<T> newList = new ArrayList<T>();
        for (T element : list) {
            if (!newList.contains(element)&&element!=null) {
                newList.add(element);
            }
        }
        return newList;
    }
    public String[] setCorrelationID(String[] correlationID, Show[] object, String correlation_id) {
        for (int i = 0; i < correlationID.length; i++) {
            correlationID[i] = object[i]._source.get(correlation_id);
        }
        return correlationID;
    }
    public String setFileName(String s, String index) {
        String filename = index.replace("*", "");
        return s+filename;
    }
    public CsvSchema setSchema(String[] file){
        CsvSchema.Builder builder = CsvSchema.builder();
        builder.addColumn("_index");
        builder.addColumn("_type");
        builder.addColumn("_id");
        builder.addColumn("_score");
        builder.addColumn("sort");
        for (int j = 0; j < file.length; j++) {
            builder.addColumn(String.valueOf(file[j]));
        }
        CsvSchema schema = builder.setUseHeader(true).build();
        return schema;
    }
    public void checkStatus(JobExecution execution) throws Exception {
        if(execution.getStatus() == BatchStatus.FAILED){
            throw new Exception("ERROR");
        }
    }
    public void setData(String data) throws Exception {
        final String decoded = URLDecoder.decode(data, "UTF-8");        // DECODE UTF8
        data = decoded;
        String[] is = new String[5];
        String[] key = new String[5];
        String[] value = new String[5];
        String[] type = new String[4];
        if(data.charAt(0) == '{'){
            try {
                JSONObject obj = new JSONObject(data.toString());
                JSONArray arr = obj.getJSONArray("selectget");
                String[] stringsArray = new String[arr.length()];
                for (int i = 0; i < arr.length(); i++) {
                    stringsArray[i] = arr.getString(i);
                }
                models.setSelect(stringsArray);
            } catch (Exception e) {
                models.setSelect(null);
            }
            JsonNode actualObj = mapper.readTree(data);
            models.setIndex(actualObj.get("Index").textValue());
            is[0]=String.valueOf(actualObj.get("Is1"));
            is[1]=String.valueOf(actualObj.get("Is2"));
            is[2]=String.valueOf(actualObj.get("Is3"));
            is[3]=String.valueOf(actualObj.get("Is4"));
            is[4]=String.valueOf(actualObj.get("Is5"));
            key[0]=actualObj.get("Key1").textValue();
            key[1]=actualObj.get("Key2").textValue();
            key[2]=actualObj.get("Key3").textValue();
            key[3]=actualObj.get("Key4").textValue();
            key[4]=actualObj.get("Key5").textValue();
            value[0]=actualObj.get("Value1").textValue();
            value[1]=actualObj.get("Value2").textValue();
            value[2]=actualObj.get("Value3").textValue();
            value[3]=actualObj.get("Value4").textValue();
            value[4]=actualObj.get("Value5").textValue();
            type[0]=actualObj.get("Type1").textValue();
            type[1]=actualObj.get("Type2").textValue();
            type[2]=actualObj.get("Type3").textValue();
            type[3]=actualObj.get("Type4").textValue();
            models.setDaygte(actualObj.get("Daygte").textValue());
            models.setTimegte(actualObj.get("Timegte").textValue());
            models.setDaylte(actualObj.get("Daylte").textValue());
            models.setTimelte(actualObj.get("Timelte").textValue());
            models.setFolder(actualObj.get("foldersave").textValue());
        }else{
            throw new Exception("Data input isn't JSON");
        }
        if(models.getIndex().equals("")){
            throw new Exception("please select index");
        }
        for(int i=0;i<type.length;i++){
            if(!type[i].equals("")&&(key[i+1].equals("")||value[i+1].equals(""))){
                type[i]="";
            }
        }
        models.setGte(models.getDaygte()+" "+models.getTimegte()+":00");
        models.setLte(models.getDaylte()+" "+models.getTimelte()+":00");
        is=sortString(is);
        key=sortString(key);
        value=sortString(value);
        type=sortString(type);
        source(is,key,value,type,models.getGte(),models.getLte());
        models.setType(type);
        models.setIs(is);
        models.setKey(key);
        models.setValue(value);
    }
    public void save() throws Exception {
        if(models.getFolder().equals("")){
            throw new Exception("please insert path");
        }else if(models.getIndex().equals("audit*")) {
            models.setSelect(fieldController.getAudits());
        }else if(models.getIndex().equals("fe-request-log*")){
            models.setSelect(fieldController.getFerequsetlogs());
        }else if(models.getIndex().equals("fe-api-log*")){
            models.setSelect(fieldController.getFeapilogs());
        }
        models.setSchema(setSchema(models.getSelect()));

        Date startDate = formatter.parse(models.getGte());
        Date endDate = formatter.parse(models.getLte());

        Calendar start = Calendar.getInstance();
        start.setTime(startDate);

        Calendar end = Calendar.getInstance();
        end.setTime(endDate);

        for (Date date = start.getTime(); start.before(end); start.add(Calendar.HOUR, 2), date = start.getTime()) {
            // Do your job here with `date`.
            SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
            models.setGte(ft.format(date));
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.HOUR, 1);
            c.add(Calendar.MINUTE, 59);
            c.add(Calendar.SECOND,59);
            date = c.getTime();
            if(end.before(c)){
                models.setLte(ft.format(endDate));
            }else{
                models.setLte(ft.format(date));
            }
            System.out.println(models.getGte());
            System.out.println(models.getLte()+"\n");
            String filename = models.getIndex().replace("*", "");
            models.setFileName(filename);
            models.setDate(new Date());
            JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
            JobExecution execution = jobLauncher.run(jobSave, jobParameters);
            if(execution.getStatus() == BatchStatus.FAILED){
                throw new Exception("EXPORT ERROR");
            }
        }
        models.setGte(models.getDaygte()+" "+models.getTimegte()+":00");
        models.setLte(models.getDaylte()+" "+models.getTimelte()+":00");
        System.out.println(models.getGte());
        System.out.println(models.getLte()+"\n");
    }
    public String[] saveCorrelationID() throws Exception {
        String[] is = {"false","false","false","false","false"};
        String[] key = {"","","","",""};
        String[] value = {"","","","",""};
        String[] type = {"","","",""};
        if(models.getFolder().equals("")){
            throw new Exception("please insert path");
        }else if(models.getIndex().equals("audit*")){
            models.setSelect(new String[]{"CORRELATION_ID"});
        }else if(models.getIndex().equals("fe-request-log*")||models.getIndex().equals("fe-api-log*")){
            models.setSelect(new String[]{"api_correlationid"});
        }
        JobParameters jobParametersGet = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
        JobExecution getExecution = jobLauncher.run(JobGetField, jobParametersGet);
        checkStatus(getExecution);
        Show[] object = mapper.readValue(String.valueOf(models.getHits()), Show[].class);
        String[] correlationID = new String[object.length];

        if(models.getIndex().equals("audit*")){
            correlationID = setCorrelationID(correlationID,object,"CORRELATION_ID") ;
        }else if(models.getIndex().equals("fe-request-log*")||models.getIndex().equals("fe-api-log*")){
            correlationID = setCorrelationID(correlationID,object,"api_correlationid");
        }
        ArrayList<String>
                list = new ArrayList<>(
                Arrays
                        .asList(correlationID));
        ArrayList<String>
                newList = removeDuplicates(list);

        CsvSchema schemaAudit = setSchema(fieldController.getAudits());
        CsvSchema schemaFequestLog = setSchema(fieldController.getFerequsetlogs());
        CsvSchema schemaFeapiLog = setSchema(fieldController.getFeapilogs());
        String[] fieldAudit = fieldController.getAudits();
        String[] fieldFequestLog = fieldController.getFerequsetlogs();
        String[] fieldFeapiLog = fieldController.getFeapilogs();
        models.setType(type);
        models.setIs(is);
        for (int i=0; i<newList.size(); i++) {
            value[0] = newList.get(i);
            models.setDate(new Date());
            key[0] = "CORRELATION_ID";
            models.setIndex("audit*");
            models.setSelect(fieldAudit);
            models.setSchema(schemaAudit);
            models.setFileName(setFileName(newList.get(i), models.getIndex()));
            models.setKey(key);
            models.setValue(value);
            JobParameters JobAudit = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
            JobExecution JobExecutionAudit = jobLauncher.run(jobSave, JobAudit);
            checkStatus(JobExecutionAudit);

            key[0] = "api_correlationid";
            models.setIndex("fe-request-log*");
            models.setSelect(fieldFequestLog);
            models.setSchema(schemaFequestLog);
            models.setFileName(setFileName(newList.get(i), models.getIndex()));
            models.setKey(key);
            models.setValue(value);
            JobParameters JobRequestlog = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
            JobExecution JobExecutionFerequestlog = jobLauncher.run(jobSave, JobRequestlog);
            checkStatus(JobExecutionFerequestlog);

            models.setIndex("fe-api-log*");
            models.setSelect(fieldFeapiLog);
            models.setSchema(schemaFeapiLog);
            models.setFileName(setFileName(newList.get(i), models.getIndex()));
            JobParameters JobApilog = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
            JobExecution JobExecutionFeapilog = jobLauncher.run(jobSave, JobApilog);
            checkStatus(JobExecutionFeapilog);
        }
        return newList.toArray(new String[0]);
    }

}
