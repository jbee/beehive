package de.jbee.io.json;

import java.util.Arrays;
import java.util.List;

public final class JsonArray
		implements IJson {

	private final IJson[] elements;

	public JsonArray( List<IJson> elements ) {
		this( elements.toArray( new IJson[elements.size()] ) );
	}

	public JsonArray( IJson... elements ) {
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
