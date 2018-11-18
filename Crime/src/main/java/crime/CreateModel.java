package crime;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.jena.graph.Triple;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.lang.PipedRDFIterator;
import org.apache.jena.riot.lang.PipedRDFStream;
import org.apache.jena.riot.lang.PipedTriplesStream;
import org.apache.log4j.BasicConfigurator;

public class CreateModel {

    public static String ontologiesBase = "http://lab.Jena.Kdeg.ie/";

    public static String relationshipBase = "http://relationships.lab.Jena.Kdeg.ie/";

    public static String baseNs;

    public static String ontologyName = "Ontology1";

    public static OntModel ontology;

    public static ArrayList<Station> Stations = new ArrayList<>();
    public static ArrayList<Division> Divisions = new ArrayList<>();

    public static void main(String args[]) throws FileNotFoundException {

        //ontologyName=args[0];
        baseNs = ontologiesBase + ontologyName + "#";
        ontology = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        BasicConfigurator.configure();

        OntClass Station = ontology.createClass(baseNs + "Station");
        OntClass Division = ontology.createClass(baseNs + "Division");
        OntClass Crime = ontology.createClass(baseNs + "Crime");
        OntClass County = ontology.createClass(baseNs + "County");

        OntClass Murder = ontology.createClass(baseNs + "Attemps_or_threats_to_murder_assaults_harassments_and_related_offences");
        OntClass Robbery = ontology.createClass(baseNs + "Robbery_extortion_and_hijacking_offences");
        OntClass Government = ontology.createClass(baseNs + "Offences_against_government_justice_procedures_and_organisations_of_crime");
        OntClass Dangerous = ontology.createClass(baseNs + "Dangerous_or_negligent_acts");
        OntClass Drug = ontology.createClass(baseNs + "Controlled_drug_offences");
        OntClass Property = ontology.createClass(baseNs + "Damage_to_property_and_to_the_environment");
        OntClass Public = ontology.createClass(baseNs + "Public_order_and_other_social_code_offences");
        OntClass Theft = ontology.createClass(baseNs + "Theft_and_related_offences");
        OntClass Fraud = ontology.createClass(baseNs + "Fraud_Deception_and_related_offences");
        OntClass Wepons = ontology.createClass(baseNs + "Wepons_and_Explosives_Offences");
        OntClass Burglary = ontology.createClass(baseNs + "Burglary_and_related_offences");
        OntClass Kidnapping = ontology.createClass(baseNs + "Kidnapping_and_related_offences");

        Crime.addSubClass(Murder);
        Crime.addSubClass(Robbery);
        Crime.addSubClass(Government);
        Crime.addSubClass(Dangerous);
        Crime.addSubClass(Drug);
        Crime.addSubClass(Property);
        Crime.addSubClass(Public);
        Crime.addSubClass(Theft);
        Crime.addSubClass(Fraud);
        Crime.addSubClass(Wepons);
        Crime.addSubClass(Burglary);
        Crime.addSubClass(Kidnapping);

        
        OntProperty hasX = ontology.createObjectProperty(baseNs + "has_X");
        OntProperty hasY = ontology.createObjectProperty(baseNs + "has_Y");
        
        Station.addProperty(hasX, "hasX");
        Station.addProperty(hasY, "hasY");
        /*OntClass Man = ontology.createClass(baseNs + "Man");
        OntClass Woman = ontology.createClass(baseNs + "Womann");

        Human.addSubClass(Man);
        Human.addSubClass(Woman);

        OntClass Burmese = ontology.createClass(baseNs + "Burmese");
        OntClass Siamese = ontology.createClass(baseNs + "Siamese");
        Cat.addSubClass(Burmese);
        Siamese.addSuperClass(Cat);

        Dog.addDisjointWith(Cat);

        OntProperty chases = ontology.createObjectProperty(baseNs + "chases");
        chases.addDomain(Dog);
        chases.addRange(Cat);

        OntProperty chasedby = ontology.createObjectProperty(baseNs + "chased_by");
        chasedby.addInverseOf(chases);

        OntProperty runsAfter = ontology.createObjectProperty(baseNs + "runs_after");
        runsAfter.addEquivalentProperty(chases);

        OntProperty eats = ontology.createObjectProperty(baseNs + "eats");
        chases.addSubProperty(eats);*/
        Individual man1 = ontology.createIndividual(baseNs + "man1", Crime);

        parser();

        for (Station station : Stations) {
            Individual ind = ontology.createIndividual(baseNs + station.Name, Station);
            ind.addProperty(hasX, station.X);
            ind.addProperty(hasY, ""+station.Y);
        }
        for (Division division : Divisions) {
            Individual ind = ontology.createIndividual(baseNs + division.Name, Division);
        }
        System.out.println("Done!");
        try {
            writeToFile(ontologyName);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            ReadModel.loadAllClassesOnt(ontologyName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static void parser() {

        String filename = "crime_rate_rdf.ttl";
        PipedRDFIterator<Triple> iter = new PipedRDFIterator<>();
        final PipedRDFStream<Triple> inputStream = new PipedTriplesStream(iter);

        // PipedRDFStream and PipedRDFIterator need to be on different threads
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // Create a runnable for our parser thread
        Runnable parser = new Runnable() {

            @Override
            public void run() {
                // Call the parsing process.
                RDFDataMgr.parse(inputStream, filename);
            }
        };

        executor.submit(parser);

        String currentSubject = "";
        Station station = new Station();

        while (iter.hasNext()) {
            Triple next = iter.next();

            if (!next.getSubject().toString().equals(currentSubject)) {
                Stations.add(station);
                station = new Station();
            }
            String nextPredicate = next.getPredicate().toString();
            if (nextPredicate.endsWith("#Station")) {
                System.out.println("Subject:  " + next.getSubject());

                System.out.println("Object:  " + next.getObject());
                station.setName(next.getObject().toString().replace(' ', '_'));

                System.out.println("Predicate:  " + next.getPredicate());
                System.out.println("\n");
            }
            else if(nextPredicate.endsWith("#x")){
                System.out.println("Subject:  " + next.getSubject());

                System.out.println("Object:  " + next.getObject());
                String x = next.getObject().toString();
                station.setX(x);

                System.out.println("Predicate:  " + next.getPredicate());
                System.out.println("\n");
            }
            else if (nextPredicate.endsWith("#Divisions")) {
                Division division = new Division();
                System.out.println("Subject:  " + next.getSubject());

                System.out.println("Object:  " + next.getObject());
                String divisionName = next.getObject().toString().replace(' ', '_');
                if (!Divisions.contains(divisionName)) {
                    division.setName(divisionName);
                    Divisions.add(division);
                }

                System.out.println("Predicate:  " + next.getPredicate());
                System.out.println("\n");
            }
            // Do something with each triple

        }
    }

    public static void writeToFile(String filename)
            throws FileNotFoundException {
        try {
            ontology.write(new FileOutputStream(new File(filename)),
                    "RDF/XML-ABBREV");
            System.out.println("Ontology written to file.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
