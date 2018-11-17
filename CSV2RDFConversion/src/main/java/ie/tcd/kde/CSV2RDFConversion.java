package ie.tcd.kde;


import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.propertytable.lang.CSV2RDF;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class CSV2RDFConversion {


    public void readCSV(String filename) {
        List<Map<String, String>> list = new ArrayList<>();
        try (Reader reader = Files.newBufferedReader(Paths.get(filename), StandardCharsets.ISO_8859_1)) {
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader());
//            Map<String, Integer> header = csvParser.getHeaderMap();
//            list.add(header);

            List<CSVRecord> record = csvParser.getRecords();


            Set<String> headers = record.get(0).toMap().keySet();
            List<String> headerList = new ArrayList<String>(headers);

            List<String> removeKeys = new ArrayList<>();

            String[] search = {"2003", "2004", "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2016"};

            for (int i = 0; i < headerList.size(); i++) {

                for (int j = 0; j < search.length; j++) {
                    if (headerList.get(i).contains(search[j])) {
                        removeKeys.add(headerList.get(i));
                    }
                }

            }

            Set<String> removeHeaderSet = new HashSet<String>(removeKeys);

//            System.out.println(removeKeys);


//            System.out.println(record.size());
//            System.out.println(record.get(0).toMap());

//            list.forEach(System.out::println);

            for (int i = 0; i < record.size(); i++) {

                CSVRecord curRecord = record.get(i);
                Map<String, String> myMap = curRecord.toMap();

//                System.out.println(myMap);

//                for (int j = 0; j < removeKeys.size(); j++) {
//                    System.out.println(myMap.remove(removeKeys.get(i)));
//                }

                myMap.keySet().removeAll(removeHeaderSet);
//                System.out.println(myMap);
//                System.out.println(headers.size());
                list.add(myMap);
//                break;

            }

            writeNewCSV(list);

//            System.out.println(list.get(0));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeNewCSV(List<Map<String, String>> list) {

//        System.out.println(list.get(0).toString());

        try {

            String filename = "crime_data_2015.csv";
            File file = new File(filename);
            Writer fileWriter = new FileWriter(file);


            Set<String> key = list.get(0).keySet();




//            System.out.println(key);

            String[] headerList = (String[]) key.toArray(new String[key.size()]);
            for (int i = 0; i < headerList.length; i++) {

                headerList[i] = appendStringToCSV(headerList[i]);

            }

//            Set<String> keyHeader = new HashSet<String>(headerList);

            String header = StringUtils.join(headerList, ",");
//            System.out.println(header);

            fileWriter.write(header);
            fileWriter.write(System.getProperty("line.separator"));

            // generate content of CSV file
            for (Map<String, String> map : list) {
                String line = getLineFromMap(map, key);
//                System.out.println(line);
                fileWriter.write(line);
                fileWriter.write(System.getProperty("line.separator"));
//                break;
            }

            fileWriter.close();

            convertToRDF(file.toURI());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void convertToRDF(URI fileUri) throws IOException {

        CSV2RDF.init();
        //load through manager:
        Model m = RDFDataMgr.loadModel("crime_data_2015.csv") ;
        m.read("http://example.com", "csv");
        m.setNsPrefix("test", "http://example.com#");
        m.write(new FileWriter("crime_rate_rdf.ttl"), "ttl");

    }


    private String getLineFromMap(Map<String, String> someMap, Set<String> keys) {
        List<String> values = new ArrayList<>();
        for (String key : keys) {
            values.add(someMap.get(key) == null ? " " : appendStringToCSV(someMap.get(key)));
//            System.out.print("Key : "+ key + " Value : " + values.get(values.size() - 1));
        }
        return StringUtils.join(values, ",");
    }

    private static String appendStringToCSV(String str) {
        return "\"" + str + "\"";
    }
    public static void main(String args[]) {
        CSV2RDFConversion csv2RDF = new CSV2RDFConversion();
        csv2RDF.readCSV("garda_stations.csv");
    }

}
