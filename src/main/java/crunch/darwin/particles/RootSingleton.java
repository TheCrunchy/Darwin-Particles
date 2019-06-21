package crunch.darwin.particles;

import java.nio.file.Path;

public class RootSingleton {

	    // static variable single_instance of type Singleton 
	    private static RootSingleton single_instance = null; 
	  
	    // variable of type String 
	    private Path root; 
	  
	    public void setRoot(Path root) {
	    	this.root = root;
	    }
	    public Path getRoot() {
	    	return root;
	    }
	    // private constructor restricted to this class itself 
	    private RootSingleton() 
	    { 
	    } 
	  
	    // static method to create instance of Singleton class 
	    public static RootSingleton getInstance() 
	    { 
	        if (single_instance == null) 
	            single_instance = new RootSingleton(); 
	  
	        return single_instance; 
	    } 
	
}
