package com.example.api.example;

import com.asakusafw.vocabulary.flow.Export;
import com.asakusafw.vocabulary.flow.FlowDescription;
import com.asakusafw.vocabulary.flow.Import;
import com.asakusafw.vocabulary.flow.In;
import com.asakusafw.vocabulary.flow.JobFlow;
import com.asakusafw.vocabulary.flow.Out;
import com.asakusafw.vocabulary.flow.Source;
import com.example.api.example.SeqExampleFromCsv;
import com.example.api.example.SeqExampleToCsv;
import com.example.modelgen.dmdl.model.SeqExample;

/**
 * 
 */
@JobFlow(name = "SequenceExampleJob")
public class SequenceExampleJob extends FlowDescription {

	/**  */
	private final In<SeqExample> seqExampleFromCsv;
	/**  */
	private final Out<SeqExample> seqExampleToCsv;

	/**
	 * 
	 * @param seqExampleFromCsv
	 * @param seqExampleToCsv
	 */
	public SequenceExampleJob(
			@Import(name = "seqExampleFromCsv", description = SeqExampleFromCsv.class) In<SeqExample> seqExampleFromCsv,
			@Export(name = "seqExampleToCsv", description = SeqExampleToCsv.class) Out<SeqExample> seqExampleToCsv) {
		this.seqExampleFromCsv = seqExampleFromCsv;
		this.seqExampleToCsv = seqExampleToCsv;
	}

	@Override
	public void describe() {
		SequenceExampleOperatorFactory operator = new SequenceExampleOperatorFactory();

		Source<SeqExample> s = operator.updateSequenceNumber(seqExampleFromCsv).out;
		seqExampleToCsv.add(s);
	}
}
