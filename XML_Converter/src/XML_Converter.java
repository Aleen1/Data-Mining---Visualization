import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.ReplaceMissingValues;
import weka.clusterers.SimpleKMeans;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XML_Converter{
	
 	public static void main(String[] args) throws Exception {

 		//Take the processed instances from the ARFF data set .
 		Instances instances = getInstancesArff("input/dataset.arff");
 		 		 	
 		//Take the clusters assignment for every instance.
 		int[] assignments = runKMeans("input/kmeans.txt", instances);

 		createXML("output/educationalDS.xml", instances, assignments);
 	}
 	
 	
 	/**
 	 * The function is reading the data set from an ARFF file. 
 	 * It deletes the string attributes and replaces the missing values
 	 * with median values for that attribute.
 	 * @param filepath
 	 * @return instances
 	 * @throws Exception
 	 */
 	public static Instances getInstancesArff(String filepath) throws Exception {
 		//read ARFF file
 		ArffLoader arffLoader = new ArffLoader();
 		arffLoader.setSource(new File(filepath));
 		Instances instances = arffLoader.getDataSet();

 		//Deleting the string attributes, because KMeans can't work with strings.
 		instances.deleteStringAttributes();
 		
 		//Replacing missing values with median value for that attribute
 		ReplaceMissingValues replaceMissingFilter = new ReplaceMissingValues();
		replaceMissingFilter.setInputFormat(instances);
		instances = Filter.useFilter(instances, replaceMissingFilter);
 		
 		return instances;
 	}

 	/**
 	 * Function that runs KMeans on given instances. It takes the arguments
 	 * for KMeans functions from a property file given by filepath parameter.
 	 * @param filepath
 	 * @param instances
 	 * @return
 	 * @throws Exception
 	 */
 	public static int[] runKMeans(String filepath, Instances instances) throws Exception {
 		
 		@SuppressWarnings("resource")
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(filepath)));
 		
 		String[] tokens = bufferedReader.readLine().split("\\s+");
        int maxIterations = Integer.parseInt(tokens[1]);
        
        tokens = bufferedReader.readLine().split("\\s+");
        int numClusters = Integer.parseInt(tokens[1]);
        
        tokens = bufferedReader.readLine().split("\\s+");
        int seed = Integer.parseInt(tokens[1]);
 		
 		SimpleKMeans SKMeans = new SimpleKMeans();
 		
 		SKMeans.setPreserveInstancesOrder(true);
 		SKMeans.setMaxIterations(maxIterations);
 		SKMeans.setNumClusters(numClusters);
 		SKMeans.setSeed(seed);
 		
 		SKMeans.buildClusterer(instances);
 		
 		return SKMeans.getAssignments();
 	
 	}
 	
 	
 	/**
 	 * Function that creates an XML file out of an educational data set 
 	 * and the assignments to cluster of every instance of that data set.
 	 * @param filepath
 	 * @param instances
 	 * @param assignments
 	 * @throws Exception
 	 */
 	public static void createXML(String filepath, Instances instances, int[] assignments) throws Exception {
 		
 		//Creating the document builder
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		//Creating the root element of the XML
		Document document = docBuilder.newDocument();
		Element rootElement = document.createElement("educationalDS");
		document.appendChild(rootElement);

		//Add specific instances.
		for(int index = 19999; index < 20999; ++index) {
			Instance instance = instances.get(index);
			
			addInstanceToXML(instance, document, rootElement, index, assignments[index]);
		}
		
		//Write the content into XML file.
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(document);
		StreamResult result = new StreamResult(new File(filepath));
		transformer.transform(source, result);		
 	
 	}
 	
 	/**
 	 * Function that converts and adds an instance to XML file. 
 	 * @param instance
 	 * @param document
 	 * @param rootElement
 	 * @param index
 	 * @param assignment
 	 */
 	public static void addInstanceToXML(Instance instance, Document document, Element rootElement, int index, int assignment) {
		
 		//Create the student element. Add it's id number.
		Element student = document.createElement("Student");
		student.setAttribute("ID", String.valueOf(index - 19998));
	
		//Add assignment attribute to the student element (root element).
		addAssignmenToXML(index, assignment, document, student);
		
		//Add every existing attribute to the student element (root element).
		for(int attrIndex = 0; attrIndex < instance.numAttributes(); ++ attrIndex) {
			
			Attribute attribute = instance.attribute(attrIndex);
			String attributeName = attribute.name();
			
			String attributeValue = "";
			
			if(attribute.isNumeric()) {
				attributeValue = String.valueOf(instance.value(attrIndex));
			} else {
				attributeValue = instance.stringValue(attrIndex);
			}
			
			addAttrToInstanceXML(attributeValue, attributeName, document, student);
		
		}
 		
		//Add the instance to the root element of the entire XML file.
 		rootElement.appendChild(student);
 	
 	}
 	
 	/**
 	 * Function that adds an existing attribute of an instance to the root element of that instance.
 	 * @param attributeValue
 	 * @param attributeName
 	 * @param document
 	 * @param rootElement
 	 */
 	public static void addAttrToInstanceXML(String attributeValue, String attributeName, Document document, Element rootElement) {
 		
 		String attrName = attributeName;
 		
 		Element element = document.createElement(String.valueOf(attrName));
 		element.appendChild(document.createTextNode(attributeValue));
 	
 		rootElement.appendChild(element);
 		
 	}
 	
 	/**
 	 * Function that adds the assinagment to the cluster of an instance to the root element of that instance. 
 	 * @param index
 	 * @param assignment
 	 * @param document
 	 * @param rootElement
 	 */
 	public static void addAssignmenToXML(int index, int assignment, Document document, Element rootElement) {
 		
 		Element element = document.createElement("assignment");
 		element.appendChild(document.createTextNode(String.valueOf(assignment)));
 	
 		rootElement.appendChild(element);
 	
 	}

}