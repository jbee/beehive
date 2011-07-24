package de.jbee.io.html;

import static de.jbee.io.html.HtmlLayoutType.BLOCK;
import static de.jbee.io.html.HtmlLayoutType.INLINE;
import static de.jbee.io.html.HtmlLayoutType.INLINE_OR_BLOCK;
import static de.jbee.io.html.HtmlLayoutType.NONE;
import static de.jbee.io.html.HtmlTagType.ALWAYS_EMPTY;
import static de.jbee.io.html.HtmlTagType.MAYBE_EMPTY;
import static de.jbee.io.html.HtmlTagType.NEVER_EMPTY;
import de.jbee.io.ICharReader;

public enum Html4Dialect
		implements IHtmlTag, IHtmlDialect {

	//!DOCTYPE,
	html,
	head,
	title,
	body,
	//<!-- -->	Comment 

	// Text, Tags,	Tags, to, adjust, text, on, your, Web, page,.	 	 	 	 	 	 
	p,
	br( ALWAYS_EMPTY, BLOCK ),
	b,
	i,
	s,
	strike,
	big,
	small,

	// Link, Tags,	Tags, to, adjust, text, on, your, Web, page,.	 	 	 	 	 	 
	a,

	base( ALWAYS_EMPTY, NONE ),

	// List, Tags,	Tags, to, create, lists, on, your, Web, page,.	 	 	 	 	 	 
	ul,
	ol,
	li,
	dl,
	dt,
	dd,

	// Semantic, Tags,	Tags, that, describe, the, contents,.	 	 	 	 	 	 
	abbr,
	acronym,
	dfn,
	address,
	code,
	tt,
	div,
	span,
	del( NEVER_EMPTY, INLINE_OR_BLOCK ),
	ins( NEVER_EMPTY, INLINE_OR_BLOCK ),
	em,
	strong,
	h1,
	h2,
	h3,
	h4,
	h5,
	h6,
	hr( ALWAYS_EMPTY, BLOCK ),
	kbd,
	pre,
	samp,
	sub,
	sup,
	var,

	//Image, Tags,	Tags, that, generate, images,.	 	 	 	 	 	 
	img( ALWAYS_EMPTY, INLINE ),
	map,
	area( ALWAYS_EMPTY ),

	//Quotation, Tags,	Tags, that, define, quotations,.	 	 	 	 	 	 
	blockquote,
	q( INLINE ),
	cite,

	//Form, Tags,	Tags, that, create, and, define, HTML, forms,.	 	 	 	 	 	 
	form,
	button( INLINE_OR_BLOCK ),
	input( ALWAYS_EMPTY ),
	select,
	option,
	textarea,
	fieldset,
	label,
	legend,
	optgroup,

	//Frame, Tags,	Tags, that, create, and, define, HTML, frames,.	 	 	 	 	 	 
	frameset,
	frame,
	noframes,
	iframe,

	//Table, Tags,	Tags, that, create, and, define, HTML, tables,.	 	 	 	 	 	 
	table,
	tr,
	th,
	td,
	tbody,
	tfoot,
	thead,
	caption,
	col( ALWAYS_EMPTY ),
	colgroup,

	//Object, Tags,	Tags, that, manipulate, objects, and, multimedia,.	 	 	 	 	 	 
	object,
	param( ALWAYS_EMPTY ),
	script( MAYBE_EMPTY ),
	noscript,

	//Style, Tags,	Tags, that, style, the, contents, or, work, with, style, sheets,.	 	 	 	 	 	 
	style,
	link( ALWAYS_EMPTY ),

	//Meta, Tags,	Tags, that, define, meta, data,.	 	 	 	 	 	 
	meta( ALWAYS_EMPTY ),

	//International, Tags,	Tags, that, work, with, the, language, of, the, document,.	 	 	 	 	 	 
	bdo,

	;

	private final HtmlTagType type;
	private final HtmlElementType elementType;
	private final HtmlLayoutType layoutType;

	Html4Dialect() {
		this( HtmlTagType.NEVER_EMPTY, HtmlLayoutType.BLOCK );
	}

	Html4Dialect( HtmlLayoutType layoutType ) {
		this( HtmlTagType.NEVER_EMPTY, layoutType );
	}

	Html4Dialect( HtmlTagType type ) {
		this( type, HtmlLayoutType.BLOCK );
	}

	Html4Dialect( HtmlTagType type, HtmlLayoutType layoutType ) {
		this( type, HtmlElementType.PRESENTATION_TAG, layoutType );
	}

	Html4Dialect( HtmlTagType type, HtmlElementType elementType, HtmlLayoutType layoutType ) {
		this.type = type;
		this.elementType = elementType;
		this.layoutType = layoutType;
	}

	@Override
	public HtmlTagType getType() {
		return type;
	}

	@Override
	public void scan( ICharReader in, IHtmlProcessor out ) {
		elementType.scan( in, out ); //FIXME use a specific scanner for this tags name
	}

	/**
	 * Gobbles the element (including its attributes and children for block elements).
	 */
	@Override
	public void process( ICharReader in ) {
		// TODO Auto-generated method stub

	}

	@Override
	public IHtmlAttr attr( String name ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IHtmlTag tag( String name ) {
		return valueOf( name );
	}

}
