// src/main/java/com/example/reportgenerator/service/ReportGeneratorService.java
package com.example.reportgenerator.service;

import com.example.reportgenerator.model.InputRecord;
import com.example.reportgenerator.model.ReferenceRecord;
import com.example.reportgenerator.model.OutputRecord;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportGeneratorService {

    public List<OutputRecord> generateReport(List<InputRecord> inputRecords, List<ReferenceRecord> referenceRecords) {
        return inputRecords.stream().map(inputRecord -> {
            ReferenceRecord refRecord = referenceRecords.stream()
                    .filter(ref -> ref.getRefkey1().equals(inputRecord.getRefkey1()) && ref.getRefkey2().equals(inputRecord.getRefkey2()))
                    .findFirst().orElse(null);
            if (refRecord == null) return null;

            OutputRecord outputRecord = new OutputRecord();
            outputRecord.setOutfield1(inputRecord.getField1() + inputRecord.getField2());
            outputRecord.setOutfield2(refRecord.getRefdata1());
            outputRecord.setOutfield3(refRecord.getRefdata2() + refRecord.getRefdata3());
            outputRecord.setOutfield4(Integer.parseInt(inputRecord.getField3()) * (Math.max(inputRecord.getField5(),refRecord.getRefdata4())));
            outputRecord.setOutfield5(Math.max(inputRecord.getField5(),refRecord.getRefdata4()));
            return outputRecord;
        }).collect(Collectors.toList());
    }
}
