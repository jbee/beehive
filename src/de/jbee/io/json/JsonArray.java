package de.jbee.io.json;

import java.util.Arrays;

public final class JsonArray
		implements IJsonValue {

	private final IJsonValue[] elements;

	public JsonArray( IJsonValue... elements ) {
		super();
		this.elements = elements;
	}

	@Override
	public void pass( IJsonTreeVisitor visitor ) {
		visitor.visit( this );
	}

	@Override
	public void passChildren( IJsonTreeVisitor visitor ) {
		for ( int i = 0; i < elements.length; i++ ) {
			visitor.visitElement( i, elements[i] );
		}
	}

	@Override
	public String toString() {
		return Arrays.toString( elements );
	}
}
