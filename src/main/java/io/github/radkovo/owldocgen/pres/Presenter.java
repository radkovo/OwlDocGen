/**
 * Presenter.java
 *
 * Created on 2. 11. 2021, 8:34:17 by burgetr
 */
package io.github.radkovo.owldocgen.pres;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

/**
 * 
 * @author burgetr
 */
public class Presenter
{

    protected void generateWith(String template, Object scope, Writer writer)
    {
        try {
            MustacheFactory mf = new DefaultMustacheFactory();
            Mustache m = mf.compile(template);
            m.execute(writer, scope).flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    protected String generateWith(String template, Object scope)
    {
        StringWriter writer = new StringWriter();
        generateWith(template, scope, writer);
        return writer.toString();
    }
    
    protected String renderList(String cls, List<ResourcePresenter> list, boolean preserveEmpty)
    {
        if (preserveEmpty || (list != null && !list.isEmpty()))
        {
            ListData data = new ListData(cls, list);
            return generateWith("resourceList.mustache.html", data);
        }
        else
        {
            return null;
        }
    }

    //==================================================================================
    
    public static class ListData
    {
        public String cls;
        public List<ResourcePresenter> list;
        
        public ListData(String cls, List<ResourcePresenter> list)
        {
            super();
            this.cls = cls;
            this.list = list;
        }
    }
    
}
