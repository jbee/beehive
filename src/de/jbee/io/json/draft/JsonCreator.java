package de.jbee.io.json.draft;

import de.jbee.io.ProcessableBy;
import de.jbee.io.json.JsonProcessor;
import de.jbee.io.json.JsonType;

public class JsonCreator {

	static final class JsonConverterCreator<T>
			implements JsonProcessor {

		private final Processor<T> processor;
		private final IJsonConverter<T> converter;

		JsonConverterCreator( Processor<T> processor, IJsonConverter<T> converter ) {
			super();
			this.processor = processor;
			this.converter = converter;
		}

		@Override
		public void process( JsonType type, String name, ProcessableBy<JsonProcessor> element ) {
			// TODO Auto-generated method stub

		}

		@Override
		public void process( String value ) {
			converter.convert( value, processor );
		}

		@Override
		public void process( Number value ) {
			converter.convert( value, processor );
		}

		@Override
		public void process( boolean value ) {
			converter.convert( value, processor );
		}

		@Override
		public void processNull() {
			converter.convertNull( processor );
		}

	}
}
