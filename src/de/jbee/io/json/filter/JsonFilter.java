package de.jbee.io.json.filter;

import de.jbee.io.IProcessable;
import de.jbee.io.json.IJsonProcessor;
import de.jbee.io.json.JsonType;
import de.jbee.io.property.IPropertySelector;
import de.jbee.io.property.PropertyPath;

public class JsonFilter
		implements IJsonProcessor {

	private final IJsonProcessor processor;
	private final IPropertySelector selector;
	private final PropertyPath path;

	public JsonFilter( IJsonProcessor processor, IPropertySelector selector ) {
		this( processor, selector, PropertyPath.NONE );
	}

	JsonFilter( IJsonProcessor processor, IPropertySelector selector, PropertyPath path ) {
		super();
		this.processor = processor;
		this.selector = selector;
		this.path = path;
	}

	@Override
	public void process( JsonType type, String name, final IProcessable<IJsonProcessor> element ) {
		final PropertyPath memberPath = path.child( name );
		if ( !selector.selects( memberPath ) ) {
			element.discardBy( processor );
			return;
		}
		processor.process( type, name, new IProcessable<IJsonProcessor>() {

			@Override
			public void discardBy( IJsonProcessor processor ) {
				element.discardBy( processor );
			}

			@Override
			public void processBy( IJsonProcessor processor ) {
				element.processBy( new JsonFilter( processor, selector, memberPath ) );
			}
		} );
	}

	@Override
	public void visit( String value ) {
		System.out.println( path + " ----- " + value );
		processor.visit( value );
	}

	@Override
	public void visit( Number value ) {
		System.out.println( path + " ----- " + value );
		processor.visit( value );
	}

	@Override
	public void visit( boolean value ) {
		System.out.println( path + " ----- " + value );
		processor.visit( value );
	}

	@Override
	public void visitNull() {
		System.out.println( path + " ----- null" );
		processor.visitNull();
	}

}
