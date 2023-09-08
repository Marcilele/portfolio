package com.bigdata.visualanalysis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.bigdata.visualanalysis.dao")
public class VisualanalysisApplication {

    public static void main(String[] args) {
        SpringApplication.run(VisualanalysisApplication.class, args);
    }

}
