package de.jbee.io.json;

public interface IJsonValue {

	void pass( IJsonTreeVisitor visitor );

	void passChildren( IJsonTreeVisitor visitor );
}
