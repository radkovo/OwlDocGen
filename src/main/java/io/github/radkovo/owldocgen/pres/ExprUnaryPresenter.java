/**
 * ExprUnaryPresenter.java
 *
 * Created on 3. 11. 2021, 13:45:03 by burgetr
 */
package io.github.radkovo.owldocgen.pres;

import org.eclipse.rdf4j.model.vocabulary.OWL;

import io.github.radkovo.owldocgen.model.ResourceObject;

/**
 * 
 * @author burgetr
 */
public class ExprUnaryPresenter extends ClassExpressionPresenter
{
    private String op;
    private ResourceObject obj;

    public ExprUnaryPresenter(ResourceObject res, String operator, ResourceObject obj)
    {
        super(res);
        this.op = operator;
        this.obj = obj;
    }

    public String getOp()
    {
        return op;
    }

    public ResourceObject getObj()
    {
        return obj;
    }
    
    public ResourcePresenter getObjPres()
    {
        return getBuilder().createPresenter(obj, OWL.CLASS);
    }

    @Override
    public String renderLink()
    {
        return generateWith("exprUnary.mustache.html", this);
    }

}
