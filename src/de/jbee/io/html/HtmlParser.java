package de.jbee.io.html;

import de.jbee.io.CharScanner;
import de.jbee.io.Gobble;
import de.jbee.io.ICharReader;
import de.jbee.io.ICharScanner;
import de.jbee.io.Read;

public class HtmlParser
		implements ICharScanner<IHtmlProcessor> {

	//TODO generalize to XML/ML so that xhtml or other xml documents can be parsed as well 

	@Override
	public void scan( ICharReader in, IHtmlProcessor out ) {
		Gobble.whitespace();
		Gobble.a( '<' ).process( in );
		if ( in.peek() == '!' ) {
			// TODO comment or doctype
		}
		IHtmlTag tag = out.dialect().tag( Read.toString( in, Read.LETTERS ) );
		out.process( tag, CharScanner.processable( in, tag, CharScanner.of( tag ) ) );
	}

}
