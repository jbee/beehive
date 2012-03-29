package de.jbee.io.json;

/**
 * A processor just focused on the values but not the structure in detail.
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 */
public interface JsonValueProcessor {

	// path ? provide at least some information about the path of the upcoming values ? 
	// array ? --same here ?

	void process( String value );

	void process( Number value );

	void process( boolean value );

	void processNull();
}
