package ie.tcd.kde;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.ModelFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ReadOntology {

    public static OntModel Ontologymodel = null;
    public static String localSource = "CrimeOntology.owl";

    public static OntModel loadAllClassesOnt() {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_LITE_MEM, null);
        try {
            m.read(new FileInputStream(localSource), null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return m;
    }

    public static String queryResult(String queryString) {

        OntModel m = loadAllClassesOnt();


        Query query = QueryFactory.create(queryString);
        QueryExecution queryExecution = QueryExecutionFactory.create(query, m);
        ResultSet results = queryExecution.execSelect();

        String resultTxt = ResultSetFormatter.asText(results);
//        System.out.println(resultTxt);
        queryExecution.close();


        return resultTxt;


    }


    public static String getQueryString(int id) {
        String query = "";

        switch (id) {
            case 0:
                query = "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                        "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                        "PREFIX ns0: <http://lab.Jena.Kdeg.ie/CrimeOntology.owl#>\n" +
                        "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                        "select (?a AS ?Station) (?x AS ?County) (?b AS ?NumberOfBurglaryReported)  where {\n" +
                        "?x a ns0:County.\n" +
                        "?x ns0:hasStations ?a.\n" +
                        "?a ns0:hasBurglary ?b.\n" +
                        "FILTER(?b!=0)\n" +
                        "} ORDER BY DESC(xsd:integer(?b)) LIMIT 1";
                break;
            case 1:
                query = "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                        "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                        "PREFIX ns0: <http://lab.Jena.Kdeg.ie/CrimeOntology.owl#>\n" +
                        "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                        "select (?a AS ?Station) (?x AS ?County) (?b AS ?NumberOfMurdersReported)  where {\n" +
                        "?x a ns0:County.\n" +
                        "?x ns0:hasStations ?a.\n" +
                        "?a ns0:hasMurder ?b.\n" +
                        "FILTER(?b!=0)\n" +
                        "}ORDER BY DESC(xsd:integer(?b)) LIMIT 1";
                break;
            case 2:
                query = "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                        "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                        "PREFIX ns0: <http://lab.Jena.Kdeg.ie/CrimeOntology.owl#>\n" +
                        "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                        "select (?a AS ?Station) (?x AS ?County) (?b AS ?NumberOfTheftsReported)  where {\n" +
                        "?x a ns0:County.\n" +
                        "?x ns0:hasStations ?a.\n" +
                        "?a ns0:hasTheft ?b.\n" +
                        "FILTER(?b!=0)\n" +
                        "}ORDER BY DESC(xsd:integer(?b)) LIMIT 1";
                break;
            case 3:
                query = "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                        "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                        "PREFIX ns0: <http://lab.Jena.Kdeg.ie/CrimeOntology.owl#>\n" +
                        "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                        "select (?a AS ?Station) (?x AS ?County) (?b AS ?NumberOfDangerousReported) where {\n" +
                        "?x a ns0:County.\n" +
                        "?x ns0:hasStations ?a.\n" +
                        "?a ns0:hasDangerous ?b.\n" +
                        "FILTER(?b!=0)\n" +
                        "}ORDER BY DESC(xsd:integer(?b)) LIMIT 1";
                break;

            case 4:
                query = "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                        "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                        "PREFIX ns0: <http://lab.Jena.Kdeg.ie/CrimeOntology.owl#>\n" +
                        "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                        "select (?x AS ?County) (COUNT(?a) AS ?TotalNumberOfStations) WHERE{\n" +
                        "?x a ns0:County.\n" +
                        "?x ns0:hasStations ?a\n" +
                        "} GROUP BY ?x";
                break;
            case 5:
                query = "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                        "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                        "PREFIX ns0: <http://lab.Jena.Kdeg.ie/CrimeOntology.owl#>\n" +
                        "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                        "select (?a AS ?Station) (?x AS ?County) (?b AS ?TotalCrimes)  where {\n" +
                        "?x a ns0:County.\n" +
                        "?x ns0:hasStations ?a.\n" +
                        "?a ns0:totalCrime ?b\n" +
                        "} ORDER BY DESC(xsd:int(?b))";
                break;
            case 6:
                query = "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                        "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                        "PREFIX ns0: <http://lab.Jena.Kdeg.ie/CrimeOntology.owl#>\n" +
                        "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                        "select  (?a AS ?Station) (?x AS ?County) (?b AS ?TypesOfCrimes)  where {\n" +
                        "?x a ns0:County.\n" +
                        "?x ns0:hasStations ?a.\n" +
                        "?a ns0:hasCrime ?b}";
                break;
            case 7:
                query = "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                        "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                        "PREFIX ns0: <http://lab.Jena.Kdeg.ie/CrimeOntology.owl#>\n" +
                        "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                        "select  (?x AS ?County) (COUNt(?a) AS ?AdjacentCounty)   where {\n" +
                        "?x a ns0:County.\n" +
                        "?x ns0:adjacentTo ?a\n" +
                        "} GROUP BY ?x ";
                break;

            case 8:
                query = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                        "PREFIX ns0: <http://lab.Jena.Kdeg.ie/CrimeOntology.owl#>\n" +
                        "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                        "select  (?x AS ?County) (?a AS ?Gaelic) (?b AS ?EngName) where {\n" +
                        "?x a ns0:County.\n" +
                        "?x rdfs:label ?a.\n" +
                        "?x rdfs:label ?b.\n" +
                        "FILTER(langMatches(lang(?a), \"GA\"))\n" +
                        "FILTER(langMatches(lang(?b), \"EN\"))}";
                break;
            case 9:
                query = "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                        "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                        "PREFIX ns0: <http://lab.Jena.Kdeg.ie/CrimeOntology.owl#>\n" +
                        "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                        "SELECT *  WHERE {\n" +
                        "select (?a AS ?Station) (?x AS ?County) (?b AS ?NumberOfDangerousReported) where {\n" +
                        "?x a ns0:County.\n" +
                        "?x ns0:hasStations ?a.\n" +
                        "?a ns0:totalCrime ?b.\n" +
                        "FILTER(?b!=0)\n" +
                        "}ORDER BY DESC(xsd:integer(?b)) LIMIT 2} \n" +
                        "ORDER BY (?NumberOfDangerousReported) LIMIT 1";
                break;
            default:

                break;


        }

        return query;
    }

}