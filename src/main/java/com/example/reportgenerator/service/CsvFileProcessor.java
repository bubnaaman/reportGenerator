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

/**
 * Process the CSV files
 */
@Service
public class CsvFileProcessor implements FileProcessor {

    /**
     * Logger
     */
    private static final Logger logger =
            LoggerFactory.getLogger(CsvFileProcessor.class);

    /**
     * Reads from CSV input file.
     * @param aInFile Input csv file
     * @return List of input records
     * @throws Exception if any
     */
    @Override
    public List<InputRecord> processInputFile(File aInFile) throws Exception
    {
        logger.info("Processing input file: {}", aInFile.getAbsolutePath());
        List<InputRecord> lInputRecords = new ArrayList<>();
        try (CSVParser lCSVParser = new CSVParser(new InputStreamReader
                (new FileInputStream(aInFile), StandardCharsets.UTF_8),
                CSVFormat.DEFAULT))
        {
            for (CSVRecord csvRecord : lCSVParser)
            {
                InputRecord lRecord = new InputRecord();
                lRecord.setField1(csvRecord.get(0));
                lRecord.setField2(csvRecord.get(1));
                lRecord.setField3(csvRecord.get(2));
                lRecord.setField5(Double.valueOf(csvRecord.get(4)));
                lRecord.setRefkey1(csvRecord.get(5));
                lRecord.setRefkey2(csvRecord.get(6));
                lInputRecords.add(lRecord);
                logger.debug("Processed input record: {}", lRecord);
            }
        } catch (Exception aInException)
        {
            logger.error("Error processing input file: {}",
                    aInFile.getAbsolutePath(), aInException);
            throw aInException;
        }
        logger.info("Finished processing input file. Number of records: {}",
                lInputRecords.size());
        return lInputRecords;
    }

    /**
     * Reads from the CSV Reference file
     * @param aInFile Reference file
     * @return Reference records
     * @throws Exception if any
     */
    @Override
    public List<ReferenceRecord> processReferenceFile(File aInFile) throws Exception
    {
        logger.info("Processing reference file: {}", aInFile.getAbsolutePath());
        List<ReferenceRecord> lReferenceRecords = new ArrayList<>();
        try (CSVParser lCSVParser = new CSVParser(new InputStreamReader
                (new FileInputStream(aInFile), StandardCharsets.UTF_8),
                CSVFormat.DEFAULT))
        {
            for (CSVRecord lCSVRecord : lCSVParser)
            {
                ReferenceRecord lRecord = new ReferenceRecord();
                lRecord.setRefkey1(lCSVRecord.get(0));
                lRecord.setRefdata1(lCSVRecord.get(1));
                lRecord.setRefkey2(lCSVRecord.get(2));
                lRecord.setRefdata2(lCSVRecord.get(3));
                lRecord.setRefdata3(lCSVRecord.get(4));
                lRecord.setRefdata4(Double.parseDouble(lCSVRecord.get(5)));
                lReferenceRecords.add(lRecord);
                logger.debug("Processed reference record: {}", lRecord);
            }
        } catch (Exception aInException)
        {
            logger.error("Error processing reference file: {}",
                    aInFile.getAbsolutePath(), aInException);
            throw aInException;
        }
        logger.info("Finished processing reference file. Number of records: {}",
                lReferenceRecords.size());
        return lReferenceRecords;
    }

    /**
     * Write to output CSV file
     * @param aOutRecords Records to write in csv file.
     * @param aInFile Output CSV file
     * @throws Exception if any
     */
    @Override
    public void writeOutputFile(List<OutputRecord> aOutRecords, File aInFile)
            throws Exception
    {
        logger.info("Writing output file: {}", aInFile.getAbsolutePath());
        try (BufferedWriter lWriter = new BufferedWriter(new OutputStreamWriter
                (new FileOutputStream(aInFile), StandardCharsets.UTF_8));
             CSVPrinter lCSVPrinter = new CSVPrinter(lWriter,
                     CSVFormat.DEFAULT.withHeader("outfield1", "outfield2",
                             "outfield3", "outfield4", "outfield5")))
        {
            for (OutputRecord lOutputRecord : aOutRecords) {
                lCSVPrinter.printRecord(lOutputRecord.getOutfield1(),
                        lOutputRecord.getOutfield2(),
                        lOutputRecord.getOutfield3(),
                        lOutputRecord.getOutfield4(),
                        lOutputRecord.getOutfield5());
                logger.debug("Written output record: {}", lOutputRecord);
            }
        } catch (Exception aInException) {
            logger.error("Error writing output file: {}",
                    aInFile.getAbsolutePath(), aInException);
            throw aInException;
        }
        logger.info("Finished writing output file. Number of records: {}",
                aOutRecords.size());
    }
}