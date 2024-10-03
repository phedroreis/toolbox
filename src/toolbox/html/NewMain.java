package toolbox.html;

/**
 *
 * @author hugo
 */
public class NewMain {


    public static void main(String[] args) throws Exception {
        
        toolbox.textfile.TextFileHandler tfh = 
            new toolbox.textfile.TextFileHandler("/home/hugo/inicial-manjaro.html");
        
        tfh.read();        
                
      HtmlParser htmlParser = new HtmlParser(tfh.getContent(), new Parser1());

        htmlParser.parse();        
    }
    
private static class Parser1 extends TagParser {
         
    @Override
    public TagParser openTag(final Tag t) {
        
        if (t.getTagId().equals("ul")) {
            
            String classValue = t.getAttrMap().get("class");
            
            if (classValue != null && classValue.equals("nav")) {
            
                System.out.println(t);


 

                return new Parser2();
                
            }

        }//openTag
        
         if (t.getTagId().equals("script")) t.notifyClosing();
 
        return null;
    }
    
    @Override
    public void closeTag(final Tag t) {
        
        System.out.println(t + "\n\n" + t.getTagContent()+"\n---------------------------------");
              
    }//closeTag
    
}

private static class Parser2 extends TagParser {
    
    @Override
    public TagParser openTag(Tag t) {
        if (t.getTagId().equals("li")) t.notifyClosing();
        return null;
    }
    
    @Override
    public void closeTag(Tag t) {
        System.out.println(t.getTagContent());
    }
}

}
