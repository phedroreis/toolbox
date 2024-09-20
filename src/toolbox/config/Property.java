package toolbox.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

/**
 *
 * @author 
 * @since
 * @version
 */
public class Property extends Properties {
    
    private final String configFilePathname;

    public Property(final String configFilePathname) {
        
        this.configFilePathname = configFilePathname;

    }//construtor
    
    public void load() throws FileNotFoundException, IOException {
        
        try ( FileInputStream in = new FileInputStream(configFilePathname) ) { 
            
            load(in); 
        }   
    }
    
    public void store(final String msg) throws FileNotFoundException, IOException {
        
        try (FileOutputStream out = new FileOutputStream(configFilePathname)) {               

            store(out, msg);
        }        
    }

}//classe Property
