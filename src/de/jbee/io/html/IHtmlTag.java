package de.jbee.io.html;

import de.jbee.io.ICharProcessor;
import de.jbee.io.ICharScanner;
import de.jbee.io.xml.XmlContentType;

public interface IHtmlTag
		extends ICharScanner<IHtmlProcessor>, ICharProcessor {

	String name();

	XmlContentType getType();
}
