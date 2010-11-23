package de.jbee.io.json;

import java.util.Map;
import java.util.Map.Entry;

public final class JsonObject
		implements IJsonValue {

	static class JsonMember {

		final String name;
		final IJsonValue value;

		JsonMember( String name, IJsonValue value ) {
			super();
			this.name = name;
			this.value = value;
		}

		@Override
		public String toString() {
			return name + ":" + value;
		}
	}

	private final JsonMember[] members;

	public JsonObject( Map<String, IJsonValue> values ) {
		super();
		this.members = new JsonMember[values.size()];
		int i = 0;
		for ( Entry<String, IJsonValue> e : values.entrySet() ) {
			members[i++] = new JsonMember( e.getKey(), e.getValue() );
		}
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
}
