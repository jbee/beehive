package de.jbee.io.json;

import de.jbee.io.IProcessable;

public interface IJsonProcessor
		extends IJsonVisitor {

	void process( JsonType type, String name, IProcessable<IJsonProcessor> element );

}
