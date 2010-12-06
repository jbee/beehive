package de.jbee.io.json;

public final class JsonNumber
		implements IJson {

	private final Number value;

	private JsonNumber( Number value ) {
		super();
		this.value = value;
	}

	static IJson valueOf( Number value ) {
		return value == null
			? Json.NULL
			: new JsonNumber( value );
	}

	@Override
	public void pass( IJsonTreeVisitor visitor ) {
		visitor.visit( this );
	}

	@Override
	public void passChildren( IJsonTreeVisitor visitor ) {
		visitor.process( value );
	}

	@Override
	public String toString() {
		if ( value instanceof Double ) {
			if ( ( (Double) value ).isInfinite() || ( (Double) value ).isNaN() ) {
				return "null";
			}
		}
		if ( value instanceof Float ) {
			if ( ( (Float) value ).isInfinite() || ( (Float) value ).isNaN() ) {
				return "null";
			}
		}
		return value.toString();
	}

	@Override
	public int compareTo( IJson other ) {
		if ( other.getClass() != JsonNumber.class ) {
			return -1;
		}
		return Double.compare( value.doubleValue(), ( (JsonNumber) other ).value.doubleValue() );
	}

	@Override
	public boolean equals( Object obj ) {
		return obj instanceof JsonNumber && ( (JsonNumber) obj ).value.equals( value );
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}
}
