package de.jbee.io.json;

public interface IJson {

	void pass( IJsonTreeVisitor visitor );

	void passChildren( IJsonTreeVisitor visitor );
}
