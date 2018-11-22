package crime;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.jena.graph.Triple;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.lang.PipedRDFIterator;
import org.apache.jena.riot.lang.PipedRDFStream;
import org.apache.jena.riot.lang.PipedTriplesStream;
import org.apache.jena.vocabulary.RDFS;
import org.apache.log4j.BasicConfigurator;
import com.esri.core.geometry.*;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.Ontology;
import org.apache.jena.ontology.SymmetricProperty;
import org.apache.jena.rdf.model.RDFList;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.XSD;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;
import uk.me.jstott.jcoord.LatLng;
import uk.me.jstott.jcoord.IrishRef;

public class CreateModel {

    public static String ontologiesBase = "http://lab.Jena.Kdeg.ie/";

    public static String relationshipBase = "http://relationships.lab.Jena.Kdeg.ie/";

    public static String baseNs;

    public static String ontologyName = "CrimeOntology.owl";

    public static OntModel ontology;

    public static ArrayList<Station> Stations = new ArrayList<>();
    public static ArrayList<Division> Divisions = new ArrayList<>();
    public static ArrayList<Crime> Crimes = new ArrayList<>();

    public static void main(String args[]) throws FileNotFoundException {

        baseNs = ontologiesBase + ontologyName + "#";
        ontology = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        BasicConfigurator.configure();
        Ontology ont = ontology.createOntology(baseNs);
        ont.addComment("List of all Crimes per Garda Station in Ireland - 2015", null);
        ont.addProperty(DCTerms.abstract_, "");
        ont.addProperty(DCTerms.creator, "Nicholas");
        ont.addProperty(DCTerms.creator, "Anirban");
        ont.addProperty(DCTerms.creator, "Tomin");
        ont.addProperty(DCTerms.creator, "Neeraj");
        ont.addProperty(DCTerms.creator, "Lovish");

//County
        OntClass County = ontology.createClass(baseNs + "County");
        County.setLabel("County", null);
        County.setComment("County information", null);

        DatatypeProperty area = ontology.createDatatypeProperty(baseNs + "area");
        area.setDomain(County);
        area.setRange(XSD.xfloat);
        area.setLabel("area", null);
        area.setComment("County Area", null);

        SymmetricProperty adjacentTo = ontology.createSymmetricProperty(baseNs + "adjacentTo");
        adjacentTo.setDomain(County);
        adjacentTo.setRange(County);
        adjacentTo.setLabel("adjacentTo", null);
        adjacentTo.setComment("Adject to list of counties", null);

/*Division 
        OntClass Division = ontology.createClass(baseNs + "Division");
        Division.setLabel("Division", null);
        Division.setComment("Division information", null);

        DatatypeProperty divisionName = ontology.createDatatypeProperty(baseNs + "DivisionName");
        divisionName.setDomain(Division);
        divisionName.setRange(XSD.xstring);
        divisionName.setLabel("divisionName", null);
        divisionName.setComment("divisionName", null);

*/
//Station

        OntClass Station = ontology.createClass(baseNs + "Station");
        Station.setLabel("Station", null);
        Station.setComment("Station information", null);

        DatatypeProperty stationId = ontology.createDatatypeProperty(baseNs + "StationId");
        stationId.setDomain(Station);
        stationId.setRange(XSD.nonNegativeInteger);
        stationId.setLabel("stationId", null);
        stationId.setComment("Station ID", null);

        DatatypeProperty stationName = ontology.createDatatypeProperty(baseNs + "StationName");
        stationName.setDomain(Station);
        stationName.setRange(XSD.xstring);
        stationName.setLabel("stationName", null);
        stationName.setComment("Station Name", null);

/*        DatatypeProperty inDivision = ontology.createDatatypeProperty(baseNs + "inDivision");
        inDivision.setDomain(Station);
        inDivision.setRange(Division);
        inDivision.setLabel("inDivision", null);
        inDivision.setComment("Division information per Station", null);
*/
//Crime        
        OntClass Murder = ontology.createClass(baseNs + "Attemps_or_threats_to_murder_assaults_harassments_and_related_offences");
        Murder.setLabel("Murder", null);
        Murder.setComment("Murder crime statistics", null);

        OntClass Robbery = ontology.createClass(baseNs + "Robbery_extortion_and_hijacking_offences");
        Robbery.setLabel("Robbery", null);
        Robbery.setComment("Robbery crime statistics", null);

        OntClass Government = ontology.createClass(baseNs + "Offences_against_government_justice_procedures_and_organisations_of_crime");
        Government.setLabel("Government", null);
        Government.setComment("Crimes against government statistics", null);

        OntClass Dangerous = ontology.createClass(baseNs + "Dangerous_or_negligent_acts");
        Dangerous.setLabel("Dangerous", null);
        Dangerous.setComment("Dangerous act statistics", null);

        OntClass Drug = ontology.createClass(baseNs + "Controlled_drug_offences");
        Drug.setLabel("Drug", null);
        Drug.setComment("Drug abuse statistics", null);

        OntClass Property = ontology.createClass(baseNs + "Damage_to_property_and_to_the_environment");
        Property.setLabel("Property", null);
        Property.setComment("Property crime statistics", null);

        OntClass Public = ontology.createClass(baseNs + "Public_order_and_other_social_code_offences");
        Public.setLabel("Public", null);
        Public.setComment("Public offence statistics", null);

        OntClass Theft = ontology.createClass(baseNs + "Theft_and_related_offences");
        Theft.setLabel("Theft", null);
        Theft.setComment("Theft crime statistics", null);

        OntClass Fraud = ontology.createClass(baseNs + "Fraud_Deception_and_related_offences");
        Fraud.setLabel("Fraud", null);
        Fraud.setComment("Fraud statistics", null);

        OntClass Weapons = ontology.createClass(baseNs + "Weapons_and_Explosives_Offences");
        Weapons.setLabel("Weapons", null);
        Weapons.setComment("Weapon crime statistics", null);

        OntClass Burglary = ontology.createClass(baseNs + "Burglary_and_related_offences");
        Burglary.setLabel("Burglary", null);
        Burglary.setComment("Burglary statistics", null);

        OntClass Kidnapping = ontology.createClass(baseNs + "Kidnapping_and_related_offences");
        Kidnapping.setLabel("Kidnapping", null);
        Kidnapping.setComment("Kidnapping statistics", null);

        RDFList crimeList = ontology.createList(new RDFNode[]{
            Murder,
            Robbery,
            Government,
            Dangerous,
            Drug,
            Property,
            Public,
            Theft,
            Weapons,
            Burglary,
            Kidnapping
        });
        OntClass Crime = ontology.createUnionClass(baseNs + "Crime", crimeList);
        Crime.setComment("Crime superclass", null);

        Crime.addSubClass(Murder);
        Crime.addSubClass(Robbery);
        Crime.addSubClass(Government);
        Crime.addSubClass(Dangerous);
        Crime.addSubClass(Drug);
        Crime.addSubClass(Property);
        Crime.addSubClass(Public);
        Crime.addSubClass(Theft);
        Crime.addSubClass(Fraud);
        Crime.addSubClass(Weapons);
        Crime.addSubClass(Burglary);
        Crime.addSubClass(Kidnapping);

//
        OntClass Severity = ontology.createClass(baseNs + "Severity");
        Severity.setLabel("Severity", null);
        Severity.setComment("", null);

        //Station.setDisjointWith(Division);

        OntProperty hasX = ontology.createObjectProperty(baseNs + "has_X");
        hasX.setLabel("hasX", null);
        hasX.setComment("X coordinate information per station", null);
        hasX.setDomain(Station);

        OntProperty hasY = ontology.createObjectProperty(baseNs + "has_Y");
        hasY.setLabel("hasY", null);
        hasY.setComment("Y coordinate information per station", null);
        hasY.setDomain(Station);

        OntProperty hasCrime = ontology.createObjectProperty(baseNs + "hasCrime");
        hasCrime.setLabel("hasCrime", null);
        hasCrime.setComment("Crime statistics per Station", null);
        hasCrime.setDomain(Station);
        hasCrime.setRange(Crime);

        OntProperty totalCrime = ontology.createObjectProperty(baseNs + "totalCrime");
        totalCrime.setLabel("totalCrime", null);
        totalCrime.setComment("Crime counts per Station", null);
        totalCrime.setDomain(Station);
        totalCrime.setRange(Crime);

        Station.addProperty(hasX, "hasX");
        Station.addProperty(hasY, "hasY");
        Station.addProperty(hasCrime, "hasCrime");
        
        ObjectProperty hasStations = ontology.createObjectProperty(baseNs + "hasStations");
        //Division.addProperty(hasStations, "hasStations");
        County.addProperty(hasStations, "hasStations");
        hasStations.setDomain(County);
        hasStations.setRange(Station);
        hasStations.setLabel("hasStations", null);
        hasStations.setComment("Stations per County", null);
        
        /*
        ObjectProperty hasDivisions = ontology.createObjectProperty(baseNs + "hasDivisions");
        County.addProperty(hasDivisions, "hasDivisions");
        hasDivisions.setLabel("hasDivisions", null);
        hasDivisions.setComment("Divisions per County", null);
        hasDivisions.setDomain(County);
        hasDivisions.setRange(Division);
        */
        
        DatatypeProperty inCounty = ontology.createDatatypeProperty(baseNs + "inCounty");
        inCounty.setDomain(Station);
        inCounty.setRange(County);
        inCounty.setLabel("inCounty", null);
        inCounty.setComment("County location per division", null);
        
        inCounty.addInverseOf(hasStations);
        hasStations.addInverseOf(inCounty);

//        ObjectProperty isStationIn = ontology.createObjectProperty(baseNs + "isStationIn");
//        Station.addProperty(isStationIn, "isStationIn");
//        isStationIn.setLabel("isStationIn", null);
//        isStationIn.setComment("Station's division", null);
//        isStationIn.setDomain(Station);
//        isStationIn.setRange(Division);
        ObjectProperty hasMurder = ontology.createObjectProperty(baseNs + "hasMurder");
        Station.addProperty(hasMurder, "hasMurder");
        hasMurder.setLabel("hasMurder", null);
        hasMurder.setComment("Murder statistics per station", null);
        hasMurder.setDomain(Crime);
        hasMurder.setRange(Station);

        ObjectProperty hasRobbery = ontology.createObjectProperty(baseNs + "hasRobbery");
        Station.addProperty(hasRobbery, "hasRobbery");
        hasRobbery.setLabel("hasRobbery", null);
        hasRobbery.setComment("Robbery statistics per station", null);
        hasRobbery.setDomain(Crime);
        hasRobbery.setRange(Station);

        ObjectProperty hasGovernment = ontology.createObjectProperty(baseNs + "hasGovernment");
        Station.addProperty(hasGovernment, "hasGovernment");
        hasGovernment.setLabel("hasGovernment", null);
        hasGovernment.setComment("Crimes against government statistics per station", null);
        hasGovernment.setDomain(Crime);
        hasGovernment.setRange(Station);

        ObjectProperty hasDangerous = ontology.createObjectProperty(baseNs + "hasDangerous");
        Station.addProperty(hasDangerous, "hasDangerous");
        hasDangerous.setLabel("hasDangerous", null);
        hasDangerous.setComment("Dangerous action statistics per station", null);
        hasDangerous.setDomain(Crime);
        hasDangerous.setRange(Station);

        ObjectProperty hasDrug = ontology.createObjectProperty(baseNs + "hasDrug");
        Station.addProperty(hasDrug, "hasDrug");
        hasDrug.setLabel("hasDrug", null);
        hasDrug.setComment("Drug statistics per station", null);
        hasDrug.setDomain(Crime);
        hasDrug.setRange(Station);

        ObjectProperty hasProperty = ontology.createObjectProperty(baseNs + "hasProperty");
        Station.addProperty(hasProperty, "hasProperty");
        hasProperty.setLabel("hasProperty", null);
        hasProperty.setComment("Property crime statistics per station", null);
        hasProperty.setDomain(Crime);
        hasProperty.setRange(Station);

        ObjectProperty hasPublic = ontology.createObjectProperty(baseNs + "hasPublic");
        Station.addProperty(hasPublic, "hasPublic");
        hasPublic.setLabel("hasPublic", null);
        hasPublic.setComment("Public offence statistics per station", null);
        hasPublic.setDomain(Crime);
        hasPublic.setRange(Station);

        ObjectProperty hasTheft = ontology.createObjectProperty(baseNs + "hasTheft");
        Station.addProperty(hasTheft, "hasTheft");
        hasTheft.setLabel("hasTheft", null);
        hasTheft.setComment("Theft statistics per station", null);
        hasTheft.setDomain(Crime);
        hasTheft.setRange(Station);

        ObjectProperty hasFraud = ontology.createObjectProperty(baseNs + "hasFraud");
        Station.addProperty(hasFraud, "hasFraud");
        hasFraud.setLabel("hasFraud", null);
        hasFraud.setComment("Fraud statistics per station", null);
        hasFraud.setDomain(Crime);
        hasFraud.setRange(Station);

        ObjectProperty hasWeapons = ontology.createObjectProperty(baseNs + "hasWeapons");
        Station.addProperty(hasWeapons, "hasWeapons");
        hasWeapons.setLabel("hasWeapons", null);
        hasWeapons.setComment("Weapon statistics per station", null);
        hasWeapons.setDomain(Crime);
        hasWeapons.setRange(Station);

        ObjectProperty hasBurglary = ontology.createObjectProperty(baseNs + "hasBurglary");
        Station.addProperty(hasBurglary, "hasBurglary");
        hasBurglary.setLabel("hasBurglary", null);
        hasBurglary.setComment("Burglary statistics per station", null);
        hasBurglary.setDomain(Crime);
        hasBurglary.setRange(Station);

        ObjectProperty hasKidnapping = ontology.createObjectProperty(baseNs + "hasKidnapping");
        Station.addProperty(hasKidnapping, "hasKidnapping");
        hasKidnapping.setLabel("hasKidnapping", null);
        hasKidnapping.setComment(" statistics per station", null);
        hasKidnapping.setDomain(Crime);
        hasKidnapping.setRange(Station);

        parser();

        Divisions.forEach((division) -> {
            /*Individual ind = ontology.createIndividual(baseNs + division.Name, Division);

            division.stations.forEach((action) -> {
                ind.addProperty(hasStations, action.Name);
            });
*/
        });

        Crimes.forEach((crime) -> {
        });
        System.out.println("Done!");

        //INDIVIDUALS
        Model countyRDF = RDFDataMgr.loadModel("county.ttl");

        ArrayList<ArrayList<Object>> countyList = new ArrayList<>();
        ResIterator countyResIter = countyRDF.listResourcesWithProperty(RDFS.label);

        Property hasGeometry = countyRDF.getProperty("http://www.opengis.net/ont/geosparql#hasGeometry");
        Property asWKT = countyRDF.getProperty("http://www.opengis.net/ont/geosparql#asWKT");

        while (countyResIter.hasNext()) {
            Resource res = countyResIter.next();

            // labels
            NodeIterator labelsIter = countyRDF.listObjectsOfProperty(res, RDFS.label);
            List<RDFNode> labels = labelsIter.toList();
            String idLabel = "";
            String gaLabel = "";
            String enLabel = "";

            for (RDFNode label : labels) {
                Literal name = label.asLiteral();
                if (name.getLanguage().equals("ga")) {
                    gaLabel = name.getString();
                } else if (name.getLanguage().equals("en")) {
                    enLabel = name.getString();
                } else {
                    idLabel = name.getString();
                }
            }

            // WKT
            Resource geoResource = countyRDF.listObjectsOfProperty(res, hasGeometry).next().asResource();
            String wkt = countyRDF.listObjectsOfProperty(geoResource, asWKT).next().toString();
            wkt = wkt.substring(0, wkt.indexOf("^^"));
            OperatorImportFromWkt importer = OperatorImportFromWkt.local();
            Geometry geometry = importer.execute(WktImportFlags.wktImportDefaults, Geometry.Type.Unknown, wkt, null);

            ArrayList<Object> info = new ArrayList<>();
            info.add(idLabel);
            info.add(enLabel);
            info.add(gaLabel);
            info.add(geometry);
            // TODO: AREA unit correctness, add approximate scale for now. based on dublin
            float scale = 7365.0f;
            info.add((float) geometry.calculateArea2D() * scale);

            countyList.add(info);
        }

        countyList.sort(new Comparator<ArrayList<Object>>() {
            @Override
            public int compare(ArrayList<Object> o1, ArrayList<Object> o2) {
                float area1 = (float) o1.get(4);
                float area2 = (float) o2.get(4);
                return Float.compare(area2, area1);
            }
        });

        ArrayList<Individual> countyIndiList = new ArrayList<>();
        for (ArrayList<Object> info : countyList) {
            Individual aCounty = County.createIndividual(baseNs + info.get(0));
            aCounty.addLabel((String) info.get(0), null);
            aCounty.addLabel((String) info.get(1), "en");
            aCounty.addLabel((String) info.get(2), "ga");
            aCounty.addLiteral(area, (float) info.get(4));

            countyIndiList.add(aCounty);
        }

        for (Individual countyIndi : countyIndiList) {
            int curIdx = countyIndiList.indexOf(countyIndi);
            Geometry curGeometry = (Geometry) countyList.get(curIdx).get(3);

            for (int i = curIdx + 1; i < countyIndiList.size(); i++) {
                if (i == curIdx) {
                    continue;
                }
                Individual otherCountyIndi = countyIndiList.get(i);
                if (i > curIdx) {
                    //countyIndi.addProperty(biggerThan, otherCountyIndi);
                }
                Geometry otherGeometry = (Geometry) countyList.get(i).get(3);
                // TODO: Intersection not all correct, for instance, dublin
                OperatorIntersects intersects = OperatorIntersects.local();
                if (intersects.execute(curGeometry, otherGeometry, SpatialReference.create("WGS84"), null)) {
                    countyIndi.addProperty(adjacentTo, otherCountyIndi);
                }
            }
        }

        Stations.forEach((station) -> {
            Individual ind = ontology.createIndividual(baseNs + station.Name, Station);
            //ind.addProperty(hasX, "" + station.X);
            //ind.addProperty(hasY, "" + station.Y);
            ind.addLiteral(hasX, ontology.createTypedLiteral(station.X, XSDDatatype.XSDnonNegativeInteger));
            ind.addLiteral(hasY, ontology.createTypedLiteral(station.Y, XSDDatatype.XSDnonNegativeInteger));
            if (station.County != null) {
                //String name = station.division.Name.replace('\'', ' ').replace('"', ' ').trim();
                String name = station.County;
                ind.addProperty(inCounty, name);
            }
            if (station.crime != null) {
                String crimes = "";

                ind.addProperty(hasMurder, "" + station.crime.MurderCount);
                if (station.crime.MurderCount != 0) {
                    crimes = crimes + ", Murder";
                }

                ind.addProperty(hasRobbery, "" + station.crime.RobberyCount);
                if (station.crime.RobberyCount != 0) {
                    crimes = crimes + ", Robbery";
                }

                ind.addProperty(hasGovernment, "" + station.crime.GovernmentCount);
                if (station.crime.GovernmentCount != 0) {
                    crimes = crimes + ", Government";
                }

                ind.addProperty(hasDangerous, "" + station.crime.DangerousCount);
                if (station.crime.DangerousCount != 0) {
                    crimes = crimes + ", DangerousActs";
                }

                ind.addProperty(hasDrug, "" + station.crime.DrugCount);
                if (station.crime.DrugCount != 0) {
                    crimes = crimes + ", Drug";
                }

                ind.addProperty(hasProperty, "" + station.crime.PropertyCount);
                if (station.crime.PropertyCount != 0) {
                    crimes = crimes + ", Property";
                }

                ind.addProperty(hasPublic, "" + station.crime.PublicCount);
                if (station.crime.PublicCount != 0) {
                    crimes = crimes + ", Public";
                }

                ind.addProperty(hasTheft, "" + station.crime.TheftCount);
                if (station.crime.TheftCount != 0) {
                    crimes = crimes + ", Theft";
                }

                ind.addProperty(hasFraud, "" + station.crime.FraudCount);
                if (station.crime.FraudCount != 0) {
                    crimes = crimes + ", Fraud";
                }

                ind.addProperty(hasWeapons, "" + station.crime.WeaponsCount);
                if (station.crime.WeaponsCount != 0) {
                    crimes = crimes + ", Weapons";
                }

                ind.addProperty(hasBurglary, "" + station.crime.BurglaryCount);
                if (station.crime.BurglaryCount != 0) {
                    crimes = crimes + ", Burglary";
                }

                ind.addProperty(hasKidnapping, "" + station.crime.KidnappingCount);
                if (station.crime.KidnappingCount != 0) {
                    crimes = crimes + ", Kidnapping";
                }

                int total = station.crime.MurderCount + station.crime.RobberyCount + station.crime.GovernmentCount + station.crime.DangerousCount
                        + station.crime.DrugCount + station.crime.PropertyCount + station.crime.PublicCount + station.crime.TheftCount + station.crime.FraudCount
                        + station.crime.WeaponsCount + station.crime.BurglaryCount + station.crime.KidnappingCount;

                
                if(crimes != "")
                    ind.addProperty(hasCrime, "" + crimes.substring(1));
                ind.addProperty(totalCrime, ""+total);
            }

            for (int i = 0; i < countyList.size(); i++) {
                ArrayList<Object> countyInfo = countyList.get(i);
                Geometry geometry = (Geometry) countyInfo.get(3);

                Point point = null;
                try {
                    point = convertToLatLong(station.X, station.Y);
                } catch (FactoryException ex) {
                    Logger.getLogger(CreateModel.class.getName()).log(Level.SEVERE, null, ex);
                } catch (MismatchedDimensionException ex) {
                    Logger.getLogger(CreateModel.class.getName()).log(Level.SEVERE, null, ex);
                } catch (TransformException ex) {
                    Logger.getLogger(CreateModel.class.getName()).log(Level.SEVERE, null, ex);
                }

                OperatorWithin within = OperatorWithin.local();
                if (within.execute(point, geometry, SpatialReference.create("WGS84"), null)) {
                    countyIndiList.get(i).addProperty(hasStations, ind);
                }
            }
        });

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
        Division division = new Division();
        Station station = new Station();
        Crime crime = new Crime();

        while (iter.hasNext()) {
            Triple next = iter.next();
            
            if (!next.getSubject().toString().equals(currentSubject)) {
                Divisions.add(division);
                Stations.add(station);
                station.setCrime(crime);
                Crimes.add(crime);
                division = new Division();
                station = new Station();
                crime = new Crime();
            }
            currentSubject = next.getSubject().toString();
            String nextPredicate = next.getPredicate().toString();

            if (nextPredicate.endsWith("row") || nextPredicate.endsWith("%20html%3E")) {

            } else if (nextPredicate.endsWith("#Station")) {
                station.setName(next.getObject().toString().replace(' ', '_'));
            } else if (nextPredicate.endsWith("#x")) {
                String object = next.getObject().toString();
                station.setX(Float.parseFloat((object.split("\"")[1])));
            } else if (nextPredicate.endsWith("#y")) {
                String object = next.getObject().toString();
                station.setY(Float.parseFloat(object.split("\"")[1]));
                division.addStation(station);
            } /*else if (nextPredicate.endsWith("#Divisions")) {
                String divisionName = next.getObject().toString().replace(' ', '_');
                if (!Divisions.contains(divisionName)) {
                    division.setName(divisionName);
                    station.setDivision(division);
                }
            } */else if (nextPredicate.endsWith("#id")) {
            } else {
                if (nextPredicate.endsWith("Attempts%20or%20threats%20to%20murder%2C%20assaults%2C%20harassments%20and%20related%20offences%202015")) {
                    int counter = Integer.parseInt(next.getObject().toString().split("\"")[1]);
                    crime.setMurderCount(counter);
                } else if (nextPredicate.endsWith("Burglary%20and%20related%20offences%202015")) {
                    int counter = Integer.parseInt(next.getObject().toString().split("\"")[1]);
                    crime.setBurglaryCount(counter);
                } else if (nextPredicate.endsWith("Controlled%20drug%20offences%202015")) {
                    int counter = Integer.parseInt(next.getObject().toString().split("\"")[1]);
                    crime.setDrugCount(counter);
                } else if (nextPredicate.endsWith("Damage%20to%20property%20and%20to%20the%20environment%202015")) {
                    int counter = Integer.parseInt(next.getObject().toString().split("\"")[1]);
                    crime.setPropertyCount(counter);
                } else if (nextPredicate.endsWith("Dangerous%20or%20negligent%20acts%202015")) {
                    int counter = Integer.parseInt(next.getObject().toString().split("\"")[1]);
                    crime.setDangerousCount(counter);
                } else if (nextPredicate.endsWith("Fraud%2C%20deception%20and%20related%20offences%202015")) {
                    int counter = Integer.parseInt(next.getObject().toString().split("\"")[1]);
                    crime.setFraudCount(counter);
                } else if (nextPredicate.endsWith("Kidnapping%20and%20related%20offences%202015")) {
                    int counter = Integer.parseInt(next.getObject().toString().split("\"")[1]);
                    crime.setKidnappingCount(counter);
                } else if (nextPredicate.endsWith("Offences%20against%20government%2C%20justice%20procedures%20and%20organisation%20of%20crime%202015")) {
                    int counter = Integer.parseInt(next.getObject().toString().split("\"")[1]);
                    crime.setGovernmentCount(counter);
                } else if (nextPredicate.endsWith("Public%20order%20and%20other%20social%20code%20offences%202015")) {
                    int counter = Integer.parseInt(next.getObject().toString().split("\"")[1]);
                    crime.setPublicCount(counter);
                } else if (nextPredicate.endsWith("Robbery%2C%20extortion%20and%20hijacking%20offences%202015")) {
                    int counter = Integer.parseInt(next.getObject().toString().split("\"")[1]);
                    crime.setRobberyCount(counter);
                } else if (nextPredicate.endsWith("Theft%20and%20related%20offences%202015")) {
                    int counter = Integer.parseInt(next.getObject().toString().split("\"")[1]);
                    crime.setTheftCount(counter);
                } else if (nextPredicate.endsWith("Weapons%20and%20Explosives%20Offences%202015")) {
                    int counter = Integer.parseInt(next.getObject().toString().split("\"")[1]);
                    crime.setWeaponsCount(counter);
                }

            }
            // Do something with each triple

        }
    }

    private static Point convertToLatLong(float easting, float northing) throws FactoryException, MismatchedDimensionException, TransformException {
//        CRSAuthorityFactory crsFac = ReferencingFactoryFinder.getCRSAuthorityFactory("EPSG", null);
//
//        CoordinateReferenceSystem wgs84crs = crsFac.createCoordinateReferenceSystem("EPSG:27700");
//
//        CoordinateOperation op = new DefaultCoordinateOperationFactory().createOperation(osgbCrs, wgs84crs);
//
//        DirectPosition eastNorth = new GeneralDirectPosition(easting, northing);
//        DirectPosition latLng = op.getMathTransform().transform(eastNorth, eastNorth);
//
//        double latitude = latLng.getOrdinate(0);
//        double longitude = latLng.getOrdinate(1);
//        
        IrishRef irish = new IrishRef(easting, northing);
        LatLng latLng = irish.toLatLng();
        latLng.toWGS84();

        Point p = new Point(latLng.getLongitude(), latLng.getLatitude());
        return p;
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
