package com.example.api.example;

import com.example.modelgen.dmdl.csv.AbstractSeqExampleCsvOutputDescription;
import java.util.List;

/**
 * 
 */
public class SeqExampleToCsv extends AbstractSeqExampleCsvOutputDescription {

	@Override
	public String getBasePath() {
		return "seq_example/output";
	}

	@Override
	public String getResourcePattern() {
		return "seq_example.csv";
	}

	@Override
	public List<String> getOrder() {
		return super.getOrder();
	}

	@Override
	public List<String> getDeletePatterns() {
		return super.getDeletePatterns();
	}
}
