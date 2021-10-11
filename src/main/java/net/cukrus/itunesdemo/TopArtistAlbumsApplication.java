package net.cukrus.itunesdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class TopArtistAlbumsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TopArtistAlbumsApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.additionalMessageConverters(new JavascriptJackson2HttpMessageConverter()).build();
	}

}
