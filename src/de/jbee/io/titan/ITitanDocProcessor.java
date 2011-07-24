package de.jbee.io.titan;

import de.jbee.io.IProcessableElement;

public interface ITitanDocProcessor {

	void process( SchemeType type, IProcessableElement<ITitanScheme> scheme );

	void process( StructureType type, IProcessableElement<ITitanStructure> structure );

	void process( TextType type, IProcessableElement<ITitanText> text );

	void process( WordType type, IProcessableElement<ITitanWord> word );
}
