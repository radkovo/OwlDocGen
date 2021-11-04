/**
 * DocBuilder.java
 *
 * Created on 1. 11. 2021, 12:29:52 by burgetr
 */
package io.github.radkovo.owldocgen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
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

import io.github.radkovo.owldocgen.model.Ontology;
import io.github.radkovo.owldocgen.model.ResourceObject;
import io.github.radkovo.owldocgen.pres.ClassPresenter;
import io.github.radkovo.owldocgen.pres.DatatypePropertyPresenter;
import io.github.radkovo.owldocgen.pres.ExprCollectionPresenter;
import io.github.radkovo.owldocgen.pres.ExprRestrictionPresenter;
import io.github.radkovo.owldocgen.pres.ExprUnaryPresenter;
import io.github.radkovo.owldocgen.pres.ObjectPropertyPresenter;
import io.github.radkovo.owldocgen.pres.OntologyPresenter;
import io.github.radkovo.owldocgen.pres.ResourcePresenter;
import io.github.radkovo.owldocgen.pres.RootPresenter;

/**
 * 
 * @author burgetr
 */
public class DocBuilder
{
    private static final Logger log = LoggerFactory.getLogger(DocBuilder.class);

    public final String filenameSuffix = ".html";
    
    private String mainTitle = "Ontologies";
    private Map<String, String> namespaces; // name -> url prefix
    private Map<String, String> prefixes; // url prefix -> name
    private Map<String, String> files; // namespace url -> filename
    private Repository repo;
    private List<OntologyPresenter> ontologies;
    
    private String currentPrefix; //the prefix of the ontology currently being rendered
    

    public DocBuilder(String[] filenames) throws IOException, RDFParseException
    {
        files = new HashMap<>();
        namespaces = new HashMap<>();
        prefixes = new HashMap<>();
        initDefaultPrefixes();
        repo = new SailRepository(new MemoryStore());
        for (String filename : filenames)
            loadFile(filename);
    }

    public String getMainTitle()
    {
        return mainTitle;
    }

    public void setMainTitle(String mainTitle)
    {
        this.mainTitle = mainTitle;
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
    
    public List<OntologyPresenter> getOntologies()
    {
        if (ontologies == null)
            ontologies = findOntologies();
        return ontologies;
    }

    public void renderOntology(OntologyPresenter ontology, Writer w)
    {
        currentPrefix = ontology.getRes().getSubject().toString();
        ontology.renderAll(w);
        currentPrefix = null;
    }
    
    public void renderIndex(File destDir) throws IOException
    {
        RootPresenter root = new RootPresenter(this, getOntologies());
        File dest = new File(destDir, "index" + filenameSuffix);
        try (Writer w = new FileWriter(dest)) {
            root.renderAll(w);
        }
        log.info("Rendered: {}", dest);
    }
    
    public void renderAll(File destDir) throws IOException
    {
        for (OntologyPresenter op : getOntologies())
        {
            String filename = getOntologyFileName(op);
            File dest = new File(destDir, filename);
            try (Writer w = new FileWriter(dest)) {
                renderOntology(op, w);
            }
            log.info("Rendered: {}", dest);
        }
    }
    
    //=================================================================================================
    
    protected List<OntologyPresenter> findOntologies()
    {
        final List<OntologyPresenter> ret = new ArrayList<>();
        try (RepositoryConnection con = repo.getConnection()) {
            Set<Resource> ress = QueryResults.asModel(con.getStatements(null, RDF.TYPE, OWL.ONTOLOGY, true)).subjects();
            for (Resource res : ress)
            {
                Ontology o = new Ontology(this, res);
                assignFilename(o);
                OntologyPresenter op = new OntologyPresenter(o);
                op.setClasses(findResourcesForOntology(o, OWL.CLASS));
                op.setDatatypeProperties(findResourcesForOntology(o, OWL.DATATYPEPROPERTY));
                op.setObjectProperties(findResourcesForOntology(o, OWL.OBJECTPROPERTY));
                ret.add(op);
            }
        }
        return ret;
    }
    
    protected List<ResourcePresenter> findResourcesForOntology(Ontology o, IRI typeIRI)
    {
        final List<ResourcePresenter> ret = new ArrayList<>();
        try (RepositoryConnection con = repo.getConnection()) {
            Set<Resource> ress = QueryResults.asModel(con.getStatements(null, RDF.TYPE, typeIRI, true)).subjects();
            for (Resource res : ress)
            {
                if (res instanceof IRI)
                {
                    final IRI iri = (IRI) res;
                    if (o.getPrefix().equals(iri.getNamespace()))
                        ret.add(createPresenter(new ResourceObject(this, res), typeIRI));
                }
            }
        }
        // sort by labels
        ret.sort(new Comparator<ResourcePresenter>()
        {
            @Override
            public int compare(ResourcePresenter o1, ResourcePresenter o2)
            {
                return o1.getLabel().compareTo(o2.getLabel());
            }
        });
        return ret;
    }
    
    public ResourcePresenter createPresenter(ResourceObject res, IRI typeIRI)
    {
        if (OWL.CLASS.equals(typeIRI))
        {
            /*System.out.println(res.getSubject());
            if (!(res.getSubject() instanceof IRI))
                System.out.println(res.getModel());*/
            ResourceObject obj;
            if ((obj = res.getObjectProperty(OWL.UNIONOF)) != null)
                return new ExprCollectionPresenter(res, "or", obj);
            else if ((obj = res.getObjectProperty(OWL.INTERSECTIONOF)) != null)
                return new ExprCollectionPresenter(res, "and", obj);
            else if ((obj = res.getObjectProperty(OWL.COMPLEMENTOF)) != null)
                return new ExprUnaryPresenter(res, "not", obj);
            else if (OWL.RESTRICTION.equals(res.getType()))
            {
                // restriction subject
                ResourceObject restrictionSubj = res.getObjectProperty(OWL.ONPROPERTY);
                if (restrictionSubj == null) restrictionSubj = res.getObjectProperty(OWL.ONCLASS);
                // restriction type
                ResourceObject robj;
                String value;
                if ((robj = res.getObjectProperty(OWL.SOMEVALUESFROM)) != null)
                    return new ExprRestrictionPresenter(res, restrictionSubj, "some", robj, null);
                else if ((robj = res.getObjectProperty(OWL.ALLVALUESFROM)) != null)
                    return new ExprRestrictionPresenter(res, restrictionSubj, "only", robj, null);
                else if ((value = res.getStringProperty(OWL.CARDINALITY)) != null)
                    return new ExprRestrictionPresenter(res, restrictionSubj, "exactly", null, value);
                else if ((value = res.getStringProperty(OWL.MINCARDINALITY)) != null)
                    return new ExprRestrictionPresenter(res, restrictionSubj, "min", null, value);
                else if ((value = res.getStringProperty(OWL.MAXCARDINALITY)) != null)
                    return new ExprRestrictionPresenter(res, restrictionSubj, "max", null, value);
                else
                    return new ClassPresenter(res);
            }
            else
                return new ClassPresenter(res);
        }
        else if (OWL.DATATYPEPROPERTY.equals(typeIRI))
        {
            return new DatatypePropertyPresenter(res);
        }
        else if (OWL.OBJECTPROPERTY.equals(typeIRI))
        {
            return new ObjectPropertyPresenter(res);
        }
        else
        {
            return new ResourcePresenter(res);
        }
    }
    
    public String getOntologyFileName(OntologyPresenter op)
    {
        final String prefix = ((Ontology) op.getRes()).getPrefix();
        return files.get(prefix);
    }
    
    public String getResourceFileName(Resource resource)
    {
        if (resource instanceof IRI)
            return files.get(((IRI) resource).getNamespace());
        else
            return null;
    }
    
    private String assignFilename(Ontology o)
    {
        String name = guessFilename(o);
        // solve duplicates
        int i = 1;
        String cand = name;
        while (files.containsValue(cand))
            cand = cand + (i++);
        // add suffix
        cand += filenameSuffix;
        // store
        files.put(o.getPrefix(), cand);
        return cand;
    }
    
    private String guessFilename(Ontology o)
    {
        // start with the prefix uri
        String name = o.getPrefix();
        // find the last path component
        int i = name.lastIndexOf('/');
        if (i != -1 && i + 1 < name.length())
            name = name.substring(i + 1);
        // remove possible extensions
        i = name.lastIndexOf('.');
        if (i > 0)
            name = name.substring(0, i);
        // remove other symbols
        name = name.replaceAll("[^A-Za-z0-9_\\-]+", "");
        // don't allow empty filenames
        if (name.isEmpty())
            name = "ontology";
        return name;
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
    
    //=================================================================================================
    
    /**
     * Does the iri belong to a namespace currently being rendered?
     * @param iri
     * @return
     */
    public boolean isLocalIRI(IRI iri)
    {
        return currentPrefix != null && currentPrefix.equals(iri.getNamespace());
    }
    
    /**
     * Does the iri belong to any rendered ontology namespace?
     * @param iri
     * @return
     */
    public boolean isKnownIRI(IRI iri)
    {
        for (OntologyPresenter op : ontologies)
        {
            if (op.getRes().getSubject().toString().equals(iri.getNamespace()))
                return true;
        }
        return false;
    }
    
    protected void initDefaultPrefixes()
    {
        addPrefix("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
        addPrefix("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
        addPrefix("xsd", "http://www.w3.org/2001/XMLSchema#");
        addPrefix("owl", "http://www.w3.org/2002/07/owl#");        
    }

}
