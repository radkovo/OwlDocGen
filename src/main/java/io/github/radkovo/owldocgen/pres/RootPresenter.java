/**
 * RootPresenter.java
 *
 * Created on 3. 11. 2021, 12:37:17 by burgetr
 */
package io.github.radkovo.owldocgen.pres;

import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import io.github.radkovo.owldocgen.DocBuilder;

/**
 * 
 * @author burgetr
 */
public class RootPresenter extends Presenter
{
    private List<OntologyPresenter> ontologies;

    public RootPresenter(DocBuilder builder, List<OntologyPresenter> ontologies)
    {
        super(builder);
        this.ontologies = ontologies;
    }

    public List<OntologyPresenter> getOntologies()
    {
        return ontologies;
    }
    
    public String getTitle()
    {
        return getBuilder().getMainTitle();
    }
    
    public List<OntologyInfo> getOntologyInfo()
    {
        List<OntologyInfo> ret = new ArrayList<>();
        for (OntologyPresenter op : getOntologies())
        {
            OntologyInfo info = new OntologyInfo();
            info.o = op;
            info.filename = getBuilder().getOntologyFileName(op);
            ret.add(info);
        }
        return ret;
    }
    
    public void renderAll(Writer w)
    {
        generateWith("index", this, w);
    }
    
    public static class OntologyInfo
    {
        public OntologyPresenter o;
        public String filename;
    }

}
