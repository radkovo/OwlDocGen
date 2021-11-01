/**
 * Ontology.java
 *
 * Created on 1. 11. 2021, 12:58:44 by burgetr
 */
package io.github.radkovo.owldocgen;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.eclipse.rdf4j.query.QueryResults;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryResult;

/**
 * 
 * @author burgetr
 */
public class ResourceObject
{
    private Repository repo;
    private Resource subject;
    private Model model;
    
    public ResourceObject(Repository repo, Resource subject)
    {
        this.repo = repo;
        this.subject = subject;
    }
    
    public Resource getSubject()
    {
        return subject;
    }

    public Model getModel()
    {
        if (model == null)
        {
            try (RepositoryConnection con = repo.getConnection()) {
                model = QueryResults.asModel(con.getStatements(subject, null, null, true));
            }
        }
        return model;
    }
    
    public Value getPropertyValue(IRI predicate)
    {
        Value ret = null;
        try (RepositoryConnection con = repo.getConnection()) {
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
    
    public String getStringProperty(IRI predicate)
    {
        Value val = getPropertyValue(predicate);
        if (val != null)
            return val.stringValue();
        else
            return null;
    }
    
    public String getLabel()
    {
        return getStringProperty(RDFS.LABEL);
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
    
}
