package de.jbee.io;

import java.io.IOException;
import java.io.Reader;

public class CharIO {

	public static CharWriter appender( Appendable out ) {
		return new AppendableCharWriter( out );
	}

	public static CharReader reader( Reader reader ) {
		return new ReaderAdapter( reader );
	}

	private static class AppendableCharWriter
			implements CharWriter {

		private final Appendable out;

		AppendableCharWriter( Appendable out ) {
			super();
			this.out = out;
		}

		@Override
		public void append( char c ) {
			try {
				out.append( c );
			} catch ( IOException e ) {
				throw new RuntimeException( e );
			}
		}

		@Override
		public void append( char[] c ) {
			try {
				out.append( String.valueOf( c ) );
			} catch ( IOException e ) {
				throw new RuntimeException( e );
			}
		}

		@Override
		public void append( CharSequence s ) {
			try {
				out.append( s );
			} catch ( IOException e ) {
				throw new RuntimeException( e );
			}
		}

	}

	private static final class ReaderAdapter
			implements CharReader {

		private final Reader reader;

		private boolean ahead = false;
		private int token;

		ReaderAdapter( Reader reader ) {
			super();
			this.reader = reader;
		}

		@Override
		public boolean hasNext() {
			return ahead
				? tokenAvailable()
				: readAhead();
		}

		@Override
		public char next() {
			readAheadIfNeeded();
			ahead = false;
			return (char) token;
		}

		@Override
		public char peek() {
			readAheadIfNeeded();
			return (char) token;
		}

		private boolean readAhead() {
			try {
				token = reader.read();
				ahead = true;
				return tokenAvailable();
			} catch ( IOException e ) {
				return false;
			}
		}

		private void readAheadIfNeeded() {
			if ( !ahead ) {
				readAhead();
			}
			if ( !tokenAvailable() ) {
				throw new IllegalStateException( "No further tokens available!" );
			}
		}

		private boolean tokenAvailable() {
			return token != -1;
		}

	}

}
