package de.jbee.io.xml;

import de.jbee.io.Gobble;
import de.jbee.io.ICharReader;
import de.jbee.io.ICharScanner;

public class XmlParser
		implements ICharScanner<IXmlProcessor> {

	@Override
	public void scan( ICharReader in, IXmlProcessor out ) {
		Gobble.whitespace().process( in );
		final XmlMarkupType type = XmlMarkupType.opens( in.reread( 2 ) );
		out.process( type, type );
	}
}
