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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.rdf4j.model.IRI;
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
    
    private Map<String, String> namespaces; // name -> url prefix
    private Map<String, String> prefixes; // url prefix -> name
    private Repository repo;
    private List<Ontology> ontologies;
    

    public DocBuilder(String[] filenames) throws IOException, RDFParseException
    {
        namespaces = new HashMap<>();
        prefixes = new HashMap<>();
        repo = new SailRepository(new MemoryStore());
        for (String filename : filenames)
            loadFile(filename);
    }

    public Repository getRepository()
    {
        return repo;
    }
    
    public Map<String, String> getNamespaces()
    {
        return namespaces;
    }

    public Map<String, String> getPrefixes()
    {
        return prefixes;
    }

    public void addPrefix(String name, String prefix)
    {
        namespaces.put(name, prefix);
        prefixes.put(prefix, name);
    }
    
    public List<Ontology> getOntologies()
    {
        if (ontologies == null)
            ontologies = findOntologies();
        return ontologies;
    }

    //=================================================================================================
    
    protected List<Ontology> findOntologies()
    {
        final List<Ontology> ret = new ArrayList<>();
        try (RepositoryConnection con = repo.getConnection()) {
            Set<Resource> ress = QueryResults.asModel(con.getStatements(null, RDF.TYPE, OWL.ONTOLOGY, true)).subjects();
            for (Resource res : ress)
            {
                Ontology o = new Ontology(this, res);
                o.setClasses(findClassesForOntology(o));
                ret.add(o);
            }
        }
        return ret;
    }
    
    protected List<OWLClass> findClassesForOntology(Ontology o)
    {
        final List<OWLClass> ret = new ArrayList<>();
        try (RepositoryConnection con = repo.getConnection()) {
            Set<Resource> ress = QueryResults.asModel(con.getStatements(null, RDF.TYPE, OWL.CLASS, true)).subjects();
            for (Resource res : ress)
            {
                if (res instanceof IRI)
                {
                    final IRI iri = (IRI) res;
                    if (o.getPrefix().equals(iri.getNamespace()))
                        ret.add(new OWLClass(this, res));
                }
            }
        }
        return ret;
    }
    
    //=================================================================================================
    
    public String getShortIri(IRI iri)
    {
        String name = iri.getLocalName();
        String namespace = prefixes.get(iri.getNamespace());
        return (namespace != null && name != null) ? (namespace + ":" + name) : iri.toString();
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
