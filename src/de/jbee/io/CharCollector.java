package de.jbee.io;

/**
 * Takes characters form a input {@link CharReader} and passes them to a output {@link CharWriter}.
 * 
 * The difference in the implementation is given by the characters passed until the scanner stops
 * scanning further characters. E.g. a scanner might just scan all digits or such. As soon as a
 * character is not what the scanner wants to pass it quits and the control flow returns to the
 * caller of {@link #scan(CharReader, CharWriter)} so that another {@link CharScanner} or
 * {@linkplain CharCollector} can continue to process the stream of characters.
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 */
public interface CharCollector
		extends CharScanner<CharWriter> {

	// just give this generic combination a name
}
