package de.jbee.io.json.map;

import de.jbee.io.IProcessor;
import de.jbee.io.json.IJsonProcessor;

public interface IJsonCreator<T> {

	IJsonProcessor yieldFor( IProcessor<T> accessor, IMapping mapping );

}
