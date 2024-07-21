package com.example.reportgenerator.controller;

import com.example.reportgenerator.config.SchedulerConfig;
import com.example.reportgenerator.service.ReportGenerator;
import com.example.reportgenerator.service.ReportGeneratorService;
import com.example.reportgenerator.service.FileProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

@RestController
public class ReportController {

    private static final Logger logger = LoggerFactory.getLogger(SchedulerConfig.class);
    @Autowired
    private ReportGenerator reportGeneratorService;

    @Autowired
    private FileProcessor fileProcessor;

    @PostMapping("/trigger-report-generation")
    public String triggerReportGeneration() {
        try {
            logger.info("API Trigger: Report generation process started.");

            File inputDir = new File("input");
            File referenceFile = new File("reference/reference.csv");
            File outputDir = new File("output");

            // Ensure the output directory exists
            Files.createDirectories(Paths.get(outputDir.toURI()));

            var referenceRecords = fileProcessor.processReferenceFile(referenceFile);

            // Process each input file in the input directory
            for (File inputFile : Objects.requireNonNull(inputDir.listFiles())) {
                if (inputFile.isFile() && inputFile.getName().endsWith(".csv")) {
                    logger.debug("Processing input file: {}", inputFile.getName());
                    var inputRecords = fileProcessor.processInputFile(inputFile);
                    var outputRecords = reportGeneratorService.generateReport(inputRecords, referenceRecords);

                    String outputFileName = inputFile.getName().replace(".csv", "_output.csv");
                    File outputFile = new File(outputDir, outputFileName);
                    fileProcessor.writeOutputFile(outputRecords, outputFile);
                    logger.debug("Generated report for file: {} and saved as: {}", inputFile.getName(), outputFileName);
                }
            }
            logger.info("API Trigger: Report generation process finished.");
            return "Report generation triggered successfully!";
        } catch (Exception e) {
            logger.error("Error occurred during report generation", e);
            return "Error occurred during report generation: " + e.getMessage();
        }

    }
}
