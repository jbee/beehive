package de.jbee.io.json;

public final class JsonNumber
		implements IJson {

	private final Number value;

	public JsonNumber( Number value ) {
		super();
		this.value = value;
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
}
