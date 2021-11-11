/**
 * ExprCollectionPresenter.java
 *
 * Created on 2. 11. 2021, 15:51:11 by burgetr
 */
package io.github.radkovo.owldocgen.pres;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.rdf4j.model.vocabulary.OWL;

import io.github.radkovo.owldocgen.DocBuilder;
import io.github.radkovo.owldocgen.model.ResourceObject;

/**
 * 
 * @author burgetr
 */
public class ExprCollectionPresenter extends ClassExpressionPresenter
{
    private String operator;
    private ResourceObject spec;

    public ExprCollectionPresenter(DocBuilder builder, ResourceObject res, String operator, ResourceObject spec)
    {
        super(builder, res);
        this.operator = operator;
        this.spec = spec;
    }

    public String getOperator()
    {
        return operator;
    }
    
    public List<Item> getItems()
    {
        // get resources in the collection
        List<ResourceObject> objs = getRes().getListValues(spec.getSubject());
        // prepare for rendering
        List<Item> ret = new ArrayList<>();
        for (ResourceObject obj : objs)
        {
            Item item = new Item();
            item.obj = getBuilder().createPresenter(obj, OWL.CLASS);
            if (!ret.isEmpty())
                item.operator = this.operator;
            ret.add(item);
        }
        return ret;
    }

    @Override
    public String renderLink()
    {
        return generateWith("exprCollection", this);
    }
    
    public static class Item
    {
        String operator;
        ResourcePresenter obj;
    }

}
