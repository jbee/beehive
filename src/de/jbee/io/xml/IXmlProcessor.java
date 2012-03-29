package de.jbee.io.xml;

import de.jbee.io.ProcessableBy;

public interface IXmlProcessor {

	void process( XmlMarkupType type, ProcessableBy<IXmlProcessor> element );
}
