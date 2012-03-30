package de.jbee.io.html;

import de.jbee.io.CharProcessor;
import de.jbee.io.CharScanner;
import de.jbee.io.xml.XmlContentType;

public interface IHtmlTag
		extends CharScanner<IHtmlProcessor>, CharProcessor {

	String name();

	XmlContentType getType();
}
