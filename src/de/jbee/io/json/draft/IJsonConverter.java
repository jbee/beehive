package de.jbee.io.json.draft;


public interface IJsonConverter<T> {

	void convert( String value, Processor<T> processor );

	void convert( Number value, Processor<T> processor );

	void convert( boolean value, Processor<T> processor );

	void convertNull( Processor<T> processor );
}
