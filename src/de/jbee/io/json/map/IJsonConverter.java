package de.jbee.io.json.map;

import de.jbee.io.IProcessor;

public interface IJsonConverter<T> {

	void convert( String value, IProcessor<T> processor );

	void convert( Number value, IProcessor<T> processor );

	void convert( boolean value, IProcessor<T> processor );

	void convertNull( IProcessor<T> processor );
}
