/**
 * Property.java
 *
 * Created on 1. 11. 2021, 13:10:27 by burgetr
 */
package io.github.radkovo.owldocgen;

import org.eclipse.rdf4j.model.Resource;

/**
 * 
 * @author burgetr
 */
public class OWLClass extends ResourceObject
{

    public OWLClass(DocBuilder builder, Resource subject)
    {
        super(builder, subject);
    }

    @Override
    public String getType()
    {
        return "class";
    }

}
