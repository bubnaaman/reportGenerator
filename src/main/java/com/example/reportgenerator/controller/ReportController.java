package com.example.reportgenerator.controller;

import com.example.reportgenerator.config.SchedulerConfig;
import com.example.reportgenerator.service.ReportGenerator;
import com.example.reportgenerator.service.ReportGeneratorService;
import com.example.reportgenerator.service.FileProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

@RestController
public class ReportController {

    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(SchedulerConfig.class);

    /**
     * Report generation service
     */
    @Autowired
    private ReportGenerator reportGeneratorService;

    /**
     * File processor
     */
    @Autowired
    private FileProcessor fileProcessor;

    /**
     * Input directory
     */
    @Value("${file.input.dir}")
    private String inputPath;

    /**
     * Reference file path
     */
    @Value("${file.reference.path}")
    private String referenceFilePath;

    /**
     * Output path
     */
    @Value("${file.output.dir}")
    private String outputPath;

    /**
     * REST API endpoint to trigger report generation manually
     * @return Response for API
     */
    @PostMapping("/trigger-report-generation")
    public String triggerReportGeneration() {
        try {
            logger.info("API Trigger: Report generation process started.");

            File lInputDirectory = new File(inputPath);
            File lReferenceFile = new File(referenceFilePath);
            File lOutputDirectory = new File(outputPath);

            // Ensure the output directory exists
            Files.createDirectories(Paths.get(lOutputDirectory.toURI()));

            var lReference = fileProcessor.
                    processReferenceFile(lReferenceFile);

            // Process each input file in the input directory
            for (File lInputFile : Objects.requireNonNull(lInputDirectory.
                    listFiles()))
            {
                if (lInputFile.isFile() && lInputFile.getName().endsWith(".csv"))
                {
                    logger.debug("Processing input file: {}",
                            lInputFile.getName());
                    var inputRecords = fileProcessor.
                            processInputFile(lInputFile);
                    var outputRecords = reportGeneratorService
                            .generateReport(inputRecords, lReference);

                    String lFileName = lInputFile.getName()
                            .replace(".csv", "_output.csv");
                    File lOutputFile = new File(lOutputDirectory, lFileName);
                    fileProcessor.writeOutputFile(outputRecords, lOutputFile);
                    logger.debug("Generated report for file: {} and saved as:" +
                            " {}", lInputFile.getName(), lFileName);
                }
            }
            logger.info("API Trigger: Report generation process finished.");
            return "Report generated successfully!";
        } catch (Exception e) {
            logger.error("API Trigger: Error occurred during report generation"
                    , e);
            return "Error occurred during report generation: " + e.getMessage();
        }
    }
}