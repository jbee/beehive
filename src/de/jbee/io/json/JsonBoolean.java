package de.jbee.io.json;

public final class JsonBoolean
		implements IJson {

	public static final JsonBoolean TRUE = new JsonBoolean();
	public static final JsonBoolean FALSE = new JsonBoolean();

	private JsonBoolean() {
		// hide
	}

	static IJson valueOf( Boolean value ) {
		return value == null
			? Json.NULL
			: valueOf( value.booleanValue() );
	}

	static IJson valueOf( boolean value ) {
		return value
			? TRUE
			: FALSE;
	}

	@Override
	public void pass( IJsonTreeVisitor visitor ) {
		visitor.visit( this );
	}

	@Override
	public void passChildren( IJsonTreeVisitor visitor ) {
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
	public int compareTo( IJson other ) {
		if ( other.getClass() != JsonBoolean.class ) {
			return -1;
		}
		return Boolean.valueOf( booleanValue() ).compareTo( ( (JsonBoolean) other ).booleanValue() );
	}
}
