package de.jbee.io.json;

import de.jbee.io.ProcessableBy;

public interface JsonProcessor
		extends JsonValueProcessor {

	void process( JsonType type, String name, ProcessableBy<JsonProcessor> element );

}
