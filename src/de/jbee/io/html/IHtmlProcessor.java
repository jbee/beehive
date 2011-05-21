package de.jbee.io.html;

import de.jbee.io.IProcessableElement;

public interface IHtmlProcessor {

	IHtmlDialect dialect();

	void process( IHtmlTag tag, IProcessableElement<IHtmlProcessor> element );

	void process( IHtmlAttr attr, IProcessableElement<IHtmlProcessor> element );

	void process( IHtmlAttr attr, String value );
}
