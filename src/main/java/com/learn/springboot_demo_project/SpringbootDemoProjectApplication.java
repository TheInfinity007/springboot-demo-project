package com.learn.springboot_demo_project;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class SpringbootDemoProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootDemoProjectApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunnerCustom(ApplicationContext ctx) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                System.out.println("Let's inspect the beans provided by the Spring Boot");

                String appName = ctx.getApplicationName();
                String displayName = ctx.getDisplayName();

                String[] beanNames = ctx.getBeanDefinitionNames();

                Arrays.sort(beanNames);

//                for (String bean : beanNames) {
//                    System.out.print(bean + ", ");
//                }

                System.out.println();
            }
        };
    }

}
