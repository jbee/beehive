package de.jbee.io.xml;

import de.jbee.io.CharScanner;
import de.jbee.io.Gobble;
import de.jbee.io.CharReader;

public class XmlParser
		implements CharScanner<IXmlProcessor> {

	@Override
	public void scan( CharReader in, IXmlProcessor out ) {
		Gobble.whitespace().process( in );
		final XmlMarkupType type = null; //FIXME required reread of next 2 characters
		out.process( type, type );
	}
}
