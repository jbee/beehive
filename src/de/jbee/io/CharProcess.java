package de.jbee.io;

import java.util.InputMismatchException;

/**
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 * 
 * @see CharProcessor
 * @see Gobble
 */
public final class CharProcess {

	public static final CharProcessor NULL_OBJECT = new NullCharProcessor();

	public static CharProcessor combine( CharProcessor first, CharProcessor... more ) {
		CharProcessor res = first;
		for ( int i = 0; i < more.length; i++ ) {
			res = combine( res, more[i] );
		}
		return res;
	}

	public static CharProcessor combine( CharProcessor first, CharProcessor second ) {
		return first == NULL_OBJECT
			? second
			: second == NULL_OBJECT
				? first
				: new CombinedCharProcessor( first, second );
	}

	public static CharProcessor listOf( CharProcessor element, char separator ) {
		return new ListCharProcessor( element, separator );
	}

	public static <T> CharProcessor processor( CharScanner<T> scanner, T out ) {
		return new CharScannerAdapter<T>( scanner, out );
	}

	public static CharProcessor successively( CharProcessor first, CharProcessor... more ) {
		if ( more == null || more.length == 0 ) {
			return first;
		}
		CharProcessor res = first;
		for ( int i = 0; i < more.length; i++ ) {
			res = combine( res, more[i] );
		}
		return res;
	}

	private CharProcess() {
		// util
	}

	private static final class CharScannerAdapter<T>
			implements CharProcessor {

		final CharScanner<T> scanner;
		final T out;

		CharScannerAdapter( CharScanner<T> scanner, T out ) {
			super();
			this.scanner = scanner;
			this.out = out;
		}

		@Override
		public void process( CharReader in ) {
			scanner.scan( in, out );
		}
	}

	private static final class CombinedCharProcessor
			implements CharProcessor {

		final CharProcessor first;
		final CharProcessor second;

		CombinedCharProcessor( CharProcessor first, CharProcessor second ) {
			super();
			this.first = first;
			this.second = second;
		}

		@Override
		public void process( CharReader in ) {
			first.process( in );
			second.process( in );
		}
	}

	static abstract class ExpectingCharProcessor
			implements CharProcessor {

		protected final void expect( CharReader in, char expected ) {
			expectHasNext( in );
			char next = in.next();
			if ( next != expected ) {
				mismatch( "Expeceted '" + expected + "' but found: '" + next + "'" );
			}
		}

		private void expectHasNext( CharReader in ) {
			if ( !in.hasNext() ) {
				mismatch( "Needed further character but no further token available!" );
			}
		}

		protected final void expectAny( CharReader in, String anyMember ) {
			expectHasNext( in );
			char next = in.next();
			if ( anyMember.indexOf( next ) < 0 ) {
				mismatch( "Expected any of [" + anyMember + "] but found: '" + next + "'" );
			}
		}

		protected final void expect( CharPredicate predicate, CharReader in ) {
			expectHasNext( in );
			final char next = in.next();
			if ( predicate.isSuitable( next ) ) {
				mismatch( "Expected any of [" + predicate + "] but found: '" + next + "'" );
			}
		}

		protected final void mismatch( String message )
				throws InputMismatchException {
			throw new InputMismatchException( message );
		}
	}

	private static final class ListCharProcessor
			implements CharProcessor {

		private final CharProcessor element;
		private final char separator;

		ListCharProcessor( CharProcessor element, char separator ) {
			super();
			this.element = element;
			this.separator = separator;
		}

		@Override
		public void process( CharReader in ) {
			element.process( in );
			if ( in.peek() == separator ) {
				Gobble.a( separator ).process( in );
				process( in );
			}
		}
	}

	private static final class NullCharProcessor
			implements CharProcessor {

		NullCharProcessor() {
			// make visible
		}

		@Override
		public void process( CharReader in ) {
			// do nothing
		}
	}
}
