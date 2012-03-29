package de.jbee.io.json.map;

import de.jbee.io.IProcessor;
import de.jbee.io.json.JsonProcessor;

public interface IJsonCreator<T> {

	JsonProcessor yieldFor( IProcessor<T> accessor, IMapping mapping );

}
