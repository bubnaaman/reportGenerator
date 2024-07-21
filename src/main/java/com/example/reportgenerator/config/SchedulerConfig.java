package com.example.reportgenerator.config;

import com.example.reportgenerator.service.ReportGenerator;
import com.example.reportgenerator.service.FileProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

@Configuration
@EnableScheduling
public class SchedulerConfig {

    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(SchedulerConfig.class);

    /**
     * Report generator service
     */
    @Autowired
    private ReportGenerator reportGeneratorService;

    @Autowired
    private FileProcessor fileProcessor;

    @Value("${report.schedule.fixedRate}")
    private long fixedRate;

    public SchedulerConfig() {
    }

    @Scheduled(fixedRateString = "${report.schedule.fixedRate}")
    public void processFiles() {
        logger.info("Scheduled task started: Report generation process started.");
        try {
            File inputDir = new File("input");
            File referenceFile = new File("reference/reference.csv");
            File outputDir = new File("output");

            // Ensure the output directory exists
            Files.createDirectories(Paths.get(outputDir.toURI()));

            var referenceRecords = fileProcessor.processReferenceFile(referenceFile);

            // Process each input file in the input directory
            for (File inputFile : Objects.requireNonNull(inputDir.listFiles())) {
                if (inputFile.isFile() && inputFile.getName().endsWith(".csv")) {
                    logger.info("Processing input file: {}", inputFile.getName());
                    var inputRecords = fileProcessor.processInputFile(inputFile);
                    var outputRecords = reportGeneratorService.generateReport(inputRecords, referenceRecords);

                    String outputFileName = inputFile.getName().replace(".csv", "_output.csv");
                    File outputFile = new File(outputDir, outputFileName);
                    fileProcessor.writeOutputFile(outputRecords, outputFile);
                    logger.info("Generated report for file: {} and saved as: {}", inputFile.getName(), outputFileName);
                }
            }
        } catch (Exception aInException) {
            logger.error("Error occurred during report generation", aInException);
            logger.warn("Scheduled task completed: Report generation process finished with errors.");
            return;
        }
        logger.info("Scheduled task completed: Report generation process finished.");
    }
}
