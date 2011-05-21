package de.jbee.io.json;

import de.jbee.io.IProcessableElement;
import de.jbee.io.IProcessableElement.NoopDiscardingProcessableElement;

public final class JsonProcessor {

	private JsonProcessor() {
		// util
	}

	private static final IProcessableElement<IJsonProcessor> NULL = new ProcessJsonNull();

	public static IProcessableElement<IJsonProcessor> element( String value ) {
		return value == null
			? NULL
			: new ProcessJsonString( value );
	}

	public static IProcessableElement<IJsonProcessor> element( Number value ) {
		return value == null
			? NULL
			: new ProcessJsonNumber( value );
	}

	public static IProcessableElement<IJsonProcessor> element( Boolean value ) {
		return value == null
			? NULL
			: new ProcessJsonBoolean( value );
	}

	public static void processAsElement( IJsonProcessor processor, String name, String value ) {
		processor.process( type( JsonType.STRING, value ), name, element( value ) );
	}

	public static void processAsElement( IJsonProcessor processor, String name, Number value ) {
		processor.process( type( JsonType.NUMBER, value ), name, element( value ) );
	}

	public static void processAsElement( IJsonProcessor processor, String name, Boolean value ) {
		processor.process( type( JsonType.BOOLEAN, value ), name, element( value ) );
	}

	private static JsonType type( JsonType type, Object value ) {
		return value == null
			? JsonType.NULL
			: type;
	}

	static final class ProcessJsonString
			extends NoopDiscardingProcessableElement<IJsonProcessor> {

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
			extends NoopDiscardingProcessableElement<IJsonProcessor> {

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
			extends NoopDiscardingProcessableElement<IJsonProcessor> {

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
			extends NoopDiscardingProcessableElement<IJsonProcessor> {

		@Override
		public void processBy( IJsonProcessor processor ) {
			processor.processNull();
		}
	}

}
