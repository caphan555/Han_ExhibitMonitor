package assignmentthree.exhibitmonitor;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import assignmentthree.filechecker.FileChecker;
import assignmentthree.parsedcontent.ParsedInformation;
import assignmentthree.utilities.OutputFileProducer;
import assignmentthree.utilities.SampleTask;


public class App 
{
    public static void main( String[] args ) throws Exception
    {
    	List<List<String>> information = setUp();
    	ParsedInformation pi = new ParsedInformation(information);
    	
    	
    	
    	FileChecker fileChecker = new FileChecker();
    	Thread polar = new Thread(fileChecker);
    	polar.start();
    	
    	OutputFileProducer ofp = new OutputFileProducer();
    	Timer timer = new Timer();
    	Thread myThread= new Thread(ofp);
    	Calendar cal = Calendar.getInstance();
    	cal.set(Calendar.HOUR_OF_DAY, 17);
    	cal.set(Calendar.MINUTE, 55);
    	timer.schedule(
    			  new SampleTask (myThread),
    			  cal.getTime(),
    			  1000 * 60 * 60 * 24 * 7
    			);
    	
    }
    
    public static List<List<String>> setUp() {
    	try {	
    		List<List<String>> information = new ArrayList<>();
    		
            File inputFile = new File("config.xml");
            DocumentBuilderFactory dbFactory 
               = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
           
            NodeList nList = doc.getElementsByTagName("inputfile");
            for (int temp = 0; temp < nList.getLength(); temp++) {
            	List<String> fileInput = new ArrayList<>();
               Node nNode = nList.item(temp);
               Element nElement = (Element)nNode;
             String name = nElement.getAttribute("name");
             String time = nElement.getAttribute("time");
             String gracePeriod = nElement.getAttribute("grace-period");
             String inputInfo = name +" "+time+" "+gracePeriod;
             fileInput.add(inputInfo);
              
               NodeList mList = nElement.getElementsByTagName("structure");
               for(int temp2 = 0; temp2 < mList.getLength(); temp2++) {
            	   Node fNode = mList.item(temp2);
            	   if (fNode.getNodeType() == Node.ELEMENT_NODE) {
                       Element fElement = (Element) fNode;
                       
                       NodeList fList = fElement.getElementsByTagName("field");
                       for(int temp3 = 0; temp3<fList.getLength(); temp3++) {
                    	   Node tNode = fList.item(temp3);
                    	   if(fNode.getNodeType() == Node.ELEMENT_NODE) {
                    		   Element eElement = (Element) tNode;
                    		   
                    		   String fieldName = eElement.getAttribute("name");
                    		   String fieldType = eElement.getAttribute("type");
                    		   String fieldInfo = fieldName + " "+ fieldType;
                    		   fileInput.add(fieldInfo);
                    		  
                    		  
                    	   }
                    	   
                       }
            	   }
               }
               information.add(fileInput);
            }
          
            List<String> outputFile = new ArrayList<>();
            NodeList oList = doc.getElementsByTagName("outputfile");
            for (int temp7 = 0; temp7 < oList.getLength(); temp7++) {
               Node oNode = oList.item(temp7);
               Element oElement = (Element)oNode;
             
              
               String opName = oElement.getAttribute("name");
               String time = oElement.getAttribute("time");
               String outputInfo = opName + " " + time;
               outputFile.add(outputInfo);
               
               NodeList tList = oElement.getElementsByTagName("dependency");
               for (int temp8 = 0; temp8 < tList.getLength(); temp8++) {
            	   Node tNode = nList.item(temp8);
                   Element tElement = (Element)tNode;
                   String dependency = tElement.getAttribute("name");
                   outputFile.add(dependency);
               }
            }
            
            
            information.add(outputFile);
            
          
            
            return information;
         } catch (Exception e) {
            e.printStackTrace();
         }
		return new ArrayList<>();
    }
}
