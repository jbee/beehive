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
		out.appendSequence( JsonString.escape( value ) );
		out.append( '"' );
	}

	@Override
	public void visit( Number value ) {
		out.appendSequence( new JsonNumber( value ).toString() );
	}

	@Override
	public void visit( boolean value ) {
		out.appendSequence( JsonBoolean.valueOf( value ).toString() );
	}

	@Override
	public void visitNull() {
		out.appendSequence( JsonNull.OBJECT.toString() );
	}

	@Override
	public void process( JsonType type, String name, IProcessable<IJsonProcessor> element ) {
		element.processBy( this );
	}

}
