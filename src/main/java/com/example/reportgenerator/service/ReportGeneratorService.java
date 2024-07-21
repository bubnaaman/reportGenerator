package com.example.reportgenerator.service;

import com.example.reportgenerator.model.InputRecord;
import com.example.reportgenerator.model.ReferenceRecord;
import com.example.reportgenerator.model.OutputRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Default transformation rules implementation
 */
@Service
public class ReportGeneratorService implements ReportGenerator {

    private static final Logger logger = LoggerFactory.getLogger(ReportGeneratorService.class);

    /**
     * Generates the report based on the transformation rules.
     * <p>
     * outfield1 =field1+field2
     * outfield2 = refdata1
     * outfield3 = refdata2 + refdata3
     * outfield4 = field3 * max(field5,refdata4)
     * outfield5 = max(field5,refdata4)
     *
     * @param inputRecords     Input records
     * @param referenceRecords Reference records
     * @return List of output records to write in CSV
     */
    @Override
    public List<OutputRecord> generateReport(List<InputRecord> inputRecords, List<ReferenceRecord> referenceRecords) {
        logger.info("Starting report generation with {} input records and {} reference records", inputRecords.size(), referenceRecords.size());

        // Create a map for quick lookup of reference records
        Map<String, ReferenceRecord> referenceMap = createReferenceMap(referenceRecords);

        List<OutputRecord> outputRecords = inputRecords.stream().map(inputRecord -> transformToOutputRecord(inputRecord, referenceMap)).filter(Objects::nonNull).collect(Collectors.toList());

        logger.info("Report generation completed with {} output records", outputRecords.size());
        return outputRecords;
    }

    /**
     * Create a map from reference records for quick lookups.
     *
     * @param referenceRecords List of reference records
     * @return Map with combined key of refkey1 and refkey2
     */
    private Map<String, ReferenceRecord> createReferenceMap(List<ReferenceRecord> referenceRecords) {
        return referenceRecords.stream().collect(Collectors.toMap(ref -> ref.getRefkey1() + "|" + ref.getRefkey2(), Function.identity()));
    }

    /**
     * Transform an input record to an output record based on reference data.
     *
     * @param inputRecord  The input record
     * @param referenceMap Map of reference records
     * @return The transformed output record
     */
    private OutputRecord transformToOutputRecord(InputRecord inputRecord, Map<String, ReferenceRecord> referenceMap) {
        logger.debug("Processing input record: {}", inputRecord);

        String key = inputRecord.getRefkey1() + "|" + inputRecord.getRefkey2();
        ReferenceRecord refRecord = referenceMap.get(key);

        if (refRecord == null) {
            logger.warn("No matching reference record found for input record: {}", inputRecord);
            return null;
        }

        return createOutputRecord(inputRecord, refRecord);
    }

    /**
     * Create an output record based on the input record and reference record.
     *
     * @param inputRecord The input record
     * @param refRecord   The reference record
     * @return The output record
     */
    private OutputRecord createOutputRecord(InputRecord inputRecord, ReferenceRecord refRecord) {
        OutputRecord outputRecord = new OutputRecord();
        outputRecord.setOutfield1(inputRecord.getField1() + inputRecord.getField2());
        outputRecord.setOutfield2(refRecord.getRefdata1());
        outputRecord.setOutfield3(refRecord.getRefdata2() + refRecord.getRefdata3());
        outputRecord.setOutfield4(Double.parseDouble(inputRecord.getField3()) * Math.max(inputRecord.getField5().intValue(), refRecord.getRefdata4().intValue()));
        outputRecord.setOutfield5(Math.max(inputRecord.getField5(), refRecord.getRefdata4()));

        logger.debug("Generated output record: {}", outputRecord);
        return outputRecord;
    }
}
