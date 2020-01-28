package th.co.scb.fasteasy.LogcollectorBackend.Batch;


import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class  StepConfig {
    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Autowired
    private Search search;

    @Autowired
    private Writer writer;

    @Bean
    public Step stepGet(){

        return steps.get("stepGet")
                .tasklet(search)
                .build();
    }
    @Bean
    public Step stepWriter(){
        return steps.get("stepWriter")
                .tasklet(writer)
                .build();
    }


    @Bean
    public Job JobSave(){
        return jobs.get("JobSave")
                .incrementer(new RunIdIncrementer())
                .start(stepGet())
                .next(stepWriter())
                .build();
    }
    @Bean
    public Job JobGet(){
        return jobs.get("JobGet")
                .incrementer(new RunIdIncrementer())
                .start(stepGet())
                .build();
    }

}
