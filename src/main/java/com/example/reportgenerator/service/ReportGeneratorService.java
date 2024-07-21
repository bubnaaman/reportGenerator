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

    private static final Logger logger =
            LoggerFactory.getLogger(ReportGeneratorService.class);

    /**
     * Generates the report based on the transformation rules.
     * <p>
     * outfield1 =field1+field2
     * outfield2 = refdata1
     * outfield3 = refdata2 + refdata3
     * outfield4 = field3 * max(field5,refdata4)
     * outfield5 = max(field5,refdata4)
     *
     * @param aInInputRecords     Input records
     * @param aInReferenceRecords Reference records
     * @return List of output records to write in CSV
     */
    @Override
    public List<OutputRecord> generateReport
    (List<InputRecord> aInInputRecords, List<ReferenceRecord> aInReferenceRecords)
    {
        logger.info("Starting report generation with {} input records and {}" +
                " reference records", aInInputRecords.size(),
                aInReferenceRecords.size());

        Map<String, ReferenceRecord> lReferenceMap
                = createReferenceMap(aInReferenceRecords);

        List<OutputRecord> aOutRecords = aInInputRecords.stream()
                .map(inputRecord -> transformToOutputRecord(inputRecord,
                        lReferenceMap)).filter(Objects::nonNull)
                .collect(Collectors.toList());

        logger.info("Report generation completed with {} output records",
                aOutRecords.size());
        return aOutRecords;
    }

    /**
     * Create a map from reference records for quick lookups.
     *
     * @param aInReferenceRecords List of reference records
     * @return Map with combined key of refkey1 and refkey2
     */
    private Map<String, ReferenceRecord> createReferenceMap
    (List<ReferenceRecord> aInReferenceRecords)
    {
        return aInReferenceRecords.stream().collect(Collectors.toMap
                (ref -> ref.getRefkey1() + "|" + ref.getRefkey2(),
                        Function.identity()));
    }

    /**
     * Transform an input record to an output record based on reference data.
     *
     * @param aInInputRecord  The input record
     * @param aInReferenceMap Map of reference records
     * @return The transformed output record
     */
    private OutputRecord transformToOutputRecord
    (InputRecord aInInputRecord, Map<String, ReferenceRecord> aInReferenceMap)
    {
        logger.debug("Processing input record: {}", aInInputRecord);

        String lKey = aInInputRecord.getRefkey1() + "|" +
                aInInputRecord.getRefkey2();
        ReferenceRecord lRefRecord = aInReferenceMap.get(lKey);

        if (lRefRecord == null) {
            logger.warn("No matching reference record found for input record:" +
                    " {}", aInInputRecord);
            return null;
        }

        return createOutputRecord(aInInputRecord, lRefRecord);
    }

    /**
     * Create an output record based on the input record and reference record.
     *
     * @param aInInputRecord The input record
     * @param aInReferenceRecord   The reference record
     * @return The output record
     */
    private OutputRecord createOutputRecord(InputRecord aInInputRecord,
                                            ReferenceRecord aInReferenceRecord)
    {
        OutputRecord lOutputRecord = new OutputRecord();
        lOutputRecord.setOutfield1(aInInputRecord.getField1()
                + aInInputRecord.getField2());
        lOutputRecord.setOutfield2(aInReferenceRecord.getRefdata1());
        lOutputRecord.setOutfield3(aInReferenceRecord.getRefdata2()
                + aInReferenceRecord.getRefdata3());
        lOutputRecord.setOutfield4(Double.parseDouble(aInInputRecord.getField3())
                * Math.max(aInInputRecord.getField5().intValue(),
                aInReferenceRecord.getRefdata4().intValue()));
        lOutputRecord.setOutfield5(Math.max(aInInputRecord.getField5(),
                aInReferenceRecord.getRefdata4()));

        logger.debug("Generated output record: {}", lOutputRecord);
        return lOutputRecord;
    }
}