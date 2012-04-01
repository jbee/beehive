package de.jbee.io.html;

import de.jbee.io.CharReader;
import de.jbee.io.CharScanner;
import de.jbee.io.Collect;
import de.jbee.io.Gobble;
import de.jbee.io.ScanTo;

public enum HtmlAttrType
		implements CharScanner<IHtmlProcessor> {

	CDATA( new HtmlAttributesScanner() );

	private final CharScanner<IHtmlProcessor> scanner;

	private HtmlAttrType( CharScanner<IHtmlProcessor> scanner ) {
		this.scanner = scanner;
	}

	public void scan( CharReader in, IHtmlProcessor out ) {
		scanner.scan( in, out );
	}

	static final class HtmlAttributesScanner
			implements CharScanner<IHtmlProcessor> {

		@Override
		public void scan( CharReader in, IHtmlProcessor out ) {
			Gobble.whitespace().process( in );
			char c = in.peek();
			while ( c != '/' && c != '>' ) {
				final IHtmlAttr attr = out.dialect().attr(
						Collect.toString( in, Collect.letterOrIn( "-" ) ) );
				Gobble.a( '=' ).process( in );
				out.process( attr, ScanTo.processing( in, new HtmlAttributeScanner( attr ),
						ScanTo.scansTo( Gobble.cDATA() ) ) );
				c = in.peek();
			}
		}

	}

	static final class HtmlAttributeScanner
			implements CharScanner<IHtmlProcessor> {

		final IHtmlAttr attr;

		HtmlAttributeScanner( IHtmlAttr attr ) {
			super();
			this.attr = attr;
		}

		@Override
		public void scan( CharReader in, IHtmlProcessor out ) {
			String value = Collect.toString( in, Collect.cDATA() );
			out.process( attr, value );
		}
	}
}
