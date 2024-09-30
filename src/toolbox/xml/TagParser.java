package toolbox.xml;

import javax.management.modelmbean.XMLParseException;


/***********************************************************************************************************************
 * Metodos que serao executados pelo metodo <i><b>parse()</b></i> das classes HtmlParser e
 * XmlParser deste pacote, ao localizar uma tag de abertura ou uma tag de fechamento.
 * 
 * @author Pedro Reis 
 * @version 1.0 
 * @since 1.0 - 24 de abril de 2024
 **********************************************************************************************************************/
public abstract class TagParser {
    
    /**
     * O parsing de um documento HTML ou XML e realizado pelo metodo parse() de objetos HtmlParser ou XmlParser,
     * respectivamente. Objetos destas classes devem receber em seus construtores, um objeto de uma classe
     * que herde desta classe. Deste modo, o metodo parse(), ao localizar uma tag de abertura (de uma tag de 
     * escopo ou de autofechamento), executara o metodo openTag() , passando a este um objeto Tag contendo dados sobre 
     * a tag localizada.
     * 
     * <p>Assim e possivel que o metodo openTag() verifique se e de interesse processar a referida tag, devendo entao
     * executar o metodo notifyClosing() da classe Tag. Sendo esta notificacao registrada no proprio objeto Tag,
     * quando parse() localizar a correspondente tag de fechamento, sera executado closeTag(), no qual o paramentro
     * Tag ja retornara informacoes adicionais que so podem ser obtidas apos o fechamento da tag (como o conteudo
     * do escopo da tag, por exemplo).
     * 
     * <p>Assim, implementacoes dos metodos openTag() e closeTag(), podem extrair os dados que forem necessarios
     * de um documento HTML ou XML, assim como realizar qualquer operacao necessaria com estes dados.
     * 
     * <p>Porem, se for necessario o processamento de tags aninhadas, podem ser sobrescritos os metodos openTagX() e
     * closeTagX, onde X representa o nivel de aninhamento da tag. Por exemplo: se e necessario extrair os dados de 
     * tags li aninhadas em tags ul de atributo class="lista", podem ser sobrescritos os metodos openTagLevel0() e
     * closeTagLevel0() oara localizar a tag externa ul e, no metodo openTagLevel0(), deve ser chamado o metodo 
     * parseInnerScope() (da classe Tag) para sinalizar que o parsing passa a ser realizado nas tags do escopo de ul. 
     * E, sendo assim, deverao, neste caso, serem sobrescritos tambem os metodos openTagLevel1() e closeTagLevel1().
     * 
     * <p>Estes seriam os metodos responsaveis por extrair dados das tag li aninhadas. 
     * 
     * <p>Quando localizado o fechamento da tag externa ul, este sera automaticamente processado pelo closeTagLevel0()
     * e a partir disso o metodo openTagLevel0() e o que volta a ser executado ate que nova tag ul class="lista" seja
     * localizada pelo metodo openTagLevel0().
     * 
     * <p>Para maiores informacoes, consulte a documentacao das classes Tag, HtmlParser e XmlParser deste pacote.
     * 
     * @param tag Este objeto fornece dados sobre a tag localizada: como nome da tag, seus atributos, localizacao 
     * no arquivo, conteudo de seu escopo (se houver), etc... 
     * 
     * <p>Alguns destes dados ja estao disponiveis quando a 
     * tag de abertura e localizada, outros so podem ser obtidos quando a tag correspondente de fechamento for 
     * localizada e so poderao ser extraidos quando o metodo closeTag() for executado.
     * 
     * @throws XMLParseException Se a analise dos dados do objeto Tag revelar algum problema no processamento,
     * como uma tag com atributos inesperados, ou algo assim, este metodo podera lancar uma excecao deste tipo
     * ou subclasse desta, informando sobre o erro. 
     *
     */
    public void openTag(final Tag tag) throws XMLParseException {
        
        switch (Tag.getScopeLevel()) {
            
            case 0:
                openTagLevel0(tag);
                break;
            case 1: 
                openTagLevel1(tag);
                break;
            case 2:
                openTagLevel2(tag);
                break;
            case 3:
                openTagLevel3(tag);
                
        }//switch  
        
    }//openTag
    
    /**
     * O parsing de um documento HTML ou XML e realizado pelo metodo parse() de objetos HtmlParser ou XmlParser,
     * respectivamente. Objetos destas classes devem receber em seus construtores, um objeto de uma classe
     * que herde desta classe. Deste modo, o metodo parse(), ao localizar uma tag de abertura (de uma tag de 
     * escopo ou de autofechamento), executara o metodo openTag() , passando a este um objeto Tag contendo dados sobre 
     * a tag localizada.
     * 
     * <p>Assim e possivel que o metodo openTag() verifique se e de interesse processar a referida tag, devendo entao
     * executar o metodo notifyClosing() da classe Tag. Sendo esta notificacao registrada no proprio objeto Tag,
     * quando parse() localizar a correspondente tag de fechamento, sera executado closeTag(), no qual o paramentro
     * Tag ja retornara informacoes adicionais que so podem ser obtidas apos o fechamento da tag (como o conteudo
     * do escopo da tag, por exemplo).
     * 
     * <p>Assim, implementacoes dos metodos openTag() e closeTag(), podem extrair os dados que forem necessarios
     * de um documento HTML ou XML, assim como realizar qualquer operacao necessaria com estes dados.
     * 
     * <p>Porem, se for necessario o processamento de tags aninhadas, podem ser sobrescritos os metodos openTagX() e
     * closeTagX, onde X representa o nivel de aninhamento da tag. Por exemplo: se e necessario extrair os dados de 
     * tags li aninhadas em tags ul de atributo class="lista", podem ser sobrescritos os metodos openTagLevel0() e
     * closeTagLevel0() oara localizar a tag externa ul e, no metodo openTagLevel0(), deve ser chamado o metodo 
     * parseInnerScope() (da classe Tag) para sinalizar que o parsing passa a ser realizado nas tags do escopo de ul. 
     * E, sendo assim, deverao, neste caso, serem sobrescritos tambem os metodos openTagLevel1() e closeTagLevel1().
     * 
     * <p>Estes seriam os metodos responsaveis por extrair dados das tag li aninhadas. 
     * 
     * <p>Quando localizado o fechamento da tag externa ul, este sera automaticamente processado pelo closeTagLevel0()
     * e a partir disso o metodo openTagLevel0() e o que volta a ser executado ate que nova tag ul class="lista" seja
     * localizada pelo metodo openTagLevel0().
     * 
     * <p>Para maiores informacoes, consulte a documentacao das classes Tag, HtmlParser e XmlParser deste pacote.
     * 
     * @param tag Este objeto fornece dados sobre a tag localizada: como nome da tag, seus atributos, localizacao 
     * no arquivo, conteudo de seu escopo (se houver), etc... 
     * 
     * <p>Alguns destes dados ja estao disponiveis quando a 
     * tag de abertura e localizada, outros so podem ser obtidos quando a tag correspondente de fechamento for 
     * localizada e so poderao ser extraidos quando o metodo closeTag() for executado.
     * 
     * @throws XMLParseException Se a analise dos dados do objeto Tag revelar algum problema no processamento,
     * como uma tag com atributos inesperados, ou algo assim, este metodo podera lancar uma excecao deste tipo
     * ou subclasse desta, informando sobre o erro. 
     *
     */   
    public void closeTag(final Tag tag) throws XMLParseException {
        
        Tag.setScopeLevel(tag.getScope());
        
        switch (Tag.getScopeLevel()) {
            
            case 0:
                closeTagLevel0(tag);
                break;
            case 1: 
                closeTagLevel1(tag);
                break;
            case 2:
                closeTagLevel2(tag);
                break;
            case 3:
                closeTagLevel3(tag);
                
        }//switch  
        
    }//closeTag
    
    public void openTagLevel0(final Tag tag) throws XMLParseException {}
    
    public void openTagLevel1(final Tag tag) throws XMLParseException {}
    
    public void openTagLevel2(final Tag tag) throws XMLParseException {}
    
    public void openTagLevel3(final Tag tag) throws XMLParseException {}
    
    public void closeTagLevel0(final Tag tag) throws XMLParseException {}
    
    public void closeTagLevel1(final Tag tag) throws XMLParseException {}
    
    public void closeTagLevel2(final Tag tag) throws XMLParseException {}
    
    public void closeTagLevel3(final Tag tag) throws XMLParseException {}

}//classe TagParser
