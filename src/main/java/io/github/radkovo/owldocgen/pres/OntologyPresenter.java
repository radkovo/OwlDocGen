/**
 * OntologyPresenter.java
 *
 * Created on 2. 11. 2021, 8:56:17 by burgetr
 */
package io.github.radkovo.owldocgen.pres;

import java.io.Writer;
import java.util.List;

import io.github.radkovo.owldocgen.DocBuilder;
import io.github.radkovo.owldocgen.model.ResourceObject;

/**
 * 
 * @author burgetr
 */
public class OntologyPresenter extends ResourcePresenter
{
    private List<ResourcePresenter> classes;
    private List<ResourcePresenter> objectProperties;
    private List<ResourcePresenter> datatypeProperties;

    public OntologyPresenter(DocBuilder builder, ResourceObject res)
    {
        super(builder, res);
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

    public boolean hasObjectProperties()
    {
        return objectProperties != null && !objectProperties.isEmpty();
    }

    public List<ResourcePresenter> getObjectProperties()
    {
        return objectProperties;
    }

    public void setObjectProperties(List<ResourcePresenter> objectProperties)
    {
        this.objectProperties = objectProperties;
    }

    public String getObjectPropertyLinks()
    {
        return renderList("subtoc", objectProperties, false);
    }

    public boolean hasDatatypeProperties()
    {
        return datatypeProperties != null && !datatypeProperties.isEmpty();
    }

    public List<ResourcePresenter> getDatatypeProperties()
    {
        return datatypeProperties;
    }

    public void setDatatypeProperties(List<ResourcePresenter> datatypeProperties)
    {
        this.datatypeProperties = datatypeProperties;
    }

    public String getDatatypePropertyLinks()
    {
        return renderList("subtoc", datatypeProperties, false);
    }

    public void renderAll(Writer w)
    {
        generateWith("ontology", this, w);
    }

}
