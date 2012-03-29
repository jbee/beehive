package de.jbee.io.html;

import de.jbee.io.CharScanner;
import de.jbee.io.Gobble;
import de.jbee.io.ICharReader;
import de.jbee.io.ICharScanner;
import de.jbee.io.Read;

public enum HtmlAttrType
		implements ICharScanner<IHtmlProcessor> {

	CDATA( new HtmlAttributesScanner() );

	private final ICharScanner<IHtmlProcessor> scanner;

	private HtmlAttrType( ICharScanner<IHtmlProcessor> scanner ) {
		this.scanner = scanner;
	}

	public void scan( ICharReader in, IHtmlProcessor out ) {
		scanner.scan( in, out );
	}

	static final class HtmlAttributesScanner
			implements ICharScanner<IHtmlProcessor> {

		@Override
		public void scan( ICharReader in, IHtmlProcessor out ) {
			Gobble.whitespace().process( in );
			char c = in.peek();
			while ( c != '/' && c != '>' ) {
				final IHtmlAttr attr = out.dialect().attr(
						Read.toString( in, Read.letterOrUniverse( "-" ) ) );
				Gobble.a( '=' ).process( in );
				out.process( attr, CharScanner.processing( in, new HtmlAttributeScanner( attr ),
						CharScanner.of( Gobble.cDATA() ) ) );
				c = in.peek();
			}
		}

	}

	static final class HtmlAttributeScanner
			implements ICharScanner<IHtmlProcessor> {

		final IHtmlAttr attr;

		HtmlAttributeScanner( IHtmlAttr attr ) {
			super();
			this.attr = attr;
		}

		@Override
		public void scan( ICharReader in, IHtmlProcessor out ) {
			String value = Read.toString( in, Read.CDDATA );
			out.process( attr, value );
		}
	}
}
