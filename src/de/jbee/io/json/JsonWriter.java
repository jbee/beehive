package de.jbee.io.json;

import de.jbee.io.ICharWriter;
import de.jbee.io.IProcessableUnit;

public class JsonWriter
		implements IJsonProcessor {

	private final ICharWriter out;

	public JsonWriter( ICharWriter out ) {
		super();
		this.out = out;
	}

	@Override
	public void process( String value ) {
		out.append( '"' );
		out.append( JsonString.escape( value ) );
		out.append( '"' );
	}

	@Override
	public void process( Number value ) {
		out.append( new JsonNumber( value ).toString() );
	}

	@Override
	public void process( boolean value ) {
		out.append( JsonBoolean.valueOf( value ).toString() );
	}

	@Override
	public void processNull() {
		out.append( JsonNull.OBJECT.toString() );
	}

	@Override
	public void process( JsonType type, String name, IProcessableUnit<IJsonProcessor> unit ) {
		unit.processBy( this );
	}

}
