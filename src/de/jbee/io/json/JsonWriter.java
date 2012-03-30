package de.jbee.io.json;

import de.jbee.io.CharWriter;
import de.jbee.io.ProcessableBy;

public class JsonWriter
		implements JsonProcessor {

	private final CharWriter out;

	public JsonWriter( CharWriter out ) {
		super();
		this.out = out;
	}

	@Override
	public void process( String value ) {
		out.append( '"' );
		out.append( JSON.escape( value ) );
		out.append( '"' );
	}

	@Override
	public void process( Number value ) {
		out.append( JSON.json( value ).toString() );
	}

	@Override
	public void process( boolean value ) {
		out.append( JsonBoolean.json( value ).toString() );
	}

	@Override
	public void processNull() {
		out.append( JsonNull.OBJECT.toString() );
	}

	@Override
	public void process( JsonType type, String name, ProcessableBy<JsonProcessor> element ) {
		element.processBy( this );
	}

}
