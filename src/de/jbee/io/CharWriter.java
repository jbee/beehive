package de.jbee.io;

import java.io.IOException;

public final class CharWriter {

	public static ICharWriter of( Appendable out ) {
		return new AppendableAdapter( out );
	}

	private CharWriter() {
		// util
	}

	static class AppendableAdapter
			implements ICharWriter {

		private final Appendable out;

		AppendableAdapter( Appendable out ) {
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
}
