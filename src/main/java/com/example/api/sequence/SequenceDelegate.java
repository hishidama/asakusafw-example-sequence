package com.example.api.sequence;

import java.io.Closeable;
import java.io.IOException;
import java.math.BigDecimal;

// スレッドおよびname毎にインスタンスが作られる
public abstract class SequenceDelegate implements Closeable {

	private String name;
	private SequenceAdapter adapter;

	public final void initialize(String name, SequenceAdapter adapter) throws IOException {
		this.name = name;
		this.adapter = adapter;

		doInitialize(adapter);
	}

	protected abstract void doInitialize(SequenceAdapter adapter) throws IOException;

	public final String getName() {
		return name;
	}

	public abstract long nextLong() throws IOException;

	public abstract BigDecimal nextDecimal() throws IOException;

	protected final String getConfig(String part) {
		return adapter.getConfig(name, part);
	}

	protected final String getConfig(String part, String defaultValue) {
		return adapter.getConfig(name, part, defaultValue);
	}
}
