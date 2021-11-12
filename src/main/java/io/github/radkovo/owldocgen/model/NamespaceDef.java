/**
 * Namespace.java
 *
 * Created on 12. 11. 2021, 14:43:30 by burgetr
 */
package io.github.radkovo.owldocgen.model;

/**
 * 
 * @author burgetr
 */
public class NamespaceDef
{
    private String name;
    private String urlPrefix;
    
    public NamespaceDef(String name, String urlPrefix)
    {
        super();
        this.name = name;
        this.urlPrefix = urlPrefix;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getUrlPrefix()
    {
        return urlPrefix;
    }

    public void setUrlPrefix(String urlPrefix)
    {
        this.urlPrefix = urlPrefix;
    }
}
