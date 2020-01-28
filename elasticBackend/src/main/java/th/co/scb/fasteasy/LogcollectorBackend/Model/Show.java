package th.co.scb.fasteasy.LogcollectorBackend.Model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;


@Slf4j
@Component


public class Show {
    public String _index;
    public String _type;
    public String _id;
    public String _score;
    public String[] sort;
    public Map<String,String> _source;


}
