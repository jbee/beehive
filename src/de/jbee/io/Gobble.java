package de.jbee.io;

import java.util.InputMismatchException;

public final class Gobble {

	private static final ICharProcessor NOTHING = new NullCharProcessor();
	private static final ICharProcessor NEXT_1 = new GobbleN( 1 );
	private static final ICharProcessor WHITESPACE = new GobbleWhitespace();

	private Gobble() {
		// util
	}

	static final class GobbleAll
			implements ICharProcessor {

		final char gobbled;

		GobbleAll( char gobbled ) {
			super();
			this.gobbled = gobbled;
		}

		@Override
		public void process( ICharReader in ) {
			while ( in.hasNext() && in.peek() == gobbled ) {
				in.next();
			}
		}
	}

	static final class GobbleUntilChar
			implements ICharProcessor {

		final char end;

		GobbleUntilChar( char end ) {
			super();
			this.end = end;
		}

		@Override
		public void process( ICharReader in ) {
			while ( in.hasNext() && in.peek() != end ) {
				in.next();
			}
		}
	}

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

	static final class GobbleN
			implements ICharProcessor {

		final int n;

		GobbleN( int n ) {
			super();
			this.n = n;
		}

		@Override
		public void process( ICharReader in ) {
			for ( int i = 0; i < n; i++ ) {
				in.next();
			}
		}
	}

	static final class NullCharProcessor
			implements ICharProcessor {

		@Override
		public void process( ICharReader in ) {
			// do nothing
		}
	}

	static final class GobbleWhitespace
			implements ICharProcessor {

		@Override
		public void process( ICharReader in ) {
			while ( in.hasNext() && Character.isWhitespace( in.peek() ) ) {
				in.next();
			}

		}
	}

	static final class GobbleAChar
			extends ExpectingCharProcessor {

		final char mandatory;

		GobbleAChar( char mandatory ) {
			super();
			this.mandatory = mandatory;
		}

		@Override
		public void process( ICharReader in ) {
			expect( in, mandatory );
		}
	}

	static final class GobbleASequence
			extends ExpectingCharProcessor {

		final CharSequence sequence;

		GobbleASequence( CharSequence sequence ) {
			super();
			this.sequence = sequence;
		}

		@Override
		public void process( ICharReader in ) {
			for ( int i = 0; i < sequence.length(); i++ ) {
				expect( in, sequence.charAt( i ) );
			}
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
		return first == NOTHING
			? second
			: second == NOTHING
				? first
				: new CombinedCharProcesspr( first, second );
	}

	public static ICharProcessor next( int n ) {
		return n <= 0
			? NOTHING
			: n == 1
				? NEXT_1
				: new GobbleN( n );
	}

	public static ICharProcessor until( char end ) {
		return new GobbleUntilChar( end );
	}

	public static ICharProcessor next1() {
		return NEXT_1;
	}

	public static ICharProcessor whitespace() {
		return WHITESPACE;
	}

	public static ICharProcessor whitespaced( ICharProcessor inner ) {
		return inner == NOTHING
			? WHITESPACE
			: combine( combine( WHITESPACE, inner ), WHITESPACE );
	}

	public static ICharProcessor aWhitespaced( char mandatory ) {
		return whitespaced( a( mandatory ) );
	}

	public static ICharProcessor a( char mandatory ) {
		return new GobbleAChar( mandatory );
	}

	public static ICharProcessor mandatory( String sequence ) {
		return new GobbleASequence( sequence );
	}

	public static ICharProcessor all( char optional ) {
		return new GobbleAll( optional );
	}
}
