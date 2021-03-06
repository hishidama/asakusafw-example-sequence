package com.example.api.example;

import com.asakusafw.testdriver.FlowPartTester;
import com.asakusafw.testdriver.core.PropertyName;
import com.asakusafw.vocabulary.flow.FlowDescription;
import com.asakusafw.vocabulary.flow.In;
import com.asakusafw.vocabulary.flow.Out;
import com.example.api.example.SequenceExampleJob;
import com.example.modelgen.dmdl.model.SeqExample;
import org.junit.Test;

/**
 * {@link SequenceExampleJob}のテスト。
 */
public class SequenceExampleJobTest {

	static {
		System.setProperty(PropertyName.KEY_SEGMENT_SEPARATOR, "_");
	}

	@Test
	public void describe() {
		FlowPartTester tester = new FlowPartTester(getClass());
		// TODO tester.setBatchArg("arg", "value");

		In<SeqExample> seqExampleFromCsv = tester.input("seqExampleFromCsv", SeqExample.class).prepare(
				"SequenceExampleJobTest.xls#seqExampleFromCsv");
		Out<SeqExample> seqExampleToCsv = tester.output("seqExampleToCsv", SeqExample.class).verify(
				"SequenceExampleJobTest.xls#seqExampleToCsv", "SequenceExampleJobTest.xls#seqExampleToCsv_rule");

		FlowDescription flow = new SequenceExampleJob(seqExampleFromCsv, seqExampleToCsv);
		tester.runTest(flow);
	}
}
