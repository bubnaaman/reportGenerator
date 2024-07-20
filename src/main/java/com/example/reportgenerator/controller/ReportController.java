// src/main/java/com/example/reportgenerator/controller/ReportController.java
package com.example.reportgenerator.controller;

import com.example.reportgenerator.model.OutputRecord;
import com.example.reportgenerator.service.FileProcessor;
import com.example.reportgenerator.service.ReportGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportGeneratorService reportGeneratorService;

    @Autowired
    @Qualifier("csvFileProcessor")
    private FileProcessor fileProcessor;

    @PostMapping("/generate")
    public String generateReport() {
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
            return "Report generated successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error generating report";
        }
    }
}
