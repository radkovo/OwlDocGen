/**
 * Ontology.java
 *
 * Created on 1. 11. 2021, 12:58:44 by burgetr
 */
package io.github.radkovo.owldocgen.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.impl.LinkedHashModel;
import org.eclipse.rdf4j.model.util.RDFCollections;
import org.eclipse.rdf4j.model.vocabulary.DC;
import org.eclipse.rdf4j.model.vocabulary.OWL;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.eclipse.rdf4j.query.QueryResults;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.eclipse.rdf4j.repository.util.Connections;

import io.github.radkovo.owldocgen.DocBuilder;

/**
 * 
 * @author burgetr
 */
public class ResourceObject
{
    private DocBuilder builder;
    private Resource subject;
    private Model model;
    
    public ResourceObject(DocBuilder builder, Resource subject)
    {
        this.builder = builder;
        this.subject = subject;
    }
    
    public Resource getSubject()
    {
        return subject;
    }

    public DocBuilder getBuilder()
    {
        return builder;
    }

    public boolean isLocal()
    {
        if (subject instanceof IRI)
            return builder.isLocalIRI((IRI) subject);
        else
            return false;
    }
    
    public boolean isKnown()
    {
        if (subject instanceof IRI)
            return builder.isKnownIRI((IRI) subject);
        else
            return false;
    }
    
    public String getID()
    {
        final String longId = "r-" + subject.toString().replaceAll("[^A-Za-z0-9]", "-"); 
        if (subject instanceof IRI)
        {
            IRI iri = (IRI) subject;
            if (!iri.getLocalName().isEmpty())
                return iri.getLocalName();
            else
                return longId;
        }
        else
            return longId;
    }
    
    public String getIri()
    {
        return subject.toString();
    }
    
    public String getShortIri()
    {
        if (subject instanceof IRI)
            return builder.getShortIri((IRI) subject);
        else
            return getIri();
    }
    
    public String getLabel()
    {
        String ret = getStringProperty(RDFS.LABEL);
        if (ret == null)
        {
            if (subject instanceof IRI)
            {
                ret = ((IRI) subject).getLocalName();
            }
        }
        if (ret == null)
        {
            ret = subject.toString();
        }
        return ret;
    }
    
    public String getTitle()
    {
        return getStringProperty(DC.TITLE);
    }
    
    public String getCreator()
    {
        return getStringProperty(DC.CREATOR);
    }
    
    public String getVersion()
    {
        return getStringProperty(OWL.VERSIONINFO);
    }
    
    public String getDescription()
    {
        return getStringProperty(DC.DESCRIPTION);
    }
    
    public String getComment()
    {
        return getStringProperty(RDFS.COMMENT);
    }
    
    public List<ResourceObject> getSuperClasses()
    {
        return getRelatedObjects(RDFS.SUBCLASSOF);
    }
    
    public List<ResourceObject> getSubClasses()
    {
        return getRelatedSubjects(RDFS.SUBCLASSOF);
    }
    
    public List<ResourceObject> getEquivalentClasses()
    {
        Set<ResourceObject> all = new HashSet<>();
        all.addAll(getRelatedSubjects(OWL.EQUIVALENTCLASS));
        all.addAll(getRelatedObjects(OWL.EQUIVALENTCLASS));
        List<ResourceObject> ret = new ArrayList<>(all);
        sortObjects(ret);
        return ret;
    }
    
    public List<ResourceObject> getIsInDomain()
    {
        return getRelatedSubjects(RDFS.DOMAIN);
    }
    
    public List<ResourceObject> getHasInDomain()
    {
        return getRelatedObjects(RDFS.DOMAIN);
    }
    
    public List<ResourceObject> getIsInRange()
    {
        return getRelatedSubjects(RDFS.RANGE);
    }
    
    public List<ResourceObject> getHasInRange()
    {
        return getRelatedObjects(RDFS.RANGE);
    }
    
    //==============================================================================================
    
    public Model getModel()
    {
        if (model == null)
        {
            try (RepositoryConnection con = builder.getRepository().getConnection()) {
                model = QueryResults.asModel(con.getStatements(subject, null, null, true));
            }
        }
        return model;
    }
    
    public List<Value> getPropertyValues(IRI predicate)
    {
        List<Value> ret = new ArrayList<>();
        try (RepositoryConnection con = builder.getRepository().getConnection()) {
            RepositoryResult<Statement> result = con.getStatements(subject, predicate, null, true);
            try {
                while (result.hasNext())
                {
                    ret.add(result.next().getObject());
                }
            }
            finally {
                result.close();
            }
        }
        return ret;
    }
    
    public Value getPropertyValue(IRI predicate)
    {
        Value ret = null;
        try (RepositoryConnection con = builder.getRepository().getConnection()) {
            RepositoryResult<Statement> result = con.getStatements(subject, predicate, null, true);
            try {
                if (result.hasNext())
                    ret = result.next().getObject();
            }
            finally {
                result.close();
            }
        }
        return ret;
    }
    
    public List<ResourceObject> getRelatedObjects(IRI predicate)
    {
        List<ResourceObject> ret = new ArrayList<>();
        try (RepositoryConnection con = builder.getRepository().getConnection()) {
            RepositoryResult<Statement> result = con.getStatements(subject, predicate, null, true);
            try {
                while (result.hasNext())
                {
                    Value obj = result.next().getObject();
                    if (obj instanceof Resource)
                    {
                        ret.add(new ResourceObject(builder, (Resource) obj));
                    }
                }
            }
            finally {
                result.close();
            }
        }
        sortObjects(ret);
        return ret;
    }
    
    public List<ResourceObject> getRelatedSubjects(IRI predicate)
    {
        List<ResourceObject> ret = new ArrayList<>();
        try (RepositoryConnection con = builder.getRepository().getConnection()) {
            RepositoryResult<Statement> result = con.getStatements(null, predicate, subject, true);
            try {
                while (result.hasNext())
                {
                    Resource obj = result.next().getSubject();
                    ret.add(new ResourceObject(builder, obj));
                }
            }
            finally {
                result.close();
            }
        }
        sortObjects(ret);
        return ret;
    }
    
    private void sortObjects(List<ResourceObject> list)
    {
        list.sort(new Comparator<ResourceObject>()
        {
            @Override
            public int compare(ResourceObject o1, ResourceObject o2)
            {
                return o1.getLabel().compareTo(o2.getLabel());
            }
        });
    }
    
    public String getStringProperty(IRI predicate)
    {
        Value val = getPropertyValue(predicate);
        if (val != null)
            return val.stringValue();
        else
            return null;
    }
    
    public Resource getType()
    {
        Value val = getPropertyValue(RDF.TYPE);
        if (val instanceof Resource)
            return (Resource) val;
        else
            return null;
    }
    
    public ResourceObject getObjectProperty(IRI predicate)
    {
        Value val = getPropertyValue(predicate);
        if (val != null && val instanceof Resource)
            return new ResourceObject(builder, (Resource) val);
        else
            return null;
    }
    
    public List<ResourceObject> getListValues(Resource head)
    {
        List<ResourceObject> ret = new ArrayList<>();
        try (RepositoryConnection con = builder.getRepository().getConnection()) {
            Model rdflist = Connections.getRDFCollection(con, head, new LinkedHashModel());
            List<Value> values = RDFCollections.asValues(rdflist, head, new ArrayList<>());
            for (Value val : values)
            {
                if (val instanceof Resource)
                    ret.add(new ResourceObject(builder, (Resource) val));
            }
        }
        return ret;
    }
    
    public String toString()
    {
        StringBuilder s = new StringBuilder();
        s.append("<").append(subject.toString()).append(">");
        String label = getLabel();
        if (label != null)
            s.append("(").append(label).append(")");
        return s.toString();
    }

    @Override
    public int hashCode()
    {
        return subject.toString().hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        return this.toString().equals(obj.toString());
    }
    
}
