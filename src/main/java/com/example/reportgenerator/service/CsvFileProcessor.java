// src/main/java/com/example/reportgenerator/service/CsvFileProcessor.java
package com.example.reportgenerator.service;

import com.example.reportgenerator.model.InputRecord;
import com.example.reportgenerator.model.ReferenceRecord;
import com.example.reportgenerator.model.OutputRecord;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvFileProcessor implements FileProcessor {

    @Override
    public List<InputRecord> processInputFile(File file) throws Exception {
        List<InputRecord> inputRecords = new ArrayList<>();
        try (CSVParser csvParser = new CSVParser(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8), CSVFormat.DEFAULT)) {
            for (CSVRecord csvRecord : csvParser) {
                InputRecord record = new InputRecord();
                record.setField1(csvRecord.get(0));
                record.setField2(csvRecord.get(1));
                record.setField3(csvRecord.get(2));
                record.setField5(Double.valueOf(csvRecord.get(4)));
                record.setRefkey1(csvRecord.get(5));
                record.setRefkey2(csvRecord.get(6));
                inputRecords.add(record);
            }
        }
        return inputRecords;
    }

    @Override
    public List<ReferenceRecord> processReferenceFile(File file) throws Exception {
        List<ReferenceRecord> referenceRecords = new ArrayList<>();
        try (CSVParser csvParser = new CSVParser(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8), CSVFormat.DEFAULT)) {
            for (CSVRecord csvRecord : csvParser) {
                ReferenceRecord record = new ReferenceRecord();
                record.setRefkey1(csvRecord.get(0));
                record.setRefdata1(csvRecord.get(1));
                record.setRefkey2(csvRecord.get(2));
                record.setRefdata2(csvRecord.get(3));
                record.setRefdata3(csvRecord.get(4));
                record.setRefdata4(Double.parseDouble(csvRecord.get(5)));
                referenceRecords.add(record);
            }
        }
        return referenceRecords;
    }

    @Override
    public void writeOutputFile(List<OutputRecord> outputRecords, File file) throws Exception {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("outfield1", "outfield2", "outfield3", "outfield4", "outfield5"))) {
            for (OutputRecord outputRecord : outputRecords) {
                csvPrinter.printRecord(outputRecord.getOutfield1(), outputRecord.getOutfield2(), outputRecord.getOutfield3(), outputRecord.getOutfield4(), outputRecord.getOutfield5());
            }
        }
    }
}
