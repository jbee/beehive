package de.jbee.io.json;

import de.jbee.io.IProcessableUnit;
import de.jbee.io.IProcessableUnit.NoopDiscardingProcessableUnit;

public final class JsonProcessor {

	private JsonProcessor() {
		// util
	}

	private static final IProcessableUnit<IJsonProcessor> NULL = new ProcessJsonNull();

	public static IProcessableUnit<IJsonProcessor> unit( String value ) {
		return value == null
			? NULL
			: new ProcessJsonString( value );
	}

	public static IProcessableUnit<IJsonProcessor> unit( Number value ) {
		return value == null
			? NULL
			: new ProcessJsonNumber( value );
	}

	public static IProcessableUnit<IJsonProcessor> unit( Boolean value ) {
		return value == null
			? NULL
			: new ProcessJsonBoolean( value );
	}

	public static void processAsUnit( IJsonProcessor processor, String name, String value ) {
		processor.process( type( JsonType.STRING, value ), name, unit( value ) );
	}

	public static void processAsUnit( IJsonProcessor processor, String name, Number value ) {
		processor.process( type( JsonType.NUMBER, value ), name, unit( value ) );
	}

	public static void processAsUnit( IJsonProcessor processor, String name, Boolean value ) {
		processor.process( type( JsonType.BOOLEAN, value ), name, unit( value ) );
	}

	private static JsonType type( JsonType type, Object value ) {
		return value == null
			? JsonType.NULL
			: type;
	}

	static final class ProcessJsonString
			extends NoopDiscardingProcessableUnit<IJsonProcessor> {

		private final String value;

		ProcessJsonString( String value ) {
			super();
			this.value = value;
		}

		@Override
		public void processBy( IJsonProcessor processor ) {
			processor.process( value );
		}
	}

	static final class ProcessJsonNumber
			extends NoopDiscardingProcessableUnit<IJsonProcessor> {

		private final Number value;

		ProcessJsonNumber( Number value ) {
			super();
			this.value = value;
		}

		@Override
		public void processBy( IJsonProcessor processor ) {
			processor.process( value );
		}
	}

	static final class ProcessJsonBoolean
			extends NoopDiscardingProcessableUnit<IJsonProcessor> {

		private final Boolean value;

		ProcessJsonBoolean( Boolean value ) {
			super();
			this.value = value;
		}

		@Override
		public void processBy( IJsonProcessor processor ) {
			processor.process( value );
		}
	}

	static final class ProcessJsonNull
			extends NoopDiscardingProcessableUnit<IJsonProcessor> {

		@Override
		public void processBy( IJsonProcessor processor ) {
			processor.processNull();
		}
	}

}
