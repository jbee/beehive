package de.jbee.io;

import java.io.IOException;
import java.io.Reader;

public class CharReader
		implements ICharReader {

	public static ICharReader of( Reader reader ) {
		return new ReaderAdapter( reader );
	}

	private final ICharReader in;

	public CharReader( ICharReader in ) {
		super();
		this.in = in;
	}

	@Override
	public boolean hasNext() {
		return in.hasNext();
	}

	@Override
	public Character next() {
		return in.next();
	}

	public CharReader once( ICharProcessor p ) {
		p.process( in );
		return this;
	}

	public <T> CharReader once( ICharScanner<T> s, T out ) {
		s.scan( in, out );
		return this;
	}

	@Override
	public Character peek() {
		return in.peek();
	}

	public String read( ICharScanner<ICharWriter> s ) {
		return Read.toString( in, s );
	}

	public CharReader twice( ICharProcessor p ) {
		return once( p ).once( p );
	}

	static final class ReaderAdapter
			implements IInputStream<Character>, ICharReader {

		private final Reader reader;

		private boolean ahead = false;
		private int token;

		public ReaderAdapter( Reader reader ) {
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
		public Character next() {
			readAheadIfNeeded();
			ahead = false;
			return (char) token;
		}

		@Override
		public Character peek() {
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
