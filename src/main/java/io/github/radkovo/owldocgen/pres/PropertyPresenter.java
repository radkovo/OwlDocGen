/**
 * PropertyPresenter.java
 *
 * Created on 2. 11. 2021, 13:57:54 by burgetr
 */
package io.github.radkovo.owldocgen.pres;

import io.github.radkovo.owldocgen.model.ResourceObject;

/**
 * 
 * @author burgetr
 */
public class PropertyPresenter extends ResourcePresenter
{

    public PropertyPresenter(ResourceObject res)
    {
        super(res);
    }
    
    @Override
    public String renderDetail()
    {
        return generateWith("property.mustache.html", this);
    }

    public String getHasDomain()
    {
        return renderList("rel domain", getPresenters(getRes().getHasInDomain()), false);
    }

    public String getHasRange()
    {
        return renderList("rel domain", getPresenters(getRes().getHasInRange()), false);
    }

}
