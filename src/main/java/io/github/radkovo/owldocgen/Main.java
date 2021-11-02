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
        
        final String path = System.getProperty("user.home") + "/git/fitlayout/FitLayout.github.io/ontology/";
        String[] filenames = new String[] { path + "fitlayout.owl", path + "render.owl", path + "segmentation.owl" };
        
        
        try
        {
            DocBuilder builder = new DocBuilder(filenames);
            builder.addPrefix("fl", "http://fitlayout.github.io/ontology/fitlayout.owl#");
            builder.addPrefix("box", "http://fitlayout.github.io/ontology/render.owl#");
            builder.addPrefix("segm", "http://fitlayout.github.io/ontology/segmentation.owl#");
            
            System.out.println(builder.getOntologies());
            
            //m.execute(new PrintWriter(System.out), root).flush();
            builder.getOntologies().get(1).renderAll(new PrintWriter(System.out));
            
        } catch (RDFParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
