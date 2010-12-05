package de.jbee.io.json;

public final class JsonNull
		implements IJson {

	public static final JsonNull OBJECT = new JsonNull();

	private JsonNull() {
		// singleton
	}

	@Override
	public void pass( IJsonTreeVisitor visitor ) {
		visitor.visit( this );
	}

	@Override
	public void passChildren( IJsonTreeVisitor visitor ) {
		visitor.processNull();
	}

	@Override
	public String toString() {
		return "null";
	}
}
