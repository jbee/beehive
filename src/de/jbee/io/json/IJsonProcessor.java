package de.jbee.io.json;

import de.jbee.io.IProcessableUnit;

public interface IJsonProcessor
		extends IJsonConverter {

	void process( JsonType type, String name, IProcessableUnit<IJsonProcessor> unit );

}
