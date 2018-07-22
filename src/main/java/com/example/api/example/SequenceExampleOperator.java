package com.example.api.example;

import com.asakusafw.vocabulary.operator.Update;
import com.example.api.sequence.SequenceApi;
import com.example.modelgen.dmdl.model.SeqExample;

public abstract class SequenceExampleOperator {

	@Update
	public void updateSequenceNumber(SeqExample in) {
		long number = SequenceApi.nextLong("seqTest");
		in.setSeqNumber(number);
	}
}
