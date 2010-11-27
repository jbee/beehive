package de.jbee.io.json;

import de.jbee.io.ICharWriter;

public class JsonTreeWriter
		implements IJsonTreeVisitor {

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
	public void visitMember( String name, IJsonValue value ) {
		out.append( name );
		out.append( ':' );
		value.pass( this );
		out.append( ',' );
	}

	@Override
	public void visitElement( int index, IJsonValue value ) {
		if ( index > 0 ) {
			out.append( ',' );
		}
		value.pass( this );
	}

	@Override
	public void visit( String value ) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit( Number value ) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit( boolean value ) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visitNull() {
		// TODO Auto-generated method stub

	}

}
