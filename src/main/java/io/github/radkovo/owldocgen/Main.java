/**
 * Main.java
 *
 * Created on 1. 11. 2021, 11:33:06 by burgetr
 */
package io.github.radkovo.owldocgen;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import org.eclipse.rdf4j.rio.RDFParseException;

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
            builder.setMainTitle("FitLayout Ontologies");
            builder.addPrefix("fl", "http://fitlayout.github.io/ontology/fitlayout.owl#");
            builder.addPrefix("box", "http://fitlayout.github.io/ontology/render.owl#");
            builder.addPrefix("segm", "http://fitlayout.github.io/ontology/segmentation.owl#");
            
            System.out.println(builder.getOntologies());

            /*Writer w = new FileWriter("/tmp/out.html");
            builder.renderOntology(builder.getOntologies().get(1), w);
            w.close();*/
            File dest = new File("/tmp/gen");
            builder.renderAll(dest);
            builder.renderIndex(dest);
            
        } catch (RDFParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
