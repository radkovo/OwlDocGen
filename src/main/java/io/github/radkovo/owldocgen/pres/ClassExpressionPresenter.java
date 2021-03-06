/**
 * ClassExpressionPresenter.java
 *
 * Created on 2. 11. 2021, 15:48:48 by burgetr
 */
package io.github.radkovo.owldocgen.pres;

import io.github.radkovo.owldocgen.DocBuilder;
import io.github.radkovo.owldocgen.model.ResourceObject;

/**
 * 
 * @author burgetr
 */
public class ClassExpressionPresenter extends ClassPresenter
{

    public ClassExpressionPresenter(DocBuilder builder, ResourceObject res)
    {
        super(builder, res);
    }

    @Override
    public String renderDetail()
    {
        return "(class expression)";
    }

    @Override
    public String renderLink()
    {
        return "(class expression)";
    }

}
