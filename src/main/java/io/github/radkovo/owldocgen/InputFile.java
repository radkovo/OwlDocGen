/**
 * InputFile.java
 *
 * Created on 12. 11. 2021, 10:35:17 by burgetr
 */
package io.github.radkovo.owldocgen;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Set;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Namespace;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.vocabulary.OWL;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParseException;
import org.eclipse.rdf4j.rio.Rio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author burgetr
 */
public class InputFile
{
    private static final Logger log = LoggerFactory.getLogger(InputFile.class);
    
    private Path path;
    private Model model;
    
    
    public InputFile(String path) throws RDFParseException, IOException
    {
        loadFile(path);
    }

    public Model getModel()
    {
        return model;
    }

    public void setModel(Model model)
    {
        this.model = model;
    }

    public String toString()
    {
        return "'" + path + "'(:" + getDefaultPrefix() + ")";
    }
    
    //=================================================================================================
    
    public Set<Resource> getOntologyIRIs()
    {
        return getModel().filter(null, RDF.TYPE, OWL.ONTOLOGY).subjects();
    }
    
    public String getDefaultPrefix()
    {
        final Optional<Namespace> defns = getModel().getNamespace("");
        if (defns.isPresent())
            return defns.get().getName();
        else
            return null;
    }
    
    //=================================================================================================
    
    private void loadFile(String filename) throws IOException, RDFParseException
    {
        path = Paths.get(filename);
        if (!Files.exists(path)) throw new FileNotFoundException(filename);

        RDFFormat format = Rio.getParserFormatForFileName(filename).orElse(null);
        log.trace("detected input format from filename {}: {}", filename, format);

        try (final InputStream inputStream = Files.newInputStream(path))
        {
            log.trace("Loading {}", filename);
            Model model = Rio.parse(inputStream, "", format);
            setModel(model);
        }
    }

    
}
