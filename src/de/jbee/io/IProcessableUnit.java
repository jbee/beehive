package de.jbee.io;

public interface IProcessableUnit<T> {

	void processBy( T processor );

	void discardBy( T processor );

	public abstract class NoopDiscardingProcessableUnit<T>
			implements IProcessableUnit<T> {

		public final void discardBy( T processor ) {
			// just ndo nothing
		}
	}
}
