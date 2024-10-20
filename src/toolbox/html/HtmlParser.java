package toolbox.html;
  
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.MissingResourceException;
import java.util.NoSuchElementException;
import javax.management.modelmbean.XMLParseException;

/***********************************************************************************************************************
 * Classe para realizar o parsing de documentos HTML. Nao pode ser utilizada em documentos que contem tags de fechamento
 * opcional sem sua respectiva tag de fechamento. Um objeto desta classe espera que todas as tags com escopo possuam
 * tag de fechamento, caso contrario, uma XMLPaserException e lancada.
 * 
 * @author Pedro Reis 
 * 
 * @version 1.0 
 * 
 * @since 1.0 - 23 de abril de 2024
 **********************************************************************************************************************/
public final class HtmlParser {
    
    private final toolbox.regex.Regex tagRegex;
    
    private final String htmlContent;
    
    private TagParser tagParser;
    
    private LinkedList<Tag> stack;
    
    private static String msg$1, msg$2;
    
    static {
        try {
            ResourceBundle rb = 
                ResourceBundle.getBundle("toolbox.properties.HtmlParser", toolbox.locale.Localization.getLocale());
            msg$1 = rb.getString("msg$1");//has no corresponding open tagId       
            msg$2 = rb.getString("msg$2");//do not close

            
       } catch (NullPointerException | MissingResourceException | ClassCastException e) {
           
            // Opcaoes default caso falhe a chamada a rb.getString() [Locale en_US : default]
            msg$1 = "has no corresponding open tag";        
            msg$2 = "do not close";
          
       }
    }    
    
    /*******************************************************************************************************************
     * Construtor da classe.
     * 
     * @param htmlContent O conteudo do um arquivo HTML
     * 
     * @param tagParser Objeto que de uma classe que extenda TagParser
     ******************************************************************************************************************/
    public HtmlParser(final String htmlContent, final TagParser tagParser) {
        
        this.htmlContent = htmlContent;
        
        tagRegex = new toolbox.regex.Regex("</?([A-Za-z]+)([\\S\\s]*?)>");
        tagRegex.setTarget(htmlContent);
        
        stack = new LinkedList<>();
        
        this.tagParser = tagParser;
        
    }//construtor
    
    /*==================================================================================================================
     * 
     =================================================================================================================*/
    private void popStack(final int endContentIndex, final int endBlockIndex) throws Exception {
        
        Tag tag = null;
        
        try {
        
            tag = stack.pop();
        }
        catch (NoSuchElementException e) {
            
            throw new NoSuchElementException(msg$1);
            
        }
        
        TagParser previousTagParser = tag.getPreviousParser();
        if (previousTagParser != null) tagParser = previousTagParser;       
        
        if (tag.isNotifyClosingRequired()) {
            
            tag.setTagContent(htmlContent.substring(tag.getStartTagContentIndex(), endContentIndex));
            
            tag.setEndTagBlockIndex(endBlockIndex);

            tagParser.closeTag(tag);  
            
        }  
        
    }//popStack
    
    /*==================================================================================================================
     * 
     =================================================================================================================*/
    private String getTopStackedTagId() {
  
        return stack.peek().getTagId();
        
    }//getTopStackedTagId    
    
    /*==================================================================================================================
     * 
     =================================================================================================================*/
    private void exception(final String tag) throws XMLParseException {
        
        String topStackedTagId = getTopStackedTagId();
        
        topStackedTagId = (topStackedTagId == null) ? msg$1 : (msg$2 + " <" + topStackedTagId + '>');
        
        throw new XMLParseException("</" + tag + "> " + topStackedTagId);
        
    }//exception    
     
    /*******************************************************************************************************************
     * Realiza o parsing do documento HTML.
     * 
     * @throws XMLParseException No caso do fechamento de alguma tagId nao casar com sua abertura.
     ******************************************************************************************************************/
    public void parse() throws Exception {

        String match;
        
        boolean htmlCode = true;
        
        while ((match = tagRegex.find()) != null) { 
            
            String tagId = tagRegex.group(1).toLowerCase();
            
            int tagPosition = tagRegex.start();
            
            if (htmlCode && match.charAt(1) != '/') {//tags de abertura
                
                Tag tag = new Tag(
                    tagId, 
                    tagRegex.group(2),
                    tagPosition,
                    tagPosition + match.length()
                );
                
                switch (tagId) {
                    
                    //self-closing tags
                    case "area":
                    case "base":
                    case "br":
                    case "col":
                    case "embed":
                    case "hr":    
                    case "img":
                    case "input":
                    case "link":    
                    case "meta":
                    case "param":    
                    case "source":
                    case "track":
                    case "wbr":
                    case "command"://obsoleta
                    case "keygen"://obsoleta
                    case "menuitem"://obsoleta
                    case "frame"://obsoleta    
                        break;
                        
                    //o escopo das tags sctipt e style nao contem codigo html     
                    case "script":
                    case "style":
                        htmlCode = false;
                    
                    //requerem tagId de fechamento                   
                    default: 
                        stack.push(tag);
                        
                }//switch
                
                TagParser innerParser = tagParser.openTag(tag);
                
                if (innerParser != null) {
                    
                    tag.setPreviousParser(tagParser);
                    tagParser = innerParser;
                    
                } 
                
            }
            else {//tags de fechamento
                
                if (!htmlCode) {
                    
                    if ( !(tagId.equals("script") || tagId.equals("style")) ) continue;
                    
                    htmlCode = true;
                }
               
                if (!tagId.equals(getTopStackedTagId())) exception(tagId);  
                 
                popStack(tagPosition, tagPosition + match.length());           
            }
            
            
        }//while        

    }//parse

}//classe HtmlParser