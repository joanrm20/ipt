package org.gbif.ipt.task;

import org.gbif.ipt.model.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;
/**
 * DCAT FILE EXAMPLE
 *
 *@prefix dcat: <http://www.w3.org/ns/dcat#> .
 *@prefix dc: <http://purl.org/dc/terms/> .
 *@prefix foaf: <http://xmlns.com/foaf/0.1/> .
 *<http://datatank4.gent.be/bestuurenbeleid/schepenen>
 *a dcat:Dataset ;
 *dc:title "bestuurenbeleid/schepenen" ;
 *dc:description """Schepenen van Stad Gent.
 *""" ;
 *dc:identifier "bestuurenbeleid/schepenen" ;
 *dc:issued "2015-01-15T13:05:42+0000" ;
 *dc:modified "2015-01-15T13:05:42+0000" ;
 *dcat:distribution <http://datatank4.gent.be/bestuurenbeleid/schepenen.json> .
 *
 */
public class GenerateDCAT {

   /* @Inject
    DataDir Directory;
    private  Resource resource;
    private  Eml eml;
    private  Organisation organisation;*/

    public static void main (String[]args){
        Resource r = new Resource();
        r.setShortname("Testing Resource");
        System.out.println("-----------------------------------\n"+"         Testing Resource\n"+"-----------------------------------\n"+ r.toString());

        HashMap<String,String> prefix = new HashMap<String,String>();

        prefix.put("dct:","http://purl.org/dc/terms/");
        prefix.put("dcat:","http://www.w3.org/ns/dcat#");
        prefix.put("xsd:","http://www.w3.org/2001/XMLSchema#");
        prefix.put("skos:","http://www.w3.org/2004/02/skos/core#");
        prefix.put("rdfs:","http://www.w3.org/2000/01/rdf-schema#");
        prefix.put("foaf:","http://xmlns.com/foaf/0.1/");
        prefix.put("schema:","http://schema.org/");
        prefix.put("adms:","http://www.w3.org/ns/adms#");

        String EML = "<eml:eml xsi:schemaLocation=\"eml://ecoinformatics.org/eml-2.1.1 http://rs.gbif.org/schema/eml-gbif-profile/1.1/eml.xsd\" packageId=\"http://localhost:8080/resource?id=restest1/v1.0\" system=\"http://gbif.org\" scope=\"system\" xml:lang=\"eng\"><dataset><alternateIdentifier>http://localhost:8080/resource?r=restest1</alternateIdentifier><title xml:lang=\"eng\">Testing oSoc15</title><creator><individualName><surName>Test</surName></individualName><organizationName>Debug</organizationName><positionName>Leader Test</positionName><address><country>BE</country></address></creator><metadataProvider><individualName><surName>Test</surName></individualName><organizationName>TestMeta</organizationName><positionName>Leader Metadata</positionName><address><country>BE</country></address></metadataProvider><pubDate>\n" +
                "      2015-07-08\n" +
                "  </pubDate><language>eng</language><abstract><para>Testing progress</para></abstract><keywordSet><keyword>Metadata</keyword><keywordThesaurus>GBIF Dataset Type Vocabulary: http://rs.gbif.org/vocabulary/gbif/dataset_type.xml</keywordThesaurus></keywordSet><keywordSet><keyword>Observation</keyword><keywordThesaurus>GBIF Dataset Subtype Vocabulary: http://rs.gbif.org/vocabulary/gbif/dataset_subtype.xml</keywordThesaurus></keywordSet><intellectualRights><para>To the extent possible under law, the publisher has waived all rights to these data and has dedicated them to the <ulink url=\"http://creativecommons.org/publicdomain/zero/1.0/legalcode\"><citetitle>Public Domain (CC0 1.0)</citetitle></ulink>. Users may copy, modify, distribute and use the work, including for commercial purposes, without restriction.</para></intellectualRights><coverage><geographicCoverage><boundingCoordinates><westBoundingCoordinate>2.66</westBoundingCoordinate><eastBoundingCoordinate>6.66</eastBoundingCoordinate><northBoundingCoordinate>51.2</northBoundingCoordinate><southBoundingCoordinate>49.76</southBoundingCoordinate></boundingCoordinates></geographicCoverage></coverage><maintenance><description><para/></description><maintenanceUpdateFrequency>notPlanned</maintenanceUpdateFrequency></maintenance><contact><individualName><surName>test</surName></individualName><organizationName>Testing</organizationName><positionName>Leader</positionName><address><country>BE</country></address></contact></dataset><additionalMetadata><metadata><gbif><dateStamp>2015-07-07T02:41:40.519+02:00</dateStamp><hierarchyLevel>dataset</hierarchyLevel></gbif></metadata></additionalMetadata></eml:eml>";
        ArrayList<String> txt = readXmlFile();
        ArrayList<String> finalTxt = hasMapToStringArray(prefix);
        finalTxt.addAll(txt);
        writeFile(finalTxt);
    }

    /**
     * Convert the prefix Hashmap to a array list
     * @param prefix
     * @return
     */
    private static ArrayList<String> hasMapToStringArray(HashMap<String,String> prefix){
        Object [] keyArray = prefix.keySet().toArray();
        String[] strArray = new String[keyArray.length];
        for (int i = 0; i < keyArray.length; i++)
        {
            strArray[i] = new String((String) keyArray[i]);
        }
        ArrayList<String> text = new ArrayList<String>();
        for (String key : strArray){
            String link = prefix.get(key);
            String line = "@prefix "+key+link;
            text.add(line);
        }
        return text;
    }
    /**
     * Write a new file with information in it
     *  @param informations
     */
    private static void writeFile(ArrayList<String> informations ) {
        File fnew = new File ("/home/sylvain/Documents/datadir/dcat.txt");//Need to be fix HARDCODING
        try
        {
            PrintWriter pwnew = new PrintWriter (new BufferedWriter (new FileWriter (fnew)));
            for(String info: informations){
                pwnew.println(info);
            }
            pwnew.close();
        }
        catch (IOException exception)
        {
            System.out.println("Writing error " + exception.getMessage());
        }
    }
       private static ArrayList<String> readXmlFile() {
           ArrayList<String> txt = new ArrayList<String>();
           try {

               File file = new File("/home/sylvain/Documents/datadir/coccinellidae/eml.xml");//Need to be fix HARDOCING

               DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

               Document doc = dBuilder.parse(file);
               if (doc.hasChildNodes()) {
                   printNode(doc.getChildNodes(),"",txt);
               }
           } catch (Exception e) {
               System.out.println(e.getMessage());
           }
           return txt;
       }

    /**
     * Recursive method for traking/printing everyNode
     * @param nodeList
     */
    private static void printNode(NodeList nodeList,String position,ArrayList<String> txt) {

        for (int count = 0; count < nodeList.getLength(); count++) {
            Node tempNode = nodeList.item(count);
            if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
                position += "-";
                System.out.println(position + "[OPEN]" + tempNode.getNodeName());
                txt.add(position + "[OPEN]" + tempNode.getNodeName());
                if (tempNode.hasAttributes()) {
                    NamedNodeMap nodeMap = tempNode.getAttributes();

                    for (int i = 0; i < nodeMap.getLength(); i++) {

                        Node node = nodeMap.item(i);
                        /*System.out.println("attr name : " + node.getNodeName());
                        System.out.println("attr value : " + node.getNodeValue());*/
                    }
                }
                //Recursive loop
                if (tempNode.hasChildNodes()) {
                    if(tempNode.getChildNodes().getLength() == 1){
                        System.out.println(tempNode.getTextContent());
                        txt.add(tempNode.getTextContent());
                    }
                    printNode(tempNode.getChildNodes(),position,txt);
                }
                System.out.println(position + "[CLOSE]" + tempNode.getNodeName());
                txt.add(position + "[CLOSE]" + tempNode.getNodeName());
            }
        }
    }
}
