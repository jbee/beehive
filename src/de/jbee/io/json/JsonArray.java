package de.jbee.io.json;

import java.util.Arrays;
import java.util.List;

public final class JsonArray
		implements Json {

	private final Json[] elements;

	public JsonArray( List<Json> elements ) {
		this( elements.toArray( new Json[elements.size()] ) );
	}

	public JsonArray( Json... elements ) {
		super();
		this.elements = elements;
	}

	@Override
	public void pass( JsonTreeVisitor visitor ) {
		visitor.visit( this );
	}

	@Override
	public void passChildren( JsonTreeVisitor visitor ) {
		for ( int i = 0; i < elements.length; i++ ) {
			visitor.visitElement( i, elements[i] );
		}
	}

	@Override
	public String toString() {
		return Arrays.toString( elements );
	}

	@Override
	public int compareTo( Json other ) {
		if ( other.getClass() != JsonArray.class ) {
			return -1;
		}
		final Json[] elems = ( (JsonArray) other ).elements;
		if ( elems.length != elements.length ) {
			return Integer.signum( elements.length - elems.length );
		}
		for ( int i = 0; i < elems.length; i++ ) {
			int eCompare = elems[i].compareTo( elements[i] );
			if ( eCompare != 0 ) {
				return eCompare;
			}
		}
		return 0;
	}
}
