import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.ReplaceMissingValues;
import weka.clusterers.SimpleKMeans;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


public class KMeans{
	
 	public static void main(String[] args) throws Exception{

 		//read ARFF file
 		ArffLoader loader = new ArffLoader();
 		loader.setSource(new File("database-19.arff"));
 		Instances structure = loader.getDataSet();

 		/**
 		//Removing the 19th attribute, because KMeans can't handle strings
	    String[] options = new String[2];
	    options[0] = "-R";                                    // "range"
	    options[1] = "19";                                     // first attribute
	     
	    Remove remove = new Remove();                         // new instance of filter
	    remove.setOptions(options);                           // set options
	    remove.setInputFormat(structure);                          // inform filter about dataset **AFTER** setting options
	    Instances newStructure = Filter.useFilter(structure, remove);   // apply filter
	    
	    ArffSaver saver = new ArffSaver();
	    saver.setInstances(newStructure);
	    saver.setFile(new File("database-19.arff"));
	    saver.writeBatch();
	    */
 		
 		ReplaceMissingValues m_ReplaceMissingFilter = new ReplaceMissingValues();
		m_ReplaceMissingFilter.setInputFormat(structure);
		structure = Filter.useFilter(structure, m_ReplaceMissingFilter);
 		
	    //creating clusters using KMeans
 		SimpleKMeans SKMeans = new SimpleKMeans();
 		SKMeans.setPreserveInstancesOrder(true);
 		SKMeans.setMaxIterations(500);
 		SKMeans.setNumClusters(8); // set the number of clusters here
 		SKMeans.setSeed(10); //more seeds for better result
 		SKMeans.buildClusterer(structure);
 	
 		int[] assignments = SKMeans.getAssignments();

 		//SKMeans = null;	


		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		// root elements
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("databaseXML");
		doc.appendChild(rootElement);

		
 		for(int i = 19999; i < 20999; ++i) {
 		
	 		try {
	 			//create student
	 			Element student = doc.createElement("Student");
	 			rootElement.appendChild(student);
	
	 			// set id of the student
	 			student.setAttribute("id", String.valueOf(i - 19998));
	 			/**
	 			Attr attr = doc.createAttribute("id");
	 			attr.setValue(String.valueOf(i - 19999));
	 			student.setAttributeNode(attr);
				*/
	
	 			
	 			
	 			//course_id attribute
	 			Element course_id = doc.createElement("course_id");
	 			course_id.appendChild(doc.createTextNode(structure.get(i).stringValue(0)));
	 			student.appendChild(course_id);
	
	 			//userid_DI attribute
	 			Element userid_DI = doc.createElement("userid_DI");
	 			userid_DI.appendChild(doc.createTextNode(structure.get(i).stringValue(1)));
	 			student.appendChild(userid_DI);
	 			
	 			//assignment attribute
	 			Element assignment = doc.createElement("assignment");
	 			assignment.appendChild(doc.createTextNode(String.valueOf(assignments[i])));
	 			student.appendChild(assignment);
	 			
	 			//registered attribute
	 			Element registered = doc.createElement("registered");
	 			registered.appendChild(doc.createTextNode(String.valueOf(structure.get(i).value(2))));
	 			student.appendChild(registered);
	 			
	 			//viewed attribute
	 			Element viewed = doc.createElement("viewed");
	 			viewed.appendChild(doc.createTextNode(String.valueOf(structure.get(i).value(3))));
	 			student.appendChild(viewed);
	 			
	 			//explored attribute
	 			Element explored = doc.createElement("explored");
	 			explored.appendChild(doc.createTextNode(String.valueOf(structure.get(i).value(4))));
	 			student.appendChild(explored);
	 			
	 			//certified attribute
	 			Element certified = doc.createElement("certified");
	 			certified.appendChild(doc.createTextNode(String.valueOf(structure.get(i).value(5))));
	 			student.appendChild(certified);
	 			
	 			//final_c__cname_DI attribute
	 			Element final_c__cname_DI = doc.createElement("final_c__cname_DI");
	 			final_c__cname_DI.appendChild(doc.createTextNode(structure.get(i).stringValue(6)));
	 			student.appendChild(final_c__cname_DI);
	 			
	 			//LoE_DI attribute
	 			Element LoE_DI = doc.createElement("LoE_DI");
	 			LoE_DI.appendChild(doc.createTextNode(structure.get(i).stringValue(7)));
	 			student.appendChild(LoE_DI);
	 			
	 			//YoB attribute
	 			Element YoB = doc.createElement("YoB");
	 			YoB.appendChild(doc.createTextNode(structure.get(i).stringValue(8)));
	 			student.appendChild(YoB);
	 			
	 			//gender attribute
	 			Element gender = doc.createElement("gender");
	 			gender.appendChild(doc.createTextNode(structure.get(i).stringValue(9)));
	 			student.appendChild(gender);
	 			
	 			//grade attribute
	 			Element grade = doc.createElement("grade");
	 			grade.appendChild(doc.createTextNode(String.valueOf(structure.get(i).value(10))));
	 			student.appendChild(grade);
	 			
	 			//start_time_DI attribute
	 			Element start_time_DI = doc.createElement("start_time_DI");
	 			start_time_DI.appendChild(doc.createTextNode(structure.get(i).stringValue(11)));
	 			student.appendChild(start_time_DI);
	 			
	 			//last_event_DI attribute
	 			Element last_event_DI = doc.createElement("last_event_DI");
	 			last_event_DI.appendChild(doc.createTextNode(structure.get(i).stringValue(12)));
	 			student.appendChild(last_event_DI);

	 			//nevents attribute
	 			Element nevents = doc.createElement("nevents");
	 			nevents.appendChild(doc.createTextNode(String.valueOf(structure.get(i).value(13))));
	 			student.appendChild(nevents);
	 			
	 			//ndays_act attribute
	 			Element ndays_act = doc.createElement("ndays_act");
	 			ndays_act.appendChild(doc.createTextNode(String.valueOf(structure.get(i).value(14))));
	 			student.appendChild(ndays_act);
	 			
	 			//nplay_video attribute
	 			Element nplay_video = doc.createElement("nplay_video");
	 			nplay_video.appendChild(doc.createTextNode(String.valueOf(structure.get(i).value(15))));
	 			student.appendChild(nplay_video);
	 			
	 			//nchaters attribute
	 			Element nchaters = doc.createElement("nchaters");
	 			nchaters.appendChild(doc.createTextNode(String.valueOf(structure.get(i).value(16))));
	 			student.appendChild(nchaters);
	 			
	 			//nforum_posts attribute
	 			Element nforum_posts = doc.createElement("nforum_posts");
	 			nforum_posts.appendChild(doc.createTextNode(String.valueOf(structure.get(i).value(17))));
	 			student.appendChild(nforum_posts);
	 			
	 			//incomplete_flag attribute
	 			Element incomplete_flag = doc.createElement("incomplete_flag");
	 			incomplete_flag.appendChild(doc.createTextNode(String.valueOf(structure.get(i).value(18))));
	 			student.appendChild(incomplete_flag);
	 			
	 			
	 			
	 			// write the content into xml file
	 			TransformerFactory transformerFactory = TransformerFactory.newInstance();
	 			Transformer transformer = transformerFactory.newTransformer();
	 			DOMSource source = new DOMSource(doc);
	 			StreamResult result = new StreamResult(new File("database-19.xml"));
	
	 			transformer.transform(source, result);
	
	 		  } catch (TransformerException tfe) {
	 			tfe.printStackTrace();
	 		  }
 		}
 	}
	    
 		/**
 		//make assignment file
 		FileWriter file = new FileWriter("assignements.txt");
 		BufferedWriter buffer = new BufferedWriter(file);
 		
 		for(int clusterNum : assignments) {
 			buffer.write(clusterNum + "\n");
 		}
 		
 		buffer.close();
 		*/
 	
}