package de.jbee.io.json;

import java.io.Serializable;

/**
 * A chunk of {@link JSON} data. That is a java tree representation of a JSON value/object.
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 */
public interface Json
		extends Serializable, Comparable<Json> {

	void pass( JsonTreeVisitor visitor );

	void passChildren( JsonTreeVisitor visitor );
}
