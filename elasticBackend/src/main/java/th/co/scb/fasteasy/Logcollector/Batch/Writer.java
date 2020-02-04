package th.co.scb.fasteasy.Logcollector.Batch;

import th.co.scb.fasteasy.Logcollector.Model.Models;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.io.File;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


@Slf4j
@Component
@Controller

public class Writer implements Tasklet {
//    @Autowired
//    private ShowController showController;
    @Autowired
    private Models models;
    private Date date;
    SimpleDateFormat ft = new SimpleDateFormat ("yyyy.MM.dd.HH.mm.ss.SSS");
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception{
        String hitall = new String();
        Object[] hitsArray = (Object[]) chunkContext
                .getStepContext()
                .getStepExecution()
                .getJobExecution()
                .getExecutionContext()
                .get("hitsArray");


        if(hitsArray.length!=0) {
//        body
            hitall+=("[\n");
            for (int i = 0; i < hitsArray.length; i++) {
                hitall+=String.valueOf(hitsArray[i]);
                if (i < hitsArray.length - 1) {
                    hitall+=(",");
                }
                hitall+=("\n");
            }
            hitall+=("]");
            String replacecsv1 = hitall.replace("\"_source\" : {\n", "");
            String replacecsv2 = replacecsv1.replace("},\n  \"sort\" : [\n    ", ",\"sort\" : \"");
            String replacecsv3 = replacecsv2.replace("\n  ]\n}", "\"\n}");

                File file = new File(models.getFolder() + "/"
                        +ft.format(models.getDate())+ "-"
                        +models.getFileName()+ ".csv");

                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonTree = mapper.readTree(replacecsv3);
                CsvMapper csvMapper = new CsvMapper();
                csvMapper.writerFor(JsonNode.class)
                        .with(models.getSchema())
                        .writeValue(file, jsonTree);
                String tmp = models.getFile();
                models.setFile(tmp+=file+" ");


        }
       return RepeatStatus.FINISHED;
    }





}
