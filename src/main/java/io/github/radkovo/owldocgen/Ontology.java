/**
 * Ontology.java
 *
 * Created on 1. 11. 2021, 13:09:18 by burgetr
 */
package io.github.radkovo.owldocgen;

import java.util.List;

import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.vocabulary.DC;
import org.eclipse.rdf4j.model.vocabulary.OWL;
import org.eclipse.rdf4j.model.vocabulary.RDFS;

/**
 * 
 * @author burgetr
 */
public class Ontology extends ResourceObject
{
    private List<OWLClass> classes;
    

    public Ontology(DocBuilder builder, Resource subject)
    {
        super(builder, subject);
    }
    
    public String getPrefix()
    {
        return getSubject().toString();
    }
    
    public List<OWLClass> getClasses()
    {
        return classes;
    }
    
    public boolean hasClasses()
    {
        return (classes != null && !classes.isEmpty());
    }

    public void setClasses(List<OWLClass> classes)
    {
        this.classes = classes;
    }

}
