package de.jbee.io.property;

public final class PropertyPath {

	public static final PropertyPath NONE = new PropertyPath( "" );

	private final String value;

	private PropertyPath( String value ) {
		super();
		this.value = value;
	}

	public static PropertyPath of( String property ) {
		return property == null || property.length() == 0
			? NONE
			: new PropertyPath( property );
	}

	public boolean hasParent() {
		return value.indexOf( '.' ) < 0;
	}

	public boolean isNone() {
		return this == NONE;
	}

	public PropertyPath parent() {
		return new PropertyPath( value.substring( value.indexOf( '.' ) + 1 ) );
	}

	public PropertyPath child( String name ) {
		return name == null || name.isEmpty()
			? this
			: new PropertyPath( value.isEmpty()
				? name
				: value + "." + name );
	}

	public boolean isSubElementOf( PropertyPath other ) {
		return value.startsWith( other.value );
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	@Override
	public boolean equals( Object obj ) {
		if ( obj instanceof PropertyPath ) {
			return ( (PropertyPath) obj ).value.equals( value );
		}
		return false;
	}

	@Override
	public String toString() {
		return value;
	}
}
