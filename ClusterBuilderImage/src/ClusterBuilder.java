import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.clusterers.SimpleKMeans;

import java.awt.Color;
import java.awt.image.BufferedImage;
 
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.io.BufferedWriter;

import javax.imageio.ImageIO;

public class ClusterBuilder{
 
    public static void main(String[] args) throws Exception{
 
        BufferedImage inputImage = ImageIO.read(new File("sample.png"));

        FileWriter file = new FileWriter("sample.arff");
        BufferedWriter buffer = new BufferedWriter(file);
       
        int Width = inputImage.getWidth();
        int Height = inputImage.getHeight();
       
        buffer.write("@RELATION pixels");
        buffer.newLine();
       
        buffer.write("@ATTRIBUTE x NUMERIC");
        buffer.newLine();
        buffer.write("@ATTRIBUTE y NUMERIC");
        buffer.newLine();
        buffer.write("@ATTRIBUTE RGB NUMERIC");
        buffer.newLine();
        buffer.write("@ATTRIBUTE class {pixel}");
        buffer.newLine();
        buffer.write("@DATA");
        buffer.newLine();
       
        ArrayList<Pair> pixels = new ArrayList<Pair>();
        
        for(int i = 0; i < Width; ++i) {
            for(int j = 0; j < Height; ++j) {
            	if(inputImage.getRGB(i, j) == -1) {
	                buffer.write(i + "");
	                buffer.write(",");
	                buffer.write(j + "");
	                buffer.write(",");
	                buffer.write(inputImage.getRGB(i, j) + ",pixel");
	                buffer.newLine();
            	
	                pixels.add(new Pair(i, j));
            	}
            }
        }
       
        buffer.close();
 
        ArffLoader loader = new ArffLoader();
        loader.setFile(new File("sample.arff"));
        Instances structure = loader.getDataSet();
       
 
        SimpleKMeans SimpleKMeans = new SimpleKMeans();
        SimpleKMeans.setPreserveInstancesOrder(true);
        SimpleKMeans.setMaxIterations(500);
        SimpleKMeans.setNumClusters(8);
        SimpleKMeans.setSeed(4);
        SimpleKMeans.buildClusterer(structure);

        /**
        System.out.println(SimpleKMeans.getSquaredError());
        System.out.println(SimpleKMeans.getClusterCentroids());
   
        PrintWriter writer = new PrintWriter("assignments.txt", "UTF-8");
        for(int i = 0; i < assignments.length; ++i) {
        	writer.write(index + "");
        	index ++;
        	writer.write("   ");
        	writer.write(assignments[i] + "");
        	writer.write("\n");
        }
        writer.close();
        */
        

        Color[] colors = {Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW, Color.PINK, Color.MAGENTA, Color.ORANGE, Color.LIGHT_GRAY};
        BufferedImage bi = new BufferedImage(Width, Height, BufferedImage.TYPE_INT_ARGB);
        
        int[] assignments = SimpleKMeans.getAssignments();
        int index = 0;
        
        for(int cluster : assignments) {
        	bi.setRGB(pixels.get(index).getX(), pixels.get(index).getY(), colors[cluster].getRGB());
        	
        	index ++;
        }
        
        ImageIO.write(bi, "PNG", new File("result.png"));
        
        SimpleKMeans = null;
    }
     
}