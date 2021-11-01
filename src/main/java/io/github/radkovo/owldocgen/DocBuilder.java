/**
 * DocBuilder.java
 *
 * Created on 1. 11. 2021, 12:29:52 by burgetr
 */
package io.github.radkovo.owldocgen;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.vocabulary.OWL;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.query.QueryResults;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParseException;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.sail.memory.MemoryStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author burgetr
 */
public class DocBuilder
{
    private static final Logger log = LoggerFactory.getLogger(DocBuilder.class);
    
    private Repository repo;
    

    public DocBuilder(String[] filenames) throws IOException, RDFParseException
    {
        repo = new SailRepository(new MemoryStore());
        for (String filename : filenames)
            loadFile(filename);
    }

    public List<Ontology> getOntologies()
    {
        final List<Ontology> ret = new ArrayList<>();
        try (RepositoryConnection con = repo.getConnection()) {
            Set<Resource> ress = QueryResults.asModel(con.getStatements(null, RDF.TYPE, OWL.ONTOLOGY, true)).subjects();
            for (Resource res : ress)
                ret.add(new Ontology(repo, res));
        }
        return ret;
    }
    
    //=================================================================================================
    
    private void loadFile(String filename) throws IOException, RDFParseException
    {
        Path file = Paths.get(filename);
        if (!Files.exists(file)) throw new FileNotFoundException(filename);

        RDFFormat format = Rio.getParserFormatForFileName(filename).orElse(null);
        log.trace("detected input format from filename {}: {}", filename, format);

        try (final InputStream inputStream = Files.newInputStream(file))
        {
            log.trace("Loading {}", filename);
            Model model = Rio.parse(inputStream, "", format);
            addModel(model);
        }
    }
    
    private void addModel(Model model)
    {
        try (RepositoryConnection con = repo.getConnection()) {
            con.add(model);
        }
    }
    
}
