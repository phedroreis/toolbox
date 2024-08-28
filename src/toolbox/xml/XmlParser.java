package toolbox.xml;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import javax.management.modelmbean.XMLParseException;

/***********************************************************************************************************************
 * Esta classe fornece a fucionalidade de parsing para arquivos XML.
 * 
 * @author Pedro Reis 
 * @version 1.0 - 23 de abril de 2024
 * @since 1.0
 **********************************************************************************************************************/
public final class XmlParser {
    
    private final toolbox.regex.Regex tagRegex;
    
    private final String xmlContent;
    
    private final TagParser tagParser;
    
    private LinkedList<Tag> stack;
    
    /*******************************************************************************************************************
     * Construtor.
     * 
     * @param xmlContent O conteudo do arquivo xml.
     * 
     * @param tagParser Objeto que implemente a interface TagParser.
     ******************************************************************************************************************/
    public XmlParser(final String xmlContent, final TagParser tagParser) {
        
        this.xmlContent = xmlContent;
        
        tagRegex = new toolbox.regex.Regex("</?(\\w+)(.*?)>");
        tagRegex.setTarget(xmlContent);
        
        stack = new LinkedList<>();
        
        this.tagParser = tagParser;
        
    }//construtor
    
    /*******************************************************************************************************************
     * Realiza o parse de um arquivo XML.
     * 
     * @throws XMLParseException Se nao existir tag de fechamento para uma tag que requeira isto ou
     * se for detectado fechamento nao casado de tag.
     ******************************************************************************************************************/
    public void parse() throws XMLParseException {
        
        Tag.setScopeLevel(0);
        
        String tagMatched;
        Tag tag;
        
        while ((tagMatched = tagRegex.find()) != null) { 
            
            String tagMatchedName = tagRegex.group(1).toLowerCase();
            
            int tagMatchedPosition = tagRegex.start();
            
            if (tagMatched.charAt(1) == '/') {
                
                try {
                    
                    tag = stack.pop();
                }
                catch (NoSuchElementException e) {
                    
                    throw new XMLParseException(" -> </" + tagMatchedName + '>');                    
                }
                
                if (!tagMatchedName.equals(tag.getTagName())) 
                    throw new XMLParseException('<' + tag.getTagName() + "> -> </" + tagMatchedName + '>');

                if (tag.isNotifyClosingRequired()) {
                    
                    tag.setContent(xmlContent.substring(tag.getStartContentIndex(), tagMatchedPosition));
                    tag.setEndBlockIndex(tagMatchedPosition + tagMatched.length());
                    tagParser.closeTag(tag);
                  
                }                      
               
            } 
            else {
                
                tag = new Tag(
                    tagMatchedName, 
                    tagRegex.group(2),
                    tagMatchedPosition, 
                    tagMatchedPosition + tagMatched.length()
                );
                
                if (!tagMatched.endsWith("/>")) stack.push(tag);
                
                tagParser.openTag(tag);
                
            }//if-else
            
        }//while
        
    }//parse
    
    /*******************************************************************************************************************
     * Retorna o conteudo xml.
     * 
     * @return O codigo xml.
     ******************************************************************************************************************/
    @Override
    public String toString() {
        
        return xmlContent;
        
    }//toString
    
}//classe XmlParser
