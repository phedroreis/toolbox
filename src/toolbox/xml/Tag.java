package toolbox.xml;

import java.util.HashMap;

/***********************************************************************************************************************
 * Um objeto dessa classe armazena e fornece os dados de tags XML e HTML.
 * 
 * <p>O nome da tag, a lista de atributos (se houver) e possivelmente o conteudo do escopo da tag.
 * 
 * @author Pedro Reis
 * @version 1.0 - 24 de abril de 2024 
 * @since 1.0
 **********************************************************************************************************************/
public final class Tag {
    
    private final String tagName;
    
    private final HashMap<String, String> attrMap;
    
    private String content;
    
    private final int startBlockIndex;
    
    private int endBlockIndex;
    
    private final int startContentIndex;
    
    private boolean notifyClosing;
    
    private static int scopeLevel;
    
    private int scope;
    
    /*******************************************************************************************************************
     * Apenas objetos das classes XMLParser e HTMLParser devem criar objetos Tag.
     * 
     * @param tagName O nome da tag.
     * 
     * @param tagAttrs A <code>String</code> com a lista de atributos da tag.
     * 
     * @param startBlockIndex A posicao no arquivo do primeiro caractere da propria tag.
     * 
     * @param startContentIndex A posicao no arquivo do primeiro caractere do escopo da tag.
     * Sem efeito para tags self-closing.
     ******************************************************************************************************************/
    protected Tag(
        final String tagName, 
        final String tagAttrs, 
        final int startBlockIndex, 
        final int startContentIndex) {
        
        this.tagName = tagName;        

        attrMap = getAttrMap(tagAttrs);
        
        this.startBlockIndex = startBlockIndex;
        
        this.startContentIndex = startContentIndex;
        
        notifyClosing = false;
        
        content = null;
        
        endBlockIndex = -1;
        
        scope = scopeLevel;
        
    }//construtor
     
    /*==================================================================================================================
     * Retorna um mapa com os pares chave/valor de todos os atributos da tag.
     =================================================================================================================*/
    private HashMap<String, String> getAttrMap(final String tag) {
        
        toolbox.regex.Regex regex = new toolbox.regex.Regex(" (.+?)=\"(.+?)\"");
        
        regex.setTarget(tag);
        
        HashMap<String, String> mapKeyValue = new HashMap<>();
        
        while (regex.find() != null) mapKeyValue.put(regex.group(1), regex.group(2));
            
        return mapKeyValue;
        
    }//getAttrMap
    
    /*******************************************************************************************************************
     * Retorna o indice do primeiro caractere do escopo da tag.
     * 
     * @return Indice do primeiro caractere do escopo da tag.
     ******************************************************************************************************************/
    public int getStartContentIndex() {
        
        return startContentIndex;
        
    }//getStartContentIndex
     
    /*******************************************************************************************************************
     * Retorna o indice do primeiro caractere da tag de fechamento.
     * 
     * @return O indice no arquivo do primeiro caractere da tag de fechamento. Se a tag de fechamento 
     * ainda nao foi localizada pelo parsing, retorna -1.
     ******************************************************************************************************************/
    public int getCloseTagIndex() {
        
        if (content == null) return -1;
        
        return startContentIndex + content.length();
        
    }//getCloseTagIndex
    
    /*******************************************************************************************************************
     * Atribui a posicao de fechamento do bloco de uma tag. Deve ser chamado apenas pelos metodos 
     * XmlParser e HtmlParser desta classe.
     * 
     * @param endBlockIndex A posicao no arquivo HTML onde o bloco de uma determinada tag termina. 
     * Obs: O bloco inclui a propria tag assim como sua correspondente tag de fechamento. 
     ******************************************************************************************************************/
    protected void setEndBlockIndex(final int endBlockIndex) {
        
        this.endBlockIndex = endBlockIndex;
        
    }//setEndBlockIndex
    
    /*******************************************************************************************************************
     * Retorna a posicao no arquivo do primeiro caractere da propria tag.
     * 
     * @return A posicao no arquivo do primeiro caractere da propria tag.
     ******************************************************************************************************************/
    public int getStartBlockIndex() {
        
        return startBlockIndex;
        
    }//getStartBlockIndex    
    
    /*******************************************************************************************************************
     * Retorna a posicao no arquivo do ultimo caractere da tag de fechamento. 
     * 
     * @return A posicao no arquivo do ultimo caractere da tag de fechamento.  Ou -1 caso nao exista 
     * tag de fechamento ou se a tag de fechamento ainda nao foi localizada pelo parsing. 
     * 
     * <p>Ou seja, se este metodo for chamado dentro do metodo openTag() que implementa a interface
     * TagParser, retornara -1. Portanto soh faz sentido usar o valor de retorno deste metodo em uma
     * implementacao do metodo closeTag()da interface TagParser.
     * 
     * <p>Obs: o metodo closeTag() nao eh chamado para tags que nao possuem tag de fechamento.
     ******************************************************************************************************************/
    public int getEndBlockIndex() {
        
        return endBlockIndex;
        
    }//getEndBlockIndex    
    
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
    public String getTagName() {
        
        return tagName;
        
    }//getTagName
    
    /*******************************************************************************************************************
     * Apenas objetos das classes XMLParser e HTMLParser devem chamar este metodo.
     * 
     * @param content O conteudo do escopo da tag. 
     ******************************************************************************************************************/
    protected void setContent(final String content) {
        
        this.content = content;
        
    }//setContent
    
    /*******************************************************************************************************************
     * Retorna o conteudo da tag, que eh atribuido por um objeto da classe XMLParser ou HTMLParser. 
     * Ou <code>null</code> caso ainda nao tenha sido atribuido.
     * 
     * @return O conteudo da tag, que eh atribuido por um objeto da classe XMLParser ou HTMLParser. 
     * Ou <code>null</code> caso ainda nao tenha sido atribuido ou a tag nao tiver escopo (self-closing).
     ******************************************************************************************************************/
    public String getContent() {
        
        return content;
        
    }//getContent
    
    /*******************************************************************************************************************
     * Deve ser chamado por um objeto <code>TagParser</code> quando este for notificado da abertura 
     * da tag (com a chamada do seu metodo openTag), se for desejado que tambem o fechamento da tag
     * seja notificado. Nao tem efeito em caso de tags self-closing.
     ******************************************************************************************************************/
    public void notifyClosing() {
        
        notifyClosing = true;
        
    }//notifyClosing
    
    /*******************************************************************************************************************
     * Deve ser chamado por um objeto <code>TagParser</code> quando este for notificado da abertura 
     * da tag (com a chamada do seu metodo openTag), se for desejado que o fechamento da tag seja 
     * notificado e o parsing passe a ser realizado considerando o escopo da tag como uma extensao
     * do processamento dessa propria tag. Isto e, os metodos openTagLevelX() e closeTagLevelX() 
     * (da classe TagParser) e que passam a ser chamados ate que o fechamento da tag que executou este
     * metodo seja encontrado.
     * 
     * <p>Encontrado o fechamento da referida tag, openTagLevelX-1() e closeTagLevelX-1() voltam a ser
     * os metodos chamados pelo parse() quando da localizacao de uma tag de abertura ou fechamento.
     * 
     * <p>Nao pode ser chamado para tags self-closing.
     ******************************************************************************************************************/
    public void parseInnerScope() {
        
        notifyClosing = true;
        
        scopeLevel++;
        
    }//parseInnerScope
    
    /*
    * Executado apenas por metodos de classes deste pacote.
    */
    protected static void setScopeLevel(final int sl) {
        
        scopeLevel = sl;
        
    }//setScopeLevel
    
    /*
    * Executado apenas por metodos de classes deste pacote.
    */    
    protected static int getScopeLevel() {
        
        return scopeLevel;
        
    }//getScopeLevel
    
    /*
    * Executado apenas por metodos de classes deste pacote.
    */    
    protected int getScope() {
        
        return scope;
        
    }//getScope
    
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
     * Exibe textualmente a tag, seus atributos, localizacao no arquivo e localizacao do escopo.
     * 
     * @return A tag e seus atributos, localizacao no arquivo e localizacao do escopo da tag.
     ******************************************************************************************************************/    
    @Override
    public String toString() {
        
        HashMap<String, String> hashMap = getAttrMap();
        
        StringBuilder sb = new StringBuilder();
        
        for (String key : hashMap.keySet()) 
            sb.append(" ").append(key).append("=\"").append(hashMap.get(key)).append('"');
        
        sb.append(" [").
        append(getStartBlockIndex()).append(',').append(getEndBlockIndex()).
        append("] [").
        append(getStartContentIndex()).append(',').append(getCloseTagIndex() - 1).
        append("]");

        return String.format("%s : %s%n", getTagName(), sb.toString());
    }

}//classe Tag