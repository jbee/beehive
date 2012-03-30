package de.jbee.io.html;

import de.jbee.io.ScanTo;
import de.jbee.io.Gobble;
import de.jbee.io.CharReader;
import de.jbee.io.CharScanner;
import de.jbee.io.Collect;

public class HtmlParser
		implements CharScanner<IHtmlProcessor> {

	//TODO generalize to XML/ML so that xhtml or other xml documents can be parsed as well 

	@Override
	public void scan( CharReader in, IHtmlProcessor out ) {
		Gobble.whitespace();
		Gobble.a( '<' ).process( in );
		if ( in.peek() == '!' ) {
			// TODO comment or doctype
		}
		IHtmlTag tag = out.dialect().tag( Collect.toString( in, Collect.LETTERS ) );
		out.process( tag, ScanTo.processing( in, tag, ScanTo.scansTo( tag ) ) );
	}

}
