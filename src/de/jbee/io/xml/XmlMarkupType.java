package de.jbee.io.xml;

import de.jbee.io.Gobble;
import de.jbee.io.CharProcessor;
import de.jbee.io.CharReader;
import de.jbee.io.CharScanner;
import de.jbee.io.ProcessableBy;
import de.jbee.io.html.IHtmlTag;

public enum XmlMarkupType
		implements CharScanner<IXmlProcessor>, CharProcessor, ProcessableBy<IXmlProcessor> {

	XML_DECLARATION(),
	PROCESSING_INSTRUCTION,
	TAG(),
	ATTRIBUTE(),
	/**
	 * all plain text in the body of a tag is content. content is the only element considered not to
	 * be markup.
	 */
	CONTENT(),
	/**
	 * Start with a '&' and end with a ';'. Those are markup (not content).
	 */
	ENTITY_REF,
	CHAR_REF,
	PARAMETER_REF,
	DOCTYPE_DECLARATION(),
	COMMENT(), ;

	private final CharScanner<? super IXmlProcessor> scanner;

	private XmlMarkupType() {
		this.scanner = null;
	}

	@Override
	public void discardBy( IXmlProcessor processor ) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processBy( IXmlProcessor processor ) {
		// TODO Auto-generated method stub

	}

	public boolean isMarkup() {
		return this != CONTENT;
	}

	public void scan( CharReader in, IXmlProcessor out ) {
		scanner.scan( in, out );
	}

	static class XmlDoctypeScanner
			implements CharScanner<IXmlProcessor> {

		@Override
		public void scan( CharReader in, IXmlProcessor out ) {
			Gobble.just( "<!DOCTYPE" ).process( in );
			Gobble.whitespace().process( in );
		}

	}

	static class XmlTagScanner
			implements CharScanner<IXmlProcessor> {

		private final IHtmlTag tag;

		XmlTagScanner( IHtmlTag tag ) {
			super();
			this.tag = tag;
		}

		@Override
		public final void scan( CharReader in, IXmlProcessor out ) {

		}

	}

	@Override
	public void process( CharReader in ) {
		// TODO Auto-generated method stub

	}

	static XmlMarkupType opens( CharReader in ) {
		char first = in.next();
		if ( first == '<' ) {
			char second = in.next();
			if ( second == '?' ) {
				return XML_DECLARATION;
			}
			if ( second == '!' ) {
				return in.peek() == '-'
					? COMMENT
					: DOCTYPE_DECLARATION;
			}
			return TAG;
		}
		if ( first == '&' ) {
			return ENTITY_REF;
		}
		return CONTENT;
	}

}
