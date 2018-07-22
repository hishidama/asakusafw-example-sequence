package com.example.api.example;

import com.example.modelgen.dmdl.csv.AbstractSeqExampleCsvInputDescription;

/**
 * 
 */
public class SeqExampleFromCsv extends AbstractSeqExampleCsvInputDescription {

	@Override
	public String getBasePath() {
		return "seq_example/input";
	}

	@Override
	public String getResourcePattern() {
		return "*.csv";
	}

	@Override
	public boolean isOptional() {
		return false;
	}

	@Override
	public DataSize getDataSize() {
		return DataSize.TINY;
	}
}
