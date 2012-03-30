package de.jbee.io.json.draft;

import de.jbee.io.json.JsonProcessor;

public interface IJsonCreator<T> {

	JsonProcessor yieldFor( Processor<T> accessor, IMapping mapping );

}
