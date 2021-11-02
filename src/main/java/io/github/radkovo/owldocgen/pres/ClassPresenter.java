/**
 * ClassPresenter.java
 *
 * Created on 2. 11. 2021, 9:05:27 by burgetr
 */
package io.github.radkovo.owldocgen.pres;

import org.eclipse.rdf4j.model.vocabulary.OWL;

import io.github.radkovo.owldocgen.model.ResourceObject;

/**
 * 
 * @author burgetr
 */
public class ClassPresenter extends ResourcePresenter
{

    public ClassPresenter(ResourceObject res)
    {
        super(res);
    }
    
    @Override
    public String renderDetail()
    {
        return generateWith("class.mustache.html", this);
    }
    
    public String getSuperClasses()
    {
        return renderList("rel super", getPresenters(getRes().getSuperClasses(), OWL.CLASS), false);
    }

    public String getSubClasses()
    {
        return renderList("rel sub", getPresenters(getRes().getSubClasses(), OWL.CLASS), false);
    }
    
    public String getInDomain()
    {
        return renderList("rel domain", getPresenters(getRes().getIsInDomain(), OWL.DATATYPEPROPERTY), false);
    }

    public String getInRange()
    {
        return renderList("rel domain", getPresenters(getRes().getIsInRange(), OWL.DATATYPEPROPERTY), false);
    }

}
