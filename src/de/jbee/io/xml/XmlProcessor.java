package de.jbee.io.xml;

import de.jbee.io.ProcessableBy;

public interface XmlProcessor {

	void process( XmlMarkupType type, String name, ProcessableBy<XmlProcessor> content );
}
