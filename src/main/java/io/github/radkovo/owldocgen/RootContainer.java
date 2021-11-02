/**
 * RootContainer.java
 *
 * Created on 1. 11. 2021, 14:01:41 by burgetr
 */
package io.github.radkovo.owldocgen;

import java.util.List;

import io.github.radkovo.owldocgen.model.Ontology;

/**
 * 
 * @author burgetr
 */
public class RootContainer
{
    private List<Ontology> ontologies;

    public List<Ontology> getOntologies()
    {
        return ontologies;
    }

    public void setOntologies(List<Ontology> ontologies)
    {
        this.ontologies = ontologies;
    }

}
