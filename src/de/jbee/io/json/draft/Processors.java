package de.jbee.io.json.draft;

import java.util.List;

public final class Processors {

	private Processors() {
		// util
	}

	static class ListAppendingProcessor<T>
			implements Processor<T> {

		private final List<T> list;

		ListAppendingProcessor( List<T> list ) {
			super();
			this.list = list;
		}

		public void process( T value ) {
			list.add( value );
		}
	}

	public static <T> Processor<T> appendsTo( List<T> list ) {
		return new ListAppendingProcessor<T>( list );
	}
}
