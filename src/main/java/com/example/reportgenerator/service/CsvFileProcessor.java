package com.example.reportgenerator.service;

import com.example.reportgenerator.model.InputRecord;
import com.example.reportgenerator.model.ReferenceRecord;
import com.example.reportgenerator.model.OutputRecord;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvFileProcessor implements FileProcessor {

    private static final Logger logger = LoggerFactory.getLogger(CsvFileProcessor.class);

    @Override
    public List<InputRecord> processInputFile(File file) throws Exception {
        logger.info("Processing input file: {}", file.getAbsolutePath());
        List<InputRecord> inputRecords = new ArrayList<>();
        try (CSVParser csvParser = new CSVParser(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8), CSVFormat.DEFAULT)) {
            for (CSVRecord csvRecord : csvParser) {
                InputRecord lRecord = new InputRecord();
                lRecord.setField1(csvRecord.get(0));
                lRecord.setField2(csvRecord.get(1));
                lRecord.setField3(csvRecord.get(2));
                lRecord.setField5(Double.valueOf(csvRecord.get(4)));
                lRecord.setRefkey1(csvRecord.get(5));
                lRecord.setRefkey2(csvRecord.get(6));
                inputRecords.add(lRecord);
                logger.debug("Processed input record: {}", lRecord);
            }
        } catch (IOException e) {
            logger.error("Error processing input file: {}", file.getAbsolutePath(), e);
            throw e;
        }
        logger.info("Finished processing input file. Number of records: {}", inputRecords.size());
        return inputRecords;
    }

    @Override
    public List<ReferenceRecord> processReferenceFile(File file) throws Exception {
        logger.info("Processing reference file: {}", file.getAbsolutePath());
        List<ReferenceRecord> referenceRecords = new ArrayList<>();
        try (CSVParser csvParser = new CSVParser(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8), CSVFormat.DEFAULT)) {
            for (CSVRecord csvRecord : csvParser) {
                ReferenceRecord lRecord = new ReferenceRecord();
                lRecord.setRefkey1(csvRecord.get(0));
                lRecord.setRefdata1(csvRecord.get(1));
                lRecord.setRefkey2(csvRecord.get(2));
                lRecord.setRefdata2(csvRecord.get(3));
                lRecord.setRefdata3(csvRecord.get(4));
                lRecord.setRefdata4(Double.parseDouble(csvRecord.get(5)));
                referenceRecords.add(lRecord);
                logger.debug("Processed reference record: {}", lRecord);
            }
        } catch (IOException e) {
            logger.error("Error processing reference file: {}", file.getAbsolutePath(), e);
            throw e;
        }
        logger.info("Finished processing reference file. Number of records: {}", referenceRecords.size());
        return referenceRecords;
    }

    @Override
    public void writeOutputFile(List<OutputRecord> outputRecords, File file) throws Exception {
        logger.info("Writing output file: {}", file.getAbsolutePath());
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("outfield1", "outfield2", "outfield3", "outfield4", "outfield5"))) {
            for (OutputRecord outputRecord : outputRecords) {
                csvPrinter.printRecord(outputRecord.getOutfield1(), outputRecord.getOutfield2(), outputRecord.getOutfield3(), outputRecord.getOutfield4(), outputRecord.getOutfield5());
                logger.debug("Written output record: {}", outputRecord);
            }
        } catch (Exception aInException) {
            logger.error("Error writing output file: {}", file.getAbsolutePath(), aInException);
            throw aInException;
        }
        logger.info("Finished writing output file. Number of records: {}", outputRecords.size());
    }
}
