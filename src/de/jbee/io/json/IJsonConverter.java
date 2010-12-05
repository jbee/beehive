package de.jbee.io.json;

public interface IJsonConverter {

	// path ?

	// array ?

	// rename to process ??

	void process( String value );

	void process( Number value );

	void process( boolean value );

	void processNull();
}
