package de.jbee.io.json.filter;

import de.jbee.io.ProcessableBy;
import de.jbee.io.json.JsonProcessor;
import de.jbee.io.json.JsonType;
import de.jbee.io.property.ObjectSelector;
import de.jbee.io.property.ObjectPath;

public class JsonFilter
		implements JsonProcessor {

	private final JsonProcessor processor;
	private final ObjectSelector selector;
	private final ObjectPath path;

	public JsonFilter( JsonProcessor processor, ObjectSelector selector ) {
		this( processor, selector, ObjectPath.NONE );
	}

	JsonFilter( JsonProcessor processor, ObjectSelector selector, ObjectPath path ) {
		super();
		this.processor = processor;
		this.selector = selector;
		this.path = path;
	}

	@Override
	public void process( JsonType type, String name, final ProcessableBy<JsonProcessor> element ) {
		final ObjectPath memberPath = path.child( name );
		if ( !selector.selects( memberPath ) ) {
			element.discardBy( processor );
			return;
		}
		processor.process( type, name, new ProcessableBy<JsonProcessor>() {

			@Override
			public void discardBy( JsonProcessor processor ) {
				element.discardBy( processor );
			}

			@Override
			public void processBy( JsonProcessor processor ) {
				element.processBy( new JsonFilter( processor, selector, memberPath ) );
			}
		} );
	}

	@Override
	public void process( String value ) {
		System.out.println( path + " ----- " + value );
		processor.process( value );
	}

	@Override
	public void process( Number value ) {
		System.out.println( path + " ----- " + value );
		processor.process( value );
	}

	@Override
	public void process( boolean value ) {
		System.out.println( path + " ----- " + value );
		processor.process( value );
	}

	@Override
	public void processNull() {
		System.out.println( path + " ----- null" );
		processor.processNull();
	}

}
