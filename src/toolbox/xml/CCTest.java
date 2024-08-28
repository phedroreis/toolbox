package toolbox.xml;

import java.io.IOException;
import java.util.HashMap;
import javax.management.modelmbean.XMLParseException;

/***********************************************************************************************************************
 *
 * @author hugo
 **********************************************************************************************************************/
public class CCTest {
    
    public static void main(String[] args) throws IOException, XMLParseException {
        
        toolbox.textfile.TextFileHandler tfh = 
            new toolbox.textfile.TextFileHandler("/home/hugo/page.html");
        
        tfh.read();        
                
        toolbox.xml.HtmlParser htmlParser = 
            new toolbox.xml.HtmlParser(tfh.getContent(), new Parser());

        htmlParser.parse();        
    }
    
private static class Parser extends TagParser {
         
    @Override
    public void openTag(final Tag t) {
        
        if (t.getTagName().equals("li")) {
            
            HashMap<String, String> map = t.getAttrMap();
            
            String attrValue = map.get("class");

            if (attrValue != null && attrValue.equals("header")) t.notifyClosing();
            
        }//openTag
        
    }
    
    @Override
    public void closeTag(final Tag t) {
        
        System.out.println(t + "\n\n" + t.getContent()+"\n------------------------------------------------");
              
    }//closeTag
    
}//classe privada Parser    
    
}
