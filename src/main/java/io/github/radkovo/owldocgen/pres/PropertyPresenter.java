/**
 * PropertyPresenter.java
 *
 * Created on 2. 11. 2021, 13:57:54 by burgetr
 */
package io.github.radkovo.owldocgen.pres;

import org.eclipse.rdf4j.model.vocabulary.OWL;

import io.github.radkovo.owldocgen.DocBuilder;
import io.github.radkovo.owldocgen.model.ResourceObject;

/**
 * 
 * @author burgetr
 */
public class PropertyPresenter extends ResourcePresenter
{

    public PropertyPresenter(DocBuilder builder, ResourceObject res)
    {
        super(builder, res);
    }
    
    @Override
    public String renderDetail()
    {
        return generateWith("property", this);
    }

    public String getHasDomain()
    {
        return renderList("rel domain", getPresenters(getRes().getHasInDomain(), OWL.CLASS), false);
    }

    public String getHasRange()
    {
        return renderList("rel domain", getPresenters(getRes().getHasInRange(), OWL.CLASS), false);
    }

}
