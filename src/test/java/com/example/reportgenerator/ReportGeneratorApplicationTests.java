// src/test/java/com/example/reportgenerator/ReportGeneratorApplicationTests.java
package com.example.reportgenerator;

import com.example.reportgenerator.model.InputRecord;
import com.example.reportgenerator.model.ReferenceRecord;
import com.example.reportgenerator.service.ReportGeneratorService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ReportGeneratorApplicationTests {

	@Test
	public void testGenerateReport() {
		ReportGeneratorService reportGeneratorService = new ReportGeneratorService();

		List<InputRecord> inputRecords = new ArrayList<>();
		List<ReferenceRecord> referenceRecords = new ArrayList<>();

		InputRecord inputRecord = new InputRecord();
		inputRecord.setField1("field1");
		inputRecord.setField2("field2");
		inputRecord.setField3("123");
		inputRecord.setField5(10.0);
		inputRecord.setRefkey1("key1");
		inputRecord.setRefkey2("key2");
		inputRecords.add(inputRecord);

		ReferenceRecord referenceRecord = new ReferenceRecord();
		referenceRecord.setRefkey1("key1");
		referenceRecord.setRefdata1("refdata1");
		referenceRecord.setRefkey2("key2");
		referenceRecord.setRefdata2("refdata2");
		referenceRecord.setRefdata3("refdata3");
		referenceRecord.setRefdata4(15.0);
		referenceRecords.add(referenceRecord);

		var outputRecords = reportGeneratorService.generateReport(inputRecords, referenceRecords);

		assertEquals(1, outputRecords.size());
		var outputRecord = outputRecords.get(0);
		assertEquals("field1field2", outputRecord.getOutfield1());
		assertEquals("refdata1", outputRecord.getOutfield2());
		assertEquals("refdata2refdata3", outputRecord.getOutfield3());
	}
}
