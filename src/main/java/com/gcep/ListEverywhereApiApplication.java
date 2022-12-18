package com.gcep;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Main class for starting the Spring application
 * @author Gabriel Cepleanu
 * @version 0.1
 *
 */
@ComponentScan({"com.gcep"})
@SpringBootApplication
public class ListEverywhereApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ListEverywhereApiApplication.class, args);
	}

}
