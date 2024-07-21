package com.example.reportgenerator.service;

import com.example.reportgenerator.model.InputRecord;
import com.example.reportgenerator.model.ReferenceRecord;
import com.example.reportgenerator.model.OutputRecord;

import java.util.List;

/**
 * Interface for transformation rules
 * Default rule is implemented using ReportGeneratorService class.
 * Further new rules can be added by implementing this interface.
 */
public interface ReportGenerator {
    List<OutputRecord> generateReport(List<InputRecord> inputRecords, List<ReferenceRecord> referenceRecords);
}
