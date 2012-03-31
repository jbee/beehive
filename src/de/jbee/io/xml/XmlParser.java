package de.jbee.io.xml;

import de.jbee.io.CharReader;
import de.jbee.io.CharScanner;
import de.jbee.io.Gobble;

public class XmlParser
		implements CharScanner<IXmlProcessor> {

	@Override
	public void scan( CharReader in, IXmlProcessor out ) {
		Gobble.whitespace().process( in );
		final XmlMarkupType type = null; //FIXME required reread of next 2 characters
		out.process( type, type );
	}

	// use another approach: 
	// -- see XML in a simplified way where we just know about tags having attributes in first place
	// -- have a context object that we can ask to 
	// 		-- correct a open-tag (might be a known empty tag)
	//		-- correct content (might just allow CDATA/plain text/some tags)
	// -- result of corrections can be totally different strings -> we read something but what we understand might be something else
}
