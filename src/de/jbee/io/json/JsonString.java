package de.jbee.io.json;

public final class JsonString
		implements Json {

	/**
	 * The java representation of a {@link JSON} string.
	 */
	private final String value;

	private JsonString( String value ) {
		super();
		this.value = value;
	}

	static Json json( String value ) {
		return value == null
			? JSON.NULL
			: new JsonString( value );
	}

	@Override
	public void pass( JsonTreeVisitor visitor ) {
		visitor.visit( this );
	}

	@Override
	public void passChildren( JsonTreeVisitor visitor ) {
		visitor.process( value ); // we don't escape to JSON-string here. The visitor decides about it because we might want to stay in java world.
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder( value.length() );
		JSON.escape( value, sb );
		return sb.toString();
	}

	@Override
	public int compareTo( Json other ) {
		if ( other.getClass() != JsonString.class ) {
			return -1;
		}
		return ( (JsonString) other ).value.compareTo( value );
	}

	@Override
	public boolean equals( Object obj ) {
		return obj instanceof JsonString && ( (JsonString) obj ).value.equals( value );
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}
}
