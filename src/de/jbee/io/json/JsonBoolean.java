package de.jbee.io.json;

public final class JsonBoolean
		implements IJson {

	public static final JsonBoolean TRUE = new JsonBoolean();
	public static final JsonBoolean FALSE = new JsonBoolean();

	private JsonBoolean() {
		// hide
	}

	public static JsonBoolean valueOf( Boolean value ) {
		return value == null
			? FALSE
			: valueOf( value.booleanValue() );
	}

	public static JsonBoolean valueOf( boolean value ) {
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
}
