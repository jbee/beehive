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

	static final class CharScannerAdapter<T>
			implements ICharProcessor {

		final ICharScanner<T> scanner;
		final T out;

		CharScannerAdapter( ICharScanner<T> scanner, T out ) {
			super();
			this.scanner = scanner;
			this.out = out;
		}

		@Override
		public void process( ICharReader in ) {
			scanner.scan( in, out );
		}
	}

	static final class NullCharProcessor
			implements ICharProcessor {

		@Override
		public void process( ICharReader in ) {
			// do nothing
		}
	}

	static final class ListCharProcessor
			implements ICharProcessor {

		private final ICharProcessor element;
		private final char separator;

		ListCharProcessor( ICharProcessor element, char separator ) {
			super();
			this.element = element;
			this.separator = separator;
		}

		@Override
		public void process( ICharReader in ) {
			element.process( in );
			if ( in.peek() == separator ) {
				Gobble.a( separator ).process( in );
				process( in );
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

	public static ICharProcessor list( ICharProcessor element, char separator ) {
		return new ListCharProcessor( element, separator );
	}

	public static ICharProcessor combine( ICharProcessor first, ICharProcessor second ) {
		return first == NULL_OBJECT
			? second
			: second == NULL_OBJECT
				? first
				: new CombinedCharProcesspr( first, second );
	}

	public static <T> ICharProcessor of( ICharScanner<T> scanner, T out ) {
		return new CharScannerAdapter<T>( scanner, out );
	}
}
