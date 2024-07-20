// src/main/java/com/example/reportgenerator/config/SchedulerConfig.java
package com.example.reportgenerator.config;

import com.example.reportgenerator.service.FileProcessor;
import com.example.reportgenerator.service.ReportGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

@Configuration
@EnableScheduling
public class SchedulerConfig {

    @Autowired
    private ReportGeneratorService reportGeneratorService;

    @Autowired
    private FileProcessor fileProcessor;

    @Scheduled(cron = "0 0 * * * ?")
    public void scheduleReportGeneration() {
        try {
            File inputFile = new File("input/input.csv");
            File referenceFile = new File("reference/reference.csv");
            File outputFile = new File("output/output.csv");

            var inputRecords = fileProcessor.processInputFile(inputFile);
            var referenceRecords = fileProcessor.processReferenceFile(referenceFile);
            var outputRecords = reportGeneratorService.generateReport(inputRecords, referenceRecords);

            // Ensure the output directory exists
            Files.createDirectories(Paths.get("output"));

            fileProcessor.writeOutputFile(outputRecords, outputFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
