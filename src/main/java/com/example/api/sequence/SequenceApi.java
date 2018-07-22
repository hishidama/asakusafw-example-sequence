package com.example.api.sequence;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;

public class SequenceApi {

	public static long nextLong(String name) {
		SequenceDelegate delegate = SequenceAdapter.delegate(name);
		try {
			return delegate.nextLong();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	public static BigDecimal nextDecimal(String name) {
		SequenceDelegate delegate = SequenceAdapter.delegate(name);
		try {
			return delegate.nextDecimal();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
}
