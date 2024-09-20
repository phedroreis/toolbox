package toolbox.textfile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Stream;

/**
 *
 * @author 
 * @since
 * @version
 */
public final class TextFileTools {
    
    public static String readTextFileFromInputStream(
        final InputStream inputStream,
        final String charset
    ) throws IOException {
        
        Stream<String> stream;
        StringBuilder sb;
        
        try (BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream, charset))) {
            
            stream = bf.lines();

            sb = new StringBuilder();
            
            stream.forEach(line -> sb.append(line).append(toolbox.string.StringTools.NEWLINE));
        }
        
        stream.close();
        
        return sb.toString();
        
    }    

}//classe TextFileTools
