package de.jbee.io.property;

public final class ObjectPath {

	public static final ObjectPath NONE = new ObjectPath( "" );

	private final String value;

	private ObjectPath( String value ) {
		super();
		this.value = value;
	}

	public static ObjectPath path( String path ) {
		return path == null || path.length() == 0
			? NONE
			: new ObjectPath( path );
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

	public ObjectPath child( String name ) {
		return name == null || name.isEmpty()
			? this
			: new ObjectPath( value.isEmpty()
				? name
				: value + "." + name );
	}

	public boolean isSubElementOf( ObjectPath other ) {
		return value.startsWith( other.value );
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
