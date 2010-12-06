package de.jbee.io.json;

import java.io.Serializable;

public interface IJson
		extends Serializable, Comparable<IJson> {

	void pass( IJsonTreeVisitor visitor );

	void passChildren( IJsonTreeVisitor visitor );
}
