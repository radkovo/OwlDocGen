/**
 * Main.java
 *
 * Created on 1. 11. 2021, 11:33:06 by burgetr
 */
package io.github.radkovo.owldocgen;

import java.io.IOException;
import java.io.PrintWriter;

import org.eclipse.rdf4j.rio.RDFParseException;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

/**
 * 
 * @author burgetr
 */
public class Main
{

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        
        final String path = System.getProperty("user.home") + "/git/FitLayout.github.io/ontology/";
        String[] filenames = new String[] { path + "fitlayout.owl", path + "render.owl", path + "segmentation.owl" };
        
        
        try
        {
            DocBuilder builder = new DocBuilder(filenames);
            
            System.out.println(builder.getOntologies());
            
            RootContainer root = new RootContainer();
            root.setOntologies(builder.getOntologies());
            
            MustacheFactory mf = new DefaultMustacheFactory();
            Mustache m = mf.compile("ontology.mustache.html");
            
            m.execute(new PrintWriter(System.out), root).flush();
            
            
        } catch (RDFParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
