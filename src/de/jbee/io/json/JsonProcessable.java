package de.jbee.io.json;

import de.jbee.io.ProcessableBy;
import de.jbee.io.ProcessableBy.NoopDiscardingProcessableBy;

public final class JsonProcessable {

	private JsonProcessable() {
		// util
	}

	private static final ProcessableBy<JsonProcessor> NULL = new ProcessJsonNull();

	public static ProcessableBy<JsonProcessor> element( String value ) {
		return value == null
			? NULL
			: new ProcessJsonString( value );
	}

	public static ProcessableBy<JsonProcessor> element( Number value ) {
		return value == null
			? NULL
			: new ProcessJsonNumber( value );
	}

	public static ProcessableBy<JsonProcessor> element( Boolean value ) {
		return value == null
			? NULL
			: new ProcessJsonBoolean( value );
	}

	public static void processAsElement( JsonProcessor processor, String name, String value ) {
		processor.process( type( JsonType.STRING, value ), name, element( value ) );
	}

	public static void processAsElement( JsonProcessor processor, String name, Number value ) {
		processor.process( type( JsonType.NUMBER, value ), name, element( value ) );
	}

	public static void processAsElement( JsonProcessor processor, String name, Boolean value ) {
		processor.process( type( JsonType.BOOLEAN, value ), name, element( value ) );
	}

	private static JsonType type( JsonType type, Object value ) {
		return value == null
			? JsonType.NULL
			: type;
	}

	static final class ProcessJsonString
			extends NoopDiscardingProcessableBy<JsonProcessor> {

		private final String value;

		ProcessJsonString( String value ) {
			super();
			this.value = value;
		}

		@Override
		public void processBy( JsonProcessor processor ) {
			processor.process( value );
		}
	}

	static final class ProcessJsonNumber
			extends NoopDiscardingProcessableBy<JsonProcessor> {

		private final Number value;

		ProcessJsonNumber( Number value ) {
			super();
			this.value = value;
		}

		@Override
		public void processBy( JsonProcessor processor ) {
			processor.process( value );
		}
	}

	static final class ProcessJsonBoolean
			extends NoopDiscardingProcessableBy<JsonProcessor> {

		private final Boolean value;

		ProcessJsonBoolean( Boolean value ) {
			super();
			this.value = value;
		}

		@Override
		public void processBy( JsonProcessor processor ) {
			processor.process( value );
		}
	}

	static final class ProcessJsonNull
			extends NoopDiscardingProcessableBy<JsonProcessor> {

		@Override
		public void processBy( JsonProcessor processor ) {
			processor.processNull();
		}
	}

}
