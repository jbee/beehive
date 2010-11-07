package de.jbee.io.json;

public final class ObjectPath {

	public static final ObjectPath NONE = new ObjectPath( "" );

	private final String value;

	private ObjectPath( String value ) {
		super();
		this.value = value;
	}

	public static ObjectPath of( String property ) {
		return property == null || property.length() == 0
			? NONE
			: new ObjectPath( property );
	}

	public boolean hasParent() {
		return value.indexOf( '.' ) < 0;
	}

	public boolean isNone() {
		return this == NONE;
	}

	public ObjectPath parent() {
		return new ObjectPath( value.substring( value.indexOf( '.' ) + 1 ) );
	}

	public ObjectPath member( String name ) {
		return new ObjectPath( value + "." + name );
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	@Override
	public boolean equals( Object obj ) {
		if ( obj instanceof ObjectPath ) {
			return ( (ObjectPath) obj ).value.equals( value );
		}
		return false;
	}

	@Override
	public String toString() {
		return value;
	}
}
