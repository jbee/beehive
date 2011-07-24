package de.jbee.io.html;

public enum HtmlTagType {

	/**
	 * Never have closing tags. Doesn't matter if at slash makes it an recognizable empty tag or
	 * not.
	 */
	ALWAYS_EMPTY,
	/**
	 * It is necessary to be used as open/close tag since otherwise there is no use to use it.
	 */
	NEVER_EMPTY,
	/**
	 * This tag can be used in different ways. Most of the times it depends on the attributes used
	 * if they have to be open/close or just empty.
	 */
	MAYBE_EMPTY;

	public boolean isAlwaysEmpty() {
		return this == ALWAYS_EMPTY;
	}
}
