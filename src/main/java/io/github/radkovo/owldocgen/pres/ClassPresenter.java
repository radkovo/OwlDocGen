/**
 * ClassPresenter.java
 *
 * Created on 2. 11. 2021, 9:05:27 by burgetr
 */
package io.github.radkovo.owldocgen.pres;

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
        return renderList("rel super", getPresenters(getRes().getSuperClasses()), false);
    }

    public String getSubClasses()
    {
        return renderList("rel sub", getPresenters(getRes().getSubClasses()), false);
    }

}
