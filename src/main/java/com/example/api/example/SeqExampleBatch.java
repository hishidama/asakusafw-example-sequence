package com.example.api.example;

import com.asakusafw.vocabulary.batch.Batch;
import com.asakusafw.vocabulary.batch.BatchDescription;
import com.example.api.example.SequenceExampleJob;

@Batch(name = "SeqExampleBatch", comment = "", strict = false)
public class SeqExampleBatch extends BatchDescription {

	@Override
	public void describe() {
		run(SequenceExampleJob.class).soon();
	}
}
