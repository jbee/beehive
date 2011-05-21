package de.jbee.io.json;

import de.jbee.io.IProcessableElement;

public interface IJsonProcessor
		extends IJsonValueProcessor {

	void process( JsonType type, String name, IProcessableElement<IJsonProcessor> element );

}
