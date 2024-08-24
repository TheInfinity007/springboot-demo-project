package com.learn.springboot_demo_project;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(EmployeeRepository repository) {

        System.out.println("Initializing Database with feed data");
        /*

        // Lambda implementation

        return args -> {
            log.info("Preloading " + repository.save(new Employee("Biblo Baggins", "bulglar")));
            log.info("Preloading " + repository.save(new Employee("Frodo Baggins", "thief")));
        };

        */

        // Anonymous Inner Class Implementation
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                Employee result = repository.save(new Employee("Bilbo Baggins", "Bulglar"));
                log.info("Preloading " + result);
                log.info("Preloading " + repository.save(new Employee("Frodo Baggins", "Thief")).toString());
            }
        };

        /*
            Explanation

            If we look at the lambda expression, it's implementing the run method.
            args is the arguments and inside the curly brackets it's the implementation.

            By using the lambda expression, the run method is not explicitly required
            Because the lambda expression provides a more concise way to implement the single
            abstract method of a functional interface with only one abstract method, run.

            The lambda expression directly provides the implementation for this method.
         */


    }
}
