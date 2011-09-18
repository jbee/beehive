package de.jbee.io.xml;

import de.jbee.io.IProcessableElement;

public interface IXmlProcessor {

	void process( XmlMarkupType type, IProcessableElement<IXmlProcessor> element );
}
