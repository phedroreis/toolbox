package toolbox.html;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import javax.management.modelmbean.XMLParseException;

/**
 *
 * @author Pedro Reis
 * @since 1,0 - 2 de outubro de 2024
 * @version 1.0
 */
public final class XmlParser {

    private final toolbox.regex.Regex tagRegex;

    private final String xmlContent;

    private TagParser tagParser;

    private LinkedList<Tag> stack;

    /**
     * *****************************************************************************************************************
     * Construtor.
     *
     * @param xmlContent O conteudo do arquivo xml.
     *
     * @param tagParser Objeto de uma classe que extenda a classe abstrata TagParser.
     *****************************************************************************************************************
     */
    public XmlParser(final String xmlContent, final TagParser tagParser) {

        this.xmlContent = xmlContent;

        tagRegex = new toolbox.regex.Regex("</?(\\w+)([\\s\\S]*?)>");
        tagRegex.setTarget(xmlContent);

        stack = new LinkedList<>();

        this.tagParser = tagParser;

    }//construtor

    /**
     * *****************************************************************************************************************
     * Realiza o parse de um arquivo XML.
     *
     * @throws XMLParseException Se nao existir tag de fechamento para uma tag que requeira isto ou se for detectado
     * fechamento nao casado de tag.
     *****************************************************************************************************************
     */
    public void parse() throws Exception {

        String match;
        Tag tag;

        while ((match = tagRegex.find()) != null) {

            String tagId = tagRegex.group(1).toLowerCase();

            int tagPosition = tagRegex.start();

            if (match.charAt(1) == '/') {

                try {

                    tag = stack.pop();
                    
                } catch (NoSuchElementException e) {

                    throw new XMLParseException(" -> </" + tagId + '>');
                }

                if (!tagId.equals(tag.getTagId())) {
                    throw new XMLParseException('<' + tag.getTagId() + "> -> </" + tagId + '>');
                }
                
                TagParser previousTagParser = tag.getPreviousParser();
                if (previousTagParser != null) tagParser = previousTagParser;                    

                if (tag.isNotifyClosingRequired()) {

                    tag.setTagContent(xmlContent.substring(tag.getStartTagContentIndex(), tagPosition));
                    
                    tag.setEndTagBlockIndex(tagPosition + match.length());  
                    
                    tagParser.closeTag(tag);

                }

            } else {

                tag = new Tag(
                    tagId,
                    tagRegex.group(2),
                    tagPosition,
                    tagPosition + match.length()
                );

                if (!match.endsWith("/>")) {
                    stack.push(tag);
                }

                TagParser innerParser = tagParser.openTag(tag);
                
                if (innerParser != null) {
                    
                    tag.setPreviousParser(tagParser);
                    tagParser = innerParser;
                    
                }                

            }//if-else

        }//while

    }//parse

}//classe XmlParser
