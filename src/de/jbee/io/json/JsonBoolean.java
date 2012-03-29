package de.jbee.io.json;

public final class JsonBoolean
		implements Json {

	public static final JsonBoolean TRUE = new JsonBoolean();
	public static final JsonBoolean FALSE = new JsonBoolean();

	private JsonBoolean() {
		// hide
	}

	static Json json( Boolean value ) {
		return value == null
			? JSON.NULL
			: json( value.booleanValue() );
	}

	static Json json( boolean value ) {
		return value
			? TRUE
			: FALSE;
	}

	@Override
	public void pass( JsonTreeVisitor visitor ) {
		visitor.visit( this );
	}

	@Override
	public void passChildren( JsonTreeVisitor visitor ) {
		visitor.process( booleanValue() );
	}

	public boolean booleanValue() {
		return this == TRUE;
	}

	@Override
	public String toString() {
		return booleanValue()
			? "true"
			: "false";
	}

	@Override
	public int compareTo( Json other ) {
		if ( other.getClass() != JsonBoolean.class ) {
			return -1;
		}
		return Boolean.valueOf( booleanValue() ).compareTo( ( (JsonBoolean) other ).booleanValue() );
	}
}
