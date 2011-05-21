package de.jbee.io.html;

public interface IHtmlDialect {

	IHtmlTag tag( String name );

	IHtmlAttr attr( String name );

}
