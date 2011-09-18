package de.jbee.io.html;

import static de.jbee.io.html.HtmlLayoutType.BLOCK;
import static de.jbee.io.html.HtmlLayoutType.INLINE;
import static de.jbee.io.html.HtmlLayoutType.INLINE_OR_BLOCK;
import static de.jbee.io.html.HtmlLayoutType.NONE;
import static de.jbee.io.xml.XmlContentType.EMPTY;
import de.jbee.io.ICharReader;
import de.jbee.io.xml.XmlContentType;

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
	br( EMPTY, BLOCK ),
	b,
	i,
	s,
	strike,
	big,
	small,

	// Link, Tags,	Tags, to, adjust, text, on, your, Web, page,.	 	 	 	 	 	 
	a,

	base( EMPTY, NONE ),

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
	del( INLINE_OR_BLOCK ),
	ins( INLINE_OR_BLOCK ),
	em,
	strong,
	h1,
	h2,
	h3,
	h4,
	h5,
	h6,
	hr( EMPTY, BLOCK ),
	kbd,
	pre,
	samp,
	sub,
	sup,
	var,

	//Image, Tags,	Tags, that, generate, images,.	 	 	 	 	 	 
	img( EMPTY, INLINE ),
	map,
	area( EMPTY ),

	//Quotation, Tags,	Tags, that, define, quotations,.	 	 	 	 	 	 
	blockquote,
	q( INLINE ),
	cite,

	//Form, Tags,	Tags, that, create, and, define, HTML, forms,.	 	 	 	 	 	 
	form,
	button( INLINE_OR_BLOCK ),
	input( EMPTY ),
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
	col( EMPTY ),
	colgroup,

	//Object, Tags,	Tags, that, manipulate, objects, and, multimedia,.	 	 	 	 	 	 
	object,
	param( EMPTY ),
	script,
	noscript,

	//Style, Tags,	Tags, that, style, the, contents, or, work, with, style, sheets,.	 	 	 	 	 	 
	style,
	link( EMPTY ),

	//Meta, Tags,	Tags, that, define, meta, data,.	 	 	 	 	 	 
	meta( EMPTY ),

	//International, Tags,	Tags, that, work, with, the, language, of, the, document,.	 	 	 	 	 	 
	bdo,

	;

	private final XmlContentType type;
	private final HtmlLayoutType layoutType;

	Html4Dialect() {
		this( XmlContentType.ANY, HtmlLayoutType.BLOCK );
	}

	Html4Dialect( HtmlLayoutType layoutType ) {
		this( XmlContentType.ANY, layoutType );
	}

	Html4Dialect( XmlContentType type ) {
		this( type, HtmlLayoutType.BLOCK );
	}

	Html4Dialect( XmlContentType type, HtmlLayoutType layoutType ) {
		this.type = type;
		this.layoutType = layoutType;
	}

	@Override
	public XmlContentType getType() {
		return type;
	}

	@Override
	public void scan( ICharReader in, IHtmlProcessor out ) {
		//TODO
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
