package de.jbee.io.json.map;

import de.jbee.io.IProcessableElement;
import de.jbee.io.IProcessor;
import de.jbee.io.json.IJsonProcessor;
import de.jbee.io.json.JsonType;

public class JsonCreator {

	static final class JsonConverterCreator<T>
			implements IJsonProcessor {

		private final IProcessor<T> processor;
		private final IJsonConverter<T> converter;

		JsonConverterCreator( IProcessor<T> processor, IJsonConverter<T> converter ) {
			super();
			this.processor = processor;
			this.converter = converter;
		}

		@Override
		public void process( JsonType type, String name, IProcessableElement<IJsonProcessor> element ) {
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
