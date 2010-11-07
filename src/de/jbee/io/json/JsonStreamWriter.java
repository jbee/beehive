package de.jbee.io.json;

import de.jbee.io.ICharWriter;

public class JsonStreamWriter
		implements IJsonProcessor {

	private final ICharWriter out;

	public JsonStreamWriter( ICharWriter out ) {
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
	public IJsonProcessor begin( JsonType member, String name ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IJsonProcessor end( JsonType element ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean skip( JsonType element ) {
		// TODO Auto-generated method stub
		return false;
	}

}
