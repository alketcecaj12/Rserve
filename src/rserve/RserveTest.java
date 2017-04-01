package rserve;

import java.awt.Desktop;
import java.io.File;

import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.REXP;

public class RserveTest {
	private static RConnection c = null;
	
	public static void main(String [] args) throws Exception{
		
		drawBar(new String[]{"a","b","c"},new double[]{5,6,7},
				"x","y","C:/Users/Alket/luna/Rserver/test.pdf",null);
		
	}
	
	public static void drawBar(String[] x, double [] y, String xlab, String ylab, String file, String opts) {
		try {
            	
            c = new RConnection();// make a new local connection on default port (6311)
            
            c.assign("x", x);
            c.assign("y", y);
            
            String end = opts==null || opts.length()==0 ? ";" : " + "+opts+";";
            String code = 
            		   "library(ggplot2);"
            	     + "z <- data.frame(x,y);"
            	     + "ggplot(z,aes(x=x,y=y)) + geom_bar(stat='identity') + theme_bw() +xlab('"+xlab+"') + ylab('"+ylab+"')"+end
            	     + "ggsave('"+file+"');"
            	     + "dev.off();";
            
            //System.out.println(code);
            c.eval(code);
            c.close();
            Desktop.getDesktop().open(new File(file));
        } catch (Exception e) {
        	c.close();
        	if(e.getMessage().startsWith("Cannot connect")) {
             	System.err.println("You must launch the following code in R");
             	System.err.println("library(Rserve)");
             	System.err.println("Rserve()");
            }
            else e.printStackTrace();
        }      
	}

}
