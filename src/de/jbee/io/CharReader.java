package de.jbee.io;

public class CharReader
		implements ICharReader {

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

	@Override
	public Character peek() {
		return in.peek();
	}

	public CharReader once( ICharProcessor p ) {
		p.process( in );
		return this;
	}

	public CharReader twice( ICharProcessor p ) {
		return once( p ).once( p );
	}
}
