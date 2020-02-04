package th.co.scb.fasteasy.Logcollector;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class LogcollectorApplication {

	public static void main(String[] args) {
		SpringApplication.run(LogcollectorApplication.class, args);
		
	}


}
