package toolbox.html;

import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/***********************************************************************************************************************
 * Um objeto dessa classe armazena e fornece os dados de tags XML e HTML.
 * 
 * <p>O nome da tag, a lista de atributos (se houver) e possivelmente o conteudo do escopo da tag.
 * 
 * @author Pedro Reis
 * 
 * @version 1.0 
 * 
 * @since 1.0 - 24 de abril de 2024 
 **********************************************************************************************************************/
public final class Tag {
    
    private final String tagName;
    
    private final HashMap<String, String> attrMap;

    private final int startContentIndex;

    private String tagContent;
    
    private boolean notifyClosing;

    private TagParser previousTagParser;
    
    /*******************************************************************************************************************
     * Apenas objetos das classes XMLParser e HTMLParser devem criar objetos Tag.
     * 
     * @param tagName O nome da tag.
     * 
     * @param tagAttrs A <code>String</code> com a lista de atributos da tag.
     * 
     * @param startContentIndex A posicao no arquivo do primeiro caractere do escopo da tag.
     * Sem efeito para tags self-closing.
     ******************************************************************************************************************/
    protected Tag(
        final String tagName, 
        final String tagAttrs, 
        final int startContentIndex) {
        
        this.tagName = tagName;        

        attrMap = getAttrMap(tagAttrs);
           
        this.startContentIndex = startContentIndex;
        
        notifyClosing = false;
        
        tagContent = null;
        
        previousTagParser = null;
        
    }//construtor
    
    /*==================================================================================================================
     * Cria e retorna um mapa com os pares chave/valor de todos os atributos da tag.
     =================================================================================================================*/
    private HashMap<String, String> getAttrMap(final String tag) {
        
        toolbox.regex.Regex regex = new toolbox.regex.Regex(" (.+?)=\"(.+?)\"");
        
        regex.setTarget(tag);
        
        HashMap<String, String> mapKeyValue = new HashMap<>();
        
        while (regex.find() != null) mapKeyValue.put(regex.group(1), regex.group(2));
            
        return mapKeyValue;
        
    }//getAttrMap
    
    /**
     * 
     * @param t 
     */
    protected void setPreviousParser(final TagParser t) {
        
        previousTagParser = t;
        
    }//setPreviousParser
    
    /**
     * 
     * @return 
     */
    protected TagParser getPreviousParser() {
        
        return previousTagParser;
        
    }//getPreviousParser
     
    /*******************************************************************************************************************
     * Retorna o indice do primeiro caractere do escopo da tag.
     * 
     * @return Indice do primeiro caractere do escopo da tag.
     ******************************************************************************************************************/
    public int getStartContentIndex() {
        
        return startContentIndex;
        
    }//getStartContentIndex
    
    /**
     * 
     * @return 
     */
    public int getEndContentIndex() {
        
        if (tagContent == null) return -1;
        
        return startContentIndex + tagContent.length();
        
    }//getEndContentIndex
    
    /*******************************************************************************************************************
     * Retorna o mapa com os pares chaves/valor dos atributos da tag.
     * 
     * @return Um <code>HashMap</code> com os pares chaves/valor com os atributos da tag.
     ******************************************************************************************************************/
    public HashMap<String, String> getAttrMap() {
        
        return attrMap;
        
    }//getAttrMap
    
    /*******************************************************************************************************************
     * Retorna o nome da tag.
     * 
     * @return O nome da tag.
     ******************************************************************************************************************/
    public String getTagId() {
        
        return tagName;
        
    }//getTagId
    
    /**
     * 
     * @param content 
     */
    protected void setTagContent(final String content) {
        
        tagContent = content;
        
    }//setTagContent    
    
    /*******************************************************************************************************************
     * Retorna o conteudo da tag, que eh atribuido por um objeto da classe XMLParser ou HTMLParser. 
     * Ou <code>null</code> caso ainda nao tenha sido atribuido.
     * 
     * @return O conteudo da tag, que eh atribuido por um objeto da classe XMLParser ou HTMLParser. 
     * Ou <code>null</code> caso ainda nao tenha sido atribuido ou a tag nao tiver escopo (self-closing).
     ******************************************************************************************************************/
    public String getTagContent() {
        
        return tagContent;
        
    }//getContent
    
    /**
     * 
     * @param attr
     * @param value
     * @return 
     */
    public boolean contains(final String attr, final String value) {
        
        String v = attrMap.get(attr);
        
        return (value.equals(v));
        
    }//contains
 
    /**
     * 
     * @param clas
     * @return 
     */
    public boolean isClass(final String clas) {
        
        String v = attrMap.get("class");
        
        if (v == null) return false;
        
        Scanner scanner = new Scanner(v);
        
        while (scanner.hasNext()) if (scanner.next().equalsIgnoreCase(clas)) return true;
        
        return false;
 
    }//isClass
    
    /*******************************************************************************************************************
     * Deve ser chamado por um objeto <code>TagParser</code> quando este for notificado da abertura 
     * da tag (com a chamada do seu metodo openTag), se for desejado que tambem o fechamento da tag
     * seja notificado. Nao tem efeito em caso de tags self-closing.
     ******************************************************************************************************************/
    public void notifyClosing() {
        
        notifyClosing = true;
        
    }//notifyClosing    
    
    /*******************************************************************************************************************
     * Para uso de objetos das classes <code>XMLParser e HTMLParser</code> apenas.
     * 
     * @return <code>true</code> se o metodo closeTag() sera executado quando for localizada a tag de 
     * fechamento correspondente.
     ******************************************************************************************************************/
    protected boolean isNotifyClosingRequired() {
        
        return notifyClosing;
        
    }//isNotifyClosingRequired
    
    /*******************************************************************************************************************
     * Exibe textualmente a tag, seus atributos e conteudo.
     * 
     * @return A nome da tag, seus atributos e conteudo.
     ******************************************************************************************************************/    
    @Override
    public String toString() {
        
        HashMap<String, String> hashMap = getAttrMap();
        
        StringBuilder sb = new StringBuilder();
        
        for (String key : hashMap.keySet()) 
            sb.append(" ").append(key).append("=\"").append(hashMap.get(key)).append('"');


        return String.format("%s : %s%n%s", getTagId(), sb.toString(), getTagContent());
    }

}//classe Tag