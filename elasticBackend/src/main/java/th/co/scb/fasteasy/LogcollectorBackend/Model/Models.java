package th.co.scb.fasteasy.LogcollectorBackend.Model;

import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Deque;

@Getter @Setter
@Slf4j
@Component
public class Models {
    private String index;
    private String[] is = new String[5];
    private String[] key = new String[5];
    private String[] value = new String[5];
    private String[] type = new String[4];
    private String[] select;
    private String gte;
    private String lte;
    private String folder;
    private String fileName;
    private String daygte;
    private String timegte;
    private String daylte;
    private String timelte;
    private Deque<String> hits;
    private CsvSchema schema;
    private Date date;
}
