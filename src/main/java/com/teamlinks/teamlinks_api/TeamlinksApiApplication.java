package com.teamlinks.teamlinks_api;

import com.teamlinks.teamlinks_api.config.ApiProperties;
import com.teamlinks.teamlinks_api.config.ShortenerProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({ApiProperties.class, ShortenerProperties.class})
public class TeamlinksApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TeamlinksApiApplication.class, args);
	}

}
