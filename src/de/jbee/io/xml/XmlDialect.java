package de.jbee.io.xml;

public interface XmlDialect {

	XmlContentType identifyTagContentBy( String name );

	// use another approach: 
	// -- see XML in a simplified way where we just know about tags having attributes in first place
	// -- have a context object that we can ask to 
	// 		-- correct a open-tag (might be a known empty tag)
	//		-- correct content (might just allow CDATA/plain text/some tags)
	// -- result of corrections can be totally different strings -> we read something but what we understand might be something else

}
