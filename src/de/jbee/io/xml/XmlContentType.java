package de.jbee.io.xml;

/**
 * What kind of children are expected for a XML-element.
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 */
public enum XmlContentType {

	/**
	 * Never have closing tags. Doesn't matter if at slash makes it an recognizable empty tag or
	 * not.
	 */
	EMPTY,

	/**
	 * All tags and plain text content is allowed without any restriction.
	 */
	ANY,

	/**
	 * Some of the tags and/or plain text content is allowed. Which depends on the tag.
	 */
	SOME;

	public boolean isAlwaysEmpty() {
		return this == EMPTY;
	}
}
