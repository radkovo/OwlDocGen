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

import io.github.radkovo.owldocgen.DocBuilder;
import io.github.radkovo.owldocgen.model.ResourceObject;

/**
 * 
 * @author burgetr
 */
public class ResourcePresenter extends Presenter
{
    private ResourceObject res;

    public ResourcePresenter(DocBuilder builder, ResourceObject res)
    {
        super(builder);
        this.res = res;
    }

    public ResourceObject getRes()
    {
        return res;
    }
    
    public DocBuilder getBuilder()
    {
        return res.getBuilder();
    }
    
    public String renderLink()
    {
        if (getRes().isLocal())
            return generateWith("resourceLinkLocal", this);
        else if (getRes().isKnown())
            return generateWith("resourceLinkKnown", this);
        else
            return generateWith("resourceLinkOther", this);
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
    
    public String getTitle()
    {
        return res.getTitle();
    }
    
    public String getIri()
    {
        return res.getIri();
    }
    
    public String getShortIri()
    {
        return res.getShortIri();
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
    
    public String getFileName()
    {
        return getBuilder().getResourceFileName(getRes().getSubject());
    }
    
    public List<ResourcePresenter> getPresenters(List<ResourceObject> resources, IRI typeIRI)
    {
        return resources.stream().map(r -> getBuilder().createPresenter(r, typeIRI)).collect(Collectors.toList());
    }

    @Override
    public String toString()
    {
        return "P:" + res;
    }

}
