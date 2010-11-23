package de.jbee.io.json;

import de.jbee.io.IProcessable;

public interface IJsonProcessor
		extends IJsonVisitor {

	void decideOn( JsonType type, String name, IProcessable<IJsonProcessor> element );

}
