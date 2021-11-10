/**
 * Ontology.java
 *
 * Created on 1. 11. 2021, 13:09:18 by burgetr
 */
package io.github.radkovo.owldocgen.model;

import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.vocabulary.RDFS;

import io.github.radkovo.owldocgen.DocBuilder;

/**
 * 
 * @author burgetr
 */
public class Ontology extends ResourceObject
{
    public Ontology(DocBuilder builder, Resource subject)
    {
        super(builder, subject);
    }
    
    public String getPrefix()
    {
        return getSubject().toString();
    }

    @Override
    public String getTitle()
    {
        String title = super.getTitle();
        if (title == null || title.trim().isEmpty())
            title = getStringProperty(RDFS.LABEL);
        if (title == null || title.trim().isEmpty())
            title = "(no title)";
        return title;
    }
}
