package toolbox.html;

/***********************************************************************************************************************
 * Metodos que serao executados pelo metodo <i><b>parse()</b></i> das classes HtmlParser e
 * XmlParser deste pacote, ao localizar uma tag de abertura ou uma tag de fechamento.
 * 
 * @author Pedro Reis 
 * @version 1.0 
 * @since 1.0 - 30 de setembro de 2024
 **********************************************************************************************************************/
public abstract class TagParser {

    abstract public TagParser openTag(final Tag tag) throws Exception;
 
    public void closeTag(final Tag tag) throws Exception{}

}//classe TagParser