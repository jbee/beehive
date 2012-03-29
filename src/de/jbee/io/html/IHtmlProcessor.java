package de.jbee.io.html;

import de.jbee.io.ProcessableBy;

public interface IHtmlProcessor {

	IHtmlDialect dialect();

	void process( IHtmlTag tag, ProcessableBy<IHtmlProcessor> element );

	void process( IHtmlAttr attr, ProcessableBy<IHtmlProcessor> element );

	void process( IHtmlAttr attr, String value );
}
