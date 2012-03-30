package de.jbee.io.json.draft;


public final class JsonConverter {

	private JsonConverter() {
		// util
	}

	static final class StringJsonConverter
			implements IJsonConverter<String> {

		@Override
		public void convert( String value, Processor<String> processor ) {
			processor.process( value );
		}

		@Override
		public void convert( Number value, Processor<String> processor ) {
			processor.process( value.toString() );
		}

		@Override
		public void convert( boolean value, Processor<String> processor ) {
			processor.process( Boolean.toString( value ) );
		}

		@Override
		public void convertNull( Processor<String> processor ) {
			processor.process( null );
		}

	}

	static final class NullDefaultJsonConverter<T>
			implements IJsonConverter<T> {

		private final IJsonConverter<T> converter;
		private final T nullDefault;

		NullDefaultJsonConverter( IJsonConverter<T> converter, T nullDefault ) {
			super();
			this.converter = converter;
			this.nullDefault = nullDefault;
		}

		@Override
		public void convert( String value, Processor<T> processor ) {
			converter.convert( value, processor );
		}

		@Override
		public void convert( Number value, Processor<T> processor ) {
			converter.convert( value, processor );
		}

		@Override
		public void convert( boolean value, Processor<T> processor ) {
			converter.convert( value, processor );
		}

		@Override
		public void convertNull( Processor<T> processor ) {
			processor.process( nullDefault );
		}

	}
}
