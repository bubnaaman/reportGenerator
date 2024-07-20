// src/main/java/com/example/reportgenerator/service/FileProcessor.java
package com.example.reportgenerator.service;

import com.example.reportgenerator.model.InputRecord;
import com.example.reportgenerator.model.OutputRecord;
import com.example.reportgenerator.model.ReferenceRecord;

import java.io.File;
import java.util.List;

public interface FileProcessor {
    List<InputRecord> processInputFile(File file) throws Exception;
    List<ReferenceRecord> processReferenceFile(File file) throws Exception;
    void writeOutputFile(List<OutputRecord> outputRecords, File file) throws Exception;
}
