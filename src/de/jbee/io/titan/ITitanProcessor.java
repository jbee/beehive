package de.jbee.io.titan;

import de.jbee.io.IProcessableElement;

public interface ITitanProcessor {

	// das ist wahrscheinlich was allen gemein ist:
	// type: eine art ID
	// args: parameter zu der ID - etwa schema-name
	// element: der inhalt : wenn dies nur plaintext ist würde die 2. methode dann umgehend aufgerufen werden

	void process( int type, String args, IProcessableElement<ITitanProcessor> element );

	// OPEN und der text ? 2. process methode ?
	void process( String plaintext );

	// inline extra ?????

	// Auf der 1. Absraktionsebene soll nur eine Sequenz dieser Aufrufe erzeugt werden.
	// Dazu müssen bereits die einzelnen Markups erkannt werden und sich entsprechend verschachtelt 
	// aufrufen
}
