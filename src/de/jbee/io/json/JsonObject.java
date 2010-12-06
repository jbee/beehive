package de.jbee.io.json;

import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;

public final class JsonObject
		implements IJson {

	static final class JsonMember
			implements Serializable, Comparable<JsonMember> {

		final String name;
		final IJson value;

		JsonMember( String name, IJson value ) {
			super();
			this.name = name;
			this.value = value;
		}

		@Override
		public String toString() {
			return name + ":" + value;
		}

		@Override
		public int compareTo( JsonMember other ) {
			int res = name.compareTo( other.name );
			if ( res == 0 ) {
				res = value.compareTo( other.value );
			}
			return res;
		}

		@Override
		public boolean equals( Object obj ) {
			return obj instanceof JsonMember && ( (JsonMember) obj ).name.equals( name );
		}

		@Override
		public int hashCode() {
			return name.hashCode();
		}
	}

	private final JsonMember[] members;

	public JsonObject( JsonMember[] members ) {
		this.members = members;
	}

	public static JsonObject of( Map<String, IJson> values ) {
		JsonMember[] members = new JsonMember[values.size()];
		int i = 0;
		for ( Entry<String, IJson> e : values.entrySet() ) {
			members[i++] = new JsonMember( e.getKey(), e.getValue() );
		}
		return new JsonObject( members );
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append( '{' );
		for ( int i = 0; i < members.length; i++ ) {
			if ( i > 0 ) {
				b.append( ',' );
				b.append( '\n' );
			}
			b.append( members[i].toString() );
		}
		b.append( '}' );
		return b.toString();
	}

	@Override
	public void pass( IJsonTreeVisitor visitor ) {
		visitor.visit( this );
	}

	@Override
	public void passChildren( IJsonTreeVisitor visitor ) {
		for ( JsonMember m : members ) {
			visitor.visitMember( m.name, m.value );
		}
	}

	@Override
	public int compareTo( IJson other ) {
		if ( other.getClass() != JsonObject.class ) {
			return -1;
		}
		final JsonMember[] otherMembers = ( (JsonObject) other ).members;
		if ( otherMembers.length != members.length ) {
			return Integer.signum( members.length - otherMembers.length );
		}
		for ( int i = 0; i < otherMembers.length; i++ ) {
			int mCompare = otherMembers[i].compareTo( members[i] );
			if ( mCompare != 0 ) {
				return mCompare;
			}
		}
		return 0;
	}
}
