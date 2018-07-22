package com.example.api.sequence;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

import com.asakusafw.bridge.broker.ResourceBroker;
import com.asakusafw.bridge.broker.ResourceCacheStorage;
import com.asakusafw.runtime.core.ResourceConfiguration;

/**
 * @see com.asakusafw.bridge.api.ReportAdapter
 */
public final class SequenceAdapter implements Closeable {

	private static final String CONFIG_DELIM = ".";
	private static final String CONFIG_PREFIX = "sequence" + CONFIG_DELIM;

	private static final ResourceCacheStorage<SequenceAdapter> CACHE = new ResourceCacheStorage<>();

	private static final Callable<SequenceAdapter> SUPPLIER = () -> {
		ResourceConfiguration conf = ResourceBroker.find(ResourceConfiguration.class);
		if (conf == null) {
			throw new AssertionError("not found ResourceConfiguration");
		}

		return new SequenceAdapter(conf);
	};

	// スレッド毎に異なるインスタンスを返すので、戻り値をフィールドに保持しないこと
	public static SequenceDelegate delegate(String name) {
		assert name != null;

		// SequenceAdapterはスレッドごとに1インスタンス（CACHEの機能）
		// ResourceBrokerを経由しておくと、バッチ終了時にcloseメソッドが呼ばれる
		SequenceAdapter adapter = CACHE.get(() -> ResourceBroker.get(SequenceAdapter.class, SUPPLIER));

		return adapter.getDelegate(name);
	}

	private final ResourceConfiguration configuration;

	private final Map<String, SequenceDelegate> delegateMap = new HashMap<>();
	private final Map<Class<?>, Closeable> closeableMap = new HashMap<>();

	SequenceAdapter(ResourceConfiguration configuration) {
		this.configuration = configuration;
	}

	private SequenceDelegate getDelegate(String name) {
		SequenceDelegate delegate = delegateMap.get(name);
		if (delegate == null) {
			String className = getConfig(name, "class");
			try {
				Class<?> clazz = configuration.getClassLoader().loadClass(className);
				Constructor<? extends SequenceDelegate> constructor = clazz.asSubclass(SequenceDelegate.class)
						.getConstructor();
				delegate = constructor.newInstance();
				delegate.initialize(name, this);
			} catch (Exception e) {
				throw new IllegalStateException(MessageFormat.format("name={0}, class={1}", name, className), e);
			}
			delegateMap.put(name, delegate);
		}
		return delegate;
	}

	private static final String ERROR_VALUE = new String();

	public String getConfig(String name, String part) {
		return getConfig(name, part, ERROR_VALUE);
	}

	public String getConfig(String name, String part, String defaultValue) {
		String key0 = null;

		for (;;) {
			String key = CONFIG_PREFIX + name + CONFIG_DELIM + part;
			if (key0 == null) {
				key0 = key;
			}

			String s = configuration.get(key, null);
			if (s != null) {
				return s;
			}

			int n = name.lastIndexOf('.');
			if (n < 0) {
				break;
			}
			name = name.substring(0, n);
		}

		String key = CONFIG_PREFIX + part;
		String s = configuration.get(key, null);
		if (s != null) {
			return s;
		}
		if (defaultValue != ERROR_VALUE) {
			return defaultValue;
		}
		throw new IllegalArgumentException(MessageFormat.format("not found configuration. key={0}", key0));
	}

	public <T extends Closeable> T getInstance(Class<T> aClass, Supplier<T> supplier) {
		@SuppressWarnings("unchecked")
		T closeable = (T) closeableMap.get(aClass);
		if (closeable == null) {
			closeable = supplier.get();
			if (closeable == null) {
				throw new IllegalStateException(aClass.getName());
			}
			closeableMap.put(aClass, closeable);
		}
		return closeable;
	}

	@Override
	public void close() throws IOException {
		IOException exception = null;

		for (SequenceDelegate delegate : delegateMap.values()) {
			try {
				delegate.close();
			} catch (IOException e) {
				if (exception == null) {
					exception = e;
				} else {
					exception.addSuppressed(e);
				}
			}
		}
		delegateMap.clear();

		for (Closeable closeable : closeableMap.values()) {
			try {
				closeable.close();
			} catch (IOException e) {
				if (exception == null) {
					exception = e;
				} else {
					exception.addSuppressed(e);
				}
			}
		}
		closeableMap.clear();

		if (exception != null) {
			throw exception;
		}
	}
}
