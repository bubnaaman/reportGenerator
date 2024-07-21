// src/main/java/com/example/reportgenerator/service/FileProcessor.java
package com.example.reportgenerator.service;

import com.example.reportgenerator.model.InputRecord;
import com.example.reportgenerator.model.OutputRecord;
import com.example.reportgenerator.model.ReferenceRecord;

import java.io.File;
import java.util.List;

/**
 * File processor interface.
 * CSV file processor is implemented.
 * Support for further files can be added implementing this interface.
 */
public interface FileProcessor {
    List<InputRecord> processInputFile(File aInFile) throws Exception;
    List<ReferenceRecord> processReferenceFile(File aInFile) throws Exception;
    void writeOutputFile(List<OutputRecord> outputRecords, File aInFile)
            throws Exception;
}
