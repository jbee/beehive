package de.jbee.io;

import java.util.InputMismatchException;

/**
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 * 
 * @see ICharProcessor
 * @see Gobble
 */
public final class CharProcessor {

	private CharProcessor() {
		// util
	}

	public static final ICharProcessor NULL_OBJECT = new NullCharProcessor();

	static final class CombinedCharProcesspr
			implements ICharProcessor {

		final ICharProcessor first;
		final ICharProcessor second;

		CombinedCharProcesspr( ICharProcessor first, ICharProcessor second ) {
			super();
			this.first = first;
			this.second = second;
		}

		@Override
		public void process( ICharReader in ) {
			first.process( in );
			second.process( in );
		}
	}

	static final class NullCharProcessor
			implements ICharProcessor {

		@Override
		public void process( ICharReader in ) {
			// do nothing
		}
	}

	static abstract class ExpectingCharProcessor
			implements ICharProcessor {

		protected final void mismatch( String message )
				throws InputMismatchException {
			throw new InputMismatchException( message );
		}

		protected final void expect( ICharReader in, char expected ) {
			if ( !in.hasNext() ) {
				mismatch( "Needed '" + expected + "' but no further token available!" );
			}
			char next = in.next();
			if ( next != expected ) {
				mismatch( "Expeceted '" + expected + "' but found: '" + next + "'" );
			}
		}
	}

	public static ICharProcessor combine( ICharProcessor first, ICharProcessor second ) {
		return first == NULL_OBJECT
			? second
			: second == NULL_OBJECT
				? first
				: new CombinedCharProcesspr( first, second );
	}
}
