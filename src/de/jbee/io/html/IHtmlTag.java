package de.jbee.io.html;

import de.jbee.io.ICharProcessor;
import de.jbee.io.ICharScanner;

public interface IHtmlTag
		extends ICharScanner<IHtmlProcessor>, ICharProcessor {

	String name();

}
