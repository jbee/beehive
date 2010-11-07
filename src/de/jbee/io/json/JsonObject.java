package de.jbee.io.json;

import java.util.Map;
import java.util.Map.Entry;

public final class JsonObject
		implements IJsonValue {

	private final Map<String, IJsonValue> values;

	private JsonObject( Map<String, IJsonValue> values ) {
		super();
		this.values = values;
	}

	@Override
	public void pass( IJsonTreeVisitor visitor ) {
		visitor.visit( this );
	}

	@Override
	public void passChildren( IJsonTreeVisitor visitor ) {
		for ( Entry<String, IJsonValue> e : values.entrySet() ) {
			visitor.visitMember( e.getKey(), e.getValue() );
		}
	}
}
