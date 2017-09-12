/*** Author :Vibhav Gogate
The University of Texas at Dallas
*****/


import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
 

public class KMeans {
	static int max = Integer.MIN_VALUE;
	static int min = Integer.MAX_VALUE;
    public static void main(String [] args){
	if (args.length < 3){
	    System.out.println("Usage: Kmeans <input-image> <k> <output-image>");
	    return;
	}
	try{
		int i = 0;
		double[] data = new double[10];
		double sum = 0;
		while(i < 10){
			File origin = new File(args[0]);
		    BufferedImage originalImage = ImageIO.read(origin);
		    int k=Integer.parseInt(args[1]);
		    BufferedImage kmeansJpg = kmeans_helper(originalImage,k);
		    File revise = new File(args[2]);
		    ImageIO.write(kmeansJpg, "jpg", revise); 
		    data[i] = (double)revise.length() / origin.length() * 100;
		    sum += data[i];
		    System.out.println("k = " + k + " compression rate: " + data[i] + "%");
		    i++;
		}
	    System.out.println("average compression rate: " + sum / 10 + "%");
	    double variance = 0;
	    for(int j = 0; j < 10; j++){
	    	variance += (data[j] - sum / 10) * (data[j] - sum / 10);
	    }
	    System.out.println("variace of compression rate: " + variance / 10 + "%");
	}catch(IOException e){
	    System.out.println(e.getMessage());
	}	
    }
    
    private static BufferedImage kmeans_helper(BufferedImage originalImage, int k){
	int w=originalImage.getWidth();
	int h=originalImage.getHeight();
	BufferedImage kmeansImage = new BufferedImage(w,h,originalImage.getType());
	Graphics2D g = kmeansImage.createGraphics();
	g.drawImage(originalImage, 0, 0, w,h , null);
	// Read rgb values from the image
	Point[] rgb=new Point[w*h];
	int count=0;
	for(int i=0;i<w;i++){
	    for(int j=0;j<h;j++){
	    rgb[count] = new Point(kmeansImage.getRGB(i,j));
//	    System.out.println(rgb[count].getValue());
		if(rgb[count].getValue() < min){
			min = rgb[count].getValue();
		}
		else if(rgb[count].getValue() > max){
			max = rgb[count].getValue();
		}
		count++;
	    }
	}
//	System.out.println(min);
//	System.out.println(max);
	
	// Call kmeans algorithm: update the rgb values
	kmeans(rgb,k);

	// Write the new rgb values to the image
	count=0;
	for(int i=0;i<w;i++){
	    for(int j=0;j<h;j++){
		kmeansImage.setRGB(i,j,rgb[count].getClassValue());
//		System.out.println(rgb[count].getClassValue());
		count++;
	    }
	}
	return kmeansImage;
    }

    // Your k-means code goes here
    // Update the array rgb by assigning each entry in the rgb array to its cluster center
    private static void kmeans(Point[] rgb, int k){
    	int[] num = randomPoints(k);
    	int i = 0;
    	while(i++ < 50){
    		int[] alphaSum = new int[k];
    		int[] redSum = new int[k];
    		int[] greenSum = new int[k];
    		int[] blueSum = new int[k];
        	int[] count = new int[k];
    		for(int j = 0; j < rgb.length; j++){
    			int minIndex = -1;
    			double minDistance = Double.MAX_VALUE;
    			for(int m = 0; m < k; m++){
    				int aD = rgb[j].getAlpha() - rgb[j].getAlpha(num[m]);
    				int rD = rgb[j].getRed() - rgb[j].getRed(num[m]);
    				int gD = rgb[j].getGreen() - rgb[j].getGreen(num[m]);
    				int bD = rgb[j].getBlue() - rgb[j].getBlue(num[m]);
    				double distance = Math.sqrt(aD * aD + rD * rD + gD * gD + bD * bD);
//    				System.out.println(distance);
    				if(distance < minDistance){
    					minIndex = m;
    					minDistance = distance;
    				}
    			}
//    			System.out.println(minIndex); 
//				System.out.println(minDistance); 
    			rgb[j].setClassValue(num[minIndex]);
//    			System.out.println(rgb[j].getClassValue());
    			alphaSum[minIndex] += rgb[j].getAlpha();
    			redSum[minIndex] += rgb[j].getRed();
    			greenSum[minIndex] += rgb[j].getGreen();
    			blueSum[minIndex] += rgb[j].getBlue();
//    			System.out.println(average[minIndex]);
    			count[minIndex]++;
    		}
    		num = updateCluster(alphaSum, redSum, greenSum, blueSum, count, k);
    	}
    }
    
    private static int[] randomPoints(int k){
    	Random random = new Random();
    	int[] num = new int[k];
    	for(int i = 0; i < k; i++){
    		num[i] = random.nextInt(max - min) + min;
    	}
    	return num;
    }
    
    private static int[] updateCluster(int[] aSum, int[] rSum, int[] gSum, int[] bSum, int[] count, int k){
    	int[] num = new int[k];
    	for(int i = 0; i < k; i++){
//			System.out.println(average[m]);
//			System.out.println(count[m]);
			int alphaCenter = (int)((double)aSum[i] / count[i]);
			int redCenter = (int)((double)rSum[i] / count[i]);
			int greenCenter = (int)((double)gSum[i] / count[i]);
			int blueCenter = (int)((double)bSum[i] / count[i]);
			num[i] = ((alphaCenter & 0x000000FF) << 24)|((redCenter & 0x000000FF) << 16)|((greenCenter & 0x000000FF) << 8)|((blueCenter & 0x000000FF) << 0);
//			System.out.println(num[m]);
		}
    	return num;
    }
}
