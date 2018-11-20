import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.ModelFactory;

public class ReadOntology {
public static void main (String[]args) throws FileNotFoundException {
    	
		queryResult(loadAllClassesOnt());
    }

	public static OntModel Ontologymodel = null;
	public static String localSource = "D:\\Subjects\\KDE\\Assignment-2\\CS7IS1-master (1)\\CS7IS1-master\\Crime\\CrimeOntology.owl";
	
	public static OntModel loadAllClassesOnt() throws FileNotFoundException {
		OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_LITE_MEM, null);
        m.read(new FileInputStream(localSource),null);
        return m;
	}
	
	public static void queryResult (OntModel m) {
		
		String queryString = "PREFIX owl: <http://www.w3.org/2002/07/owl#> \r\n" + 
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \r\n" + 
				"PREFIX ns0: <http://lab.Jena.Kdeg.ie/CrimeOntology#> \r\n" + 
				"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> \r\n" + 
				"\r\n" + 
				"select ?noKidnapping where {\r\n" + 
				"	?noKidnapping ns0:hasKidnapping \"0\".}";
		
		Query query = QueryFactory.create(queryString);
		QueryExecution queryExecution = QueryExecutionFactory.create(query, m);
		ResultSet results = queryExecution.execSelect();

        String resultTxt = ResultSetFormatter.asText(results);
        System.out.println(resultTxt);
        queryExecution.close();
        
        
		
		
	}
	
}
