package de.jbee.io.titan;

import de.jbee.io.Gobble;
import de.jbee.io.ICharReader;
import de.jbee.io.ICharScanner;
import de.jbee.io.Read;

/**
 * <pre>
 *               anticipated
 * Text in truly ambiguous notation
 * </pre>
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 * 
 */
public class Titan {

	static class HeadlineScanner
			implements ICharScanner<ITitanProcessor> {

		@Override
		public void scan( ICharReader in, ITitanProcessor out ) {
			StringBuilder headline = new StringBuilder( 100 );
			Read.toBuffer( in, headline, Read.LINE );
			Gobble.a( '\n' );
			while ( in.peek() == ' ' ) {
				Gobble.whitespace().process( in );
				Read.toBuffer( in, headline, Read.LINE );
			}
			out.process( headline.toString() );
		}

	}
}
