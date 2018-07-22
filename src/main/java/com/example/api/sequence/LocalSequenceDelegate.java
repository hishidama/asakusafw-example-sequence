package com.example.api.sequence;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class LocalSequenceDelegate extends SequenceDelegate {

	private static final ConcurrentHashMap<String, AtomicLong> COUNTER_MAP = new ConcurrentHashMap<>();

	private AtomicLong counter;

	@Override
	protected void doInitialize(SequenceAdapter adapter) throws IOException {
		this.counter = COUNTER_MAP.computeIfAbsent(getName(), key -> new AtomicLong());
	}

	@Override
	public long nextLong() throws IOException {
		if (counter == null) {
			throw new IllegalStateException("counter already closed");
		}
		return counter.addAndGet(1);
	}

	@Override
	public BigDecimal nextDecimal() throws IOException {
		return BigDecimal.valueOf(nextLong());
	}

	@Override
	public void close() throws IOException {
		this.counter = null;

		// スレッド違いで何度も呼ばれるが、removeは何度実行しても構わないだろう
		COUNTER_MAP.remove(getName());
	}
}
