package de.jbee.io.json;

import de.jbee.io.ICharWriter;
import de.jbee.io.IProcessable;

public class JsonWriter
		implements IJsonProcessor {

	private final ICharWriter out;

	public JsonWriter( ICharWriter out ) {
		super();
		this.out = out;
	}

	@Override
	public void visit( String value ) {
		out.append( '"' );
		out.append( JsonString.escape( value ) );
		out.append( '"' );
	}

	@Override
	public void visit( Number value ) {
		out.append( new JsonNumber( value ).toString() );
	}

	@Override
	public void visit( boolean value ) {
		out.append( JsonBoolean.valueOf( value ).toString() );
	}

	@Override
	public void visitNull() {
		out.append( JsonNull.OBJECT.toString() );
	}

	@Override
	public void process( JsonType type, String name, IProcessable<IJsonProcessor> element ) {
		element.processBy( this );
	}

}
