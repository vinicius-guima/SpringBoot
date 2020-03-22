package br.com.alura.forum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

//@ComponentScan
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})


@SpringBootApplication
@EnableSpringDataWebSupport
@EnableCaching
public class ForumApplication {

	
	public static void main(String[] args) {
		SpringApplication.run(ForumApplication.class, args);
	}

}
