/**
 * ExprRestrictionPresenter.java
 *
 * Created on 3. 11. 2021, 14:18:08 by burgetr
 */
package io.github.radkovo.owldocgen.pres;

import org.eclipse.rdf4j.model.vocabulary.OWL;

import io.github.radkovo.owldocgen.DocBuilder;
import io.github.radkovo.owldocgen.model.ResourceObject;

/**
 * 
 * @author burgetr
 */
public class ExprRestrictionPresenter extends ClassExpressionPresenter
{
    private ResourceObject restrictionSubject;
    private String type;
    private ResourceObject obj;
    private String value;

    public ExprRestrictionPresenter(DocBuilder builder, ResourceObject res,
            ResourceObject restrictionSubject, String type, ResourceObject obj, String value)
    {
        super(builder, res);
        this.restrictionSubject = restrictionSubject;
        this.type = type;
        this.obj = obj;
        this.value = value;
    }

    public ResourceObject getRestrictionSubject()
    {
        return restrictionSubject;
    }

    public ResourcePresenter getSubjPres()
    {
        return getBuilder().createPresenter(restrictionSubject, OWL.CLASS);
    }

    public String getType()
    {
        return type;
    }

    public ResourceObject getObj()
    {
        return obj;
    }

    public ResourcePresenter getObjPres()
    {
        if (obj != null)
            return getBuilder().createPresenter(obj, OWL.CLASS);
        else
            return null;
    }

    public String getValue()
    {
        return value;
    }
    
    @Override
    public String renderLink()
    {
        return generateWith("exprRestriction", this);
    }
    
}
