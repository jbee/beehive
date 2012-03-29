package de.jbee.io.json;

public final class JsonNull
		implements Json {

	static final JsonNull OBJECT = new JsonNull();

	private JsonNull() {
		// singleton
	}

	@Override
	public void pass( JsonTreeVisitor visitor ) {
		visitor.visit( this );
	}

	@Override
	public void passChildren( JsonTreeVisitor visitor ) {
		visitor.processNull();
	}

	@Override
	public String toString() {
		return "null";
	}

	@Override
	public boolean equals( Object obj ) {
		return obj.getClass() == JsonNull.class;
	}

	@Override
	public int hashCode() {
		return JsonNull.class.hashCode();
	}

	@Override
	public int compareTo( Json other ) {
		return other.getClass() != JsonNull.class
			? -1
			: 0;
	}
}
