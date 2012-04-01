package de.jbee.io.xml;

import de.jbee.io.CharReader;
import de.jbee.io.CharScanner;
import de.jbee.io.Gobble;

public class XmlParser
		implements CharScanner<XmlProcessor> {

	@Override
	public void scan( CharReader in, XmlProcessor out ) {
		Gobble.whitespace().process( in );

	}

}
