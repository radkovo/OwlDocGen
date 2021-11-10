/**
 * Main.java
 *
 * Created on 1. 11. 2021, 11:33:06 by burgetr
 */
package io.github.radkovo.owldocgen;

import java.io.File;
import java.io.IOException;

import org.eclipse.rdf4j.rio.RDFParseException;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

/**
 * 
 * @author burgetr
 */
@Command(name = "OwlDocGen", version = "OwlDocGen 0.1", mixinStandardHelpOptions = true)
public class Main implements Runnable
{
    @Option(names = { "-t", "--title" }, paramLabel = "title", description = "Index page title")
    private String title = "Ontologies";
    
    @Option(names = { "-p", "--prefix" }, split = ",", paramLabel = "<prefix::iri>", description = "Additional prefix definition(s)")
    private String[] namespaces = {};

    @Option(names = { "-o", "--ouptut-folder" }, paramLabel = "<path>", description = "Ouput folder path", defaultValue = ".")
    private File dest;
    
    @Parameters(paramLabel = "<filename>", description = "Input OWL files", arity = "1..*")
    private String[] filenames = {};
    
    @Override
    public void run()
    {
        try
        {
            DocBuilder builder = new DocBuilder(filenames);
            builder.setMainTitle(title);
            for (String ns : namespaces)
            {
                String[] vals = ns.split("::");
                if (vals.length == 2)
                    builder.addPrefix(vals[0].trim(), vals[1].trim());
                else
                    System.err.println("Warning: ignoring malfromed namespace definition " + ns);
            }
            
            System.err.println("Found ontologies:");
            System.err.println(builder.getOntologies());

            builder.renderAll(dest);
            builder.renderIndex(dest);
            
        } catch (RDFParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * @param args
     */
    public static void main(String[] args)
    {
        int exitCode = new CommandLine(new Main()).setUsageHelpWidth(160).execute(args); 
        System.exit(exitCode);     
    }

}
