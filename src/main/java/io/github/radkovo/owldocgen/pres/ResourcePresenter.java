/**
 * Presenter.java
 *
 * Created on 2. 11. 2021, 8:20:49 by burgetr
 */
package io.github.radkovo.owldocgen.pres;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.vocabulary.RDF;

import io.github.radkovo.owldocgen.model.ResourceObject;

/**
 * 
 * @author burgetr
 */
public class ResourcePresenter extends Presenter
{
    private ResourceObject res;

    public ResourcePresenter(ResourceObject res)
    {
        super();
        this.res = res;
    }

    public ResourceObject getRes()
    {
        return res;
    }
    
    public String renderLink()
    {
        return generateWith("resourceLink.mustache.html", this);
    }
    
    public String renderDetail()
    {
        return "(unknown resource detail)";
    }
    
    //==========================================================================
    
    public String getID()
    {
        return res.getID();
    }
    
    public String getLabel()
    {
        return res.getLabel();
    }
    
    public String getRdfTypes()
    {
        String ret = "res";
        List<Value> types = res.getPropertyValues(RDF.TYPE);
        for (Value v : types)
        {
            if (v instanceof IRI)
            {
                ret += " " + ((IRI) v).getLocalName();
            }
        }
        return ret;
    }
    
    public List<ResourcePresenter> getPresenters(List<ResourceObject> resources)
    {
        return resources.stream().map(r -> new ResourcePresenter(r)).collect(Collectors.toList());
    }

    @Override
    public String toString()
    {
        return "P:" + res;
    }

}
