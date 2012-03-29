package de.jbee.io.json;

import de.jbee.io.ICharWriter;

public class JsonTreeWriter
		implements JsonTreeVisitor {

	private final ICharWriter out;

	private JsonTreeWriter( ICharWriter out ) {
		super();
		this.out = out;
	}

	@Override
	public void visit( JsonObject value ) {
		out.append( '{' );
		value.passChildren( this );
		out.append( '}' );
	}

	@Override
	public void visit( JsonArray value ) {
		out.append( '[' );
		value.passChildren( this );
		out.append( ']' );
	}

	@Override
	public void visit( JsonBoolean value ) {
		out.append( value.toString() );
	}

	@Override
	public void visit( JsonNull value ) {
		out.append( value.toString() );
	}

	@Override
	public void visit( JsonNumber value ) {
		out.append( value.toString() );
	}

	@Override
	public void visit( JsonString value ) {
		out.append( value.toString() );
	}

	@Override
	public void visitMember( String name, Json value ) {
		out.append( name );
		out.append( ':' );
		value.pass( this );
		out.append( ',' );
	}

	@Override
	public void visitElement( int index, Json value ) {
		if ( index > 0 ) {
			out.append( ',' );
		}
		value.pass( this );
	}

	@Override
	public void process( String value ) {
		// TODO Auto-generated method stub

	}

	@Override
	public void process( Number value ) {
		// TODO Auto-generated method stub

	}

	@Override
	public void process( boolean value ) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processNull() {
		// TODO Auto-generated method stub

	}

}
