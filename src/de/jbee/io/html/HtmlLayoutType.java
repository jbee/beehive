package de.jbee.io.html;

public enum HtmlLayoutType {

	/**
	 * The tag is not visible at all since it has a functional reasons to appear like all tags in
	 * <code>head</code> section.
	 */
	NONE,
	INLINE,
	BLOCK,

	/**
	 * Is a {@link #INLINE} or {@link #BLOCK} depending on its parent. This is true for
	 * <code>del</code> and <code>ins</code>.
	 */
	INLINE_OR_BLOCK;
}
