package de.jbee.io;

import java.util.List;

public final class Processor {

	private Processor() {
		// util
	}

	static class ListAppendingProcessor<T>
			implements IProcessor<T> {

		private final List<T> list;

		ListAppendingProcessor( List<T> list ) {
			super();
			this.list = list;
		}

		public void process( T value ) {
			list.add( value );
		}
	}

	public static <T> IProcessor<T> appendsTo( List<T> list ) {
		return new ListAppendingProcessor<T>( list );
	}
}
