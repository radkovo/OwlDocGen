/**
 * Property.java
 *
 * Created on 1. 11. 2021, 13:10:27 by burgetr
 */
package io.github.radkovo.owldocgen;

import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.repository.Repository;

/**
 * 
 * @author burgetr
 */
public class Property extends ResourceObject
{

    public Property(Repository repo, Resource subject)
    {
        super(repo, subject);
    }

}
