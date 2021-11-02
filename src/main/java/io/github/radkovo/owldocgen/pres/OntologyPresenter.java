/**
 * OntologyPresenter.java
 *
 * Created on 2. 11. 2021, 8:56:17 by burgetr
 */
package io.github.radkovo.owldocgen.pres;

import java.io.Writer;
import java.util.List;

import io.github.radkovo.owldocgen.model.ResourceObject;

/**
 * 
 * @author burgetr
 */
public class OntologyPresenter extends ResourcePresenter
{
    private List<ResourcePresenter> classes;

    public OntologyPresenter(ResourceObject res)
    {
        super(res);
    }
    
    public boolean hasClasses()
    {
        return classes != null && !classes.isEmpty();
    }
    
    public List<ResourcePresenter> getClasses()
    {
        return classes;
    }

    public void setClasses(List<ResourcePresenter> classes)
    {
        this.classes = classes;
    }

    public String getClassLinks()
    {
        return renderList("subtoc", classes, false);
    }

    public void renderAll(Writer w)
    {
        generateWith("ontology.mustache.html", this, w);
    }

}
