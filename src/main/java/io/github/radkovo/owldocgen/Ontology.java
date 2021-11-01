/**
 * Ontology.java
 *
 * Created on 1. 11. 2021, 13:09:18 by burgetr
 */
package io.github.radkovo.owldocgen;

import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.vocabulary.DC;
import org.eclipse.rdf4j.repository.Repository;

/**
 * 
 * @author burgetr
 */
public class Ontology extends ResourceObject
{

    public Ontology(Repository repo, Resource subject)
    {
        super(repo, subject);
    }
    
    public String getTitle()
    {
        return getStringProperty(DC.TITLE);
    }
    

}
