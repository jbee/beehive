package de.jbee.io;

import java.io.IOException;
import java.io.Reader;

public class CharacterInputStream
		implements IInputStream<Character> {

	private final Reader reader;

	private boolean ahead = false;
	private int token;

	public CharacterInputStream( Reader reader ) {
		super();
		this.reader = reader;
	}

	@Override
	public boolean hasNext() {
		return ahead
			? tokenAvailable()
			: readAhead();
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

	private boolean tokenAvailable() {
		return token != -1;
	}

	@Override
	public Character next() {
		readAheadIfNeeded();
		ahead = false;
		return (char) token;
	}

	private void readAheadIfNeeded() {
		if ( !ahead ) {
			readAhead();
		}
		if ( !tokenAvailable() ) {
			throw new IllegalStateException( "No further tokens available!" );
		}
	}

	@Override
	public Character peek() {
		readAheadIfNeeded();
		return (char) token;
	}

}
