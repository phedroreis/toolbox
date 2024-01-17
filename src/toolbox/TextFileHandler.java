package toolbox;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Uma classe para edição de arquivos texto de até 2GB.
 * 
 * <p>Um objeto desta classe carrega um arquivo texto inteiramente na memória
 * em uma única operação atômica e realiza todas as edições necessárias nesta
 * cópia em memória do arquivo. E realizando a gravação da cópia editada, de
 * volta no arquivo, também em uma única operação de gravação.</p>
 * 
 * @since 1.0 - 14 de janeiro de 2024
 * @version 1.0
 * @author Pedro Reis
 */
public class TextFileHandler {
    
    private final String pathname;
    
    private String content;
    
    private Charset charset;
    
    private Scanner scanner;
    
    private final String dir;
    
    private final String filename;
    
    private final String extension;
    
    private int countLocks;
    
    private Map<String, String> lock;  
    
    /**
     * Obtém somente o caminho do arquivo passado no argumento 
     * <b><i>pathname</i></b>. Sem o nome do arquivo.
     * 
     * <p>O método tenta obter o caminho absoluto, mas se não for possível
     * retorna o caminho relativo.</p>
     * 
     * <p>Se não foi possível obter o caminho absoluto e não foi passado no
     * argumento <b><i>pathname</i></b> nenhum caminho relativo, então retorna
     * <code>null</code>
     * 
     * @param pathname Caminho de arquivo ou diretório.
     * 
     * @return O caminho absoluto, se possível. Ou o caminho relativo ou
     * <code>null</code> se este não existir no argumento <b><i>pathname</i></b>.
     */
    /*-------------------------------------------------------------------------
 
    --------------------------------------------------------------------------*/     
    public static String getPath(final String pathname) {
        try {
            
            return new File(new File(pathname).getCanonicalPath()).getParent();
        }
        catch (IOException e) {
            
            return new File(pathname).getParent();
        }
        
    }//getPath

    /**
     * Obtém somente o caminho do arquivo passado no argumento 
     * <b><i>pathname</i></b> do construtor da classe. Sem o nome do arquivo.
     * 
     * <p>O método tenta obter o caminho absoluto, mas se não for possível
     * retorna o caminho relativo.</p>
     * 
     * <p>Se não foi possível obter o caminho absoluto e não foi passado no
     * argumento <b><i>pathname</i></b> nenhum caminho relativo, então retorna
     * <code>null</code>
     * 
     * @return O caminho absoluto, se possível. Ou o caminho relativo ou
     * <code>null</code> se este não existir no argumento <b><i>pathname</i></b>.
     */
    /*-------------------------------------------------------------------------
 
    --------------------------------------------------------------------------*/     
    public String getPath() {
        
        return dir;
        
    }//getPath
    
    /**
     * Obtém o nome do arquivo no argumento <b><i>pathname</i></b>.
     * 
     * @param pathname O pathname de um arquivo.
     * 
     * @return O nome do arquivo. Sem o caminho.
     */
    /*-------------------------------------------------------------------------
 
    --------------------------------------------------------------------------*/     
    public static String getName(final String pathname) {
        
        return new File(pathname).getName();
        
    }//getName
    
    /**
     * Obtém o nome do arquivo no argumento <b><i>pathname</i></b> passado ao
     * construtor da classe.
     * 
     * @return Somente o nome do arquivo. Sem o caminho.
     */
    /*-------------------------------------------------------------------------
 
    --------------------------------------------------------------------------*/     
    public String getName() {
        
        return filename;
        
    }//getName
    
    /**
     * Obtém a extensão no nome de um arquivo. Sem o ponto.
     * 
     * @param pathname O pathname do arquivo.
     * 
     * @return A extensão do arquivo sem o ponto.
     */
    /*-------------------------------------------------------------------------
 
    --------------------------------------------------------------------------*/     
    public static String getExt(final String pathname) {
        
        int p = pathname.lastIndexOf('.');
        if (p < 1) return "";
        return pathname.substring(p + 1, pathname.length());
        
    }//getExt
    
     /**
     * Obtém a extensão do arquivo no argumento <b><i>pathname</i></b> passado 
     * ao construtor da classe.
     * 
     * @return Somente a extensão do arquivo. Sem o ponto.
     */   
    /*-------------------------------------------------------------------------
 
    --------------------------------------------------------------------------*/     
    public String getExt() {
        
        return extension;
        
    }//getExt
    
    /**
     * Obtém o pathname como foi passado ao construtor da classe.
     * 
     * @return O pathname como foi passado ao construtor da classe.
     */
    /*-------------------------------------------------------------------------
 
    --------------------------------------------------------------------------*/     
    public String getPathname() {
        
        return pathname;
        
    }//getPathname
    
    /**
     * Construtor da classe.
     * 
     * @param pathname O pathname do arquivo que será lido. Não pode exceder 
     * 2GB.
     * 
     * @param charsetName O encoding que será usado para ler e gravar o arquivo.
     * 
     * <p>Os seguintes parâmetros são aceitos:</p>
     * <ul>
     * <li>iso-8859-1
     * <li>us-ascii
     * <li>utf16
     * <li>utf_16be
     * <li>utf_16le
     * <li>utf8
     * </ul>
     */
    /*-------------------------------------------------------------------------
 
    --------------------------------------------------------------------------*/     
    public TextFileHandler(final String pathname, final String charsetName) {
        
        this.pathname = pathname;
        
        dir = getPath(pathname);
        
        filename = getName(pathname);
        
        extension = getExt(filename);
        
        content = null;
        
        charset = Charset.forName(charsetName);
        
        countLocks = 0;
        
        lock = new HashMap<>();  
        
    }//construtor
    
    /**
     * Construtor.
     * 
     * @param pathname O pathname do arquivo que será lido e/ou gravado.
     * Não pode exceder 2GB.
     */
    /*-------------------------------------------------------------------------
 
    --------------------------------------------------------------------------*/     
    public TextFileHandler(final String pathname) {
        
         this(pathname, Charset.defaultCharset().toString());
         
    }//construtor
    
    /**
     * Carrega um novo conteúdo para o objeto <b><i>TextFileHandler</i></b>.
     * Descartando o anterior.
     * 
     * @param newContent Novo conteúdo.
     */
    /*-------------------------------------------------------------------------
 
    --------------------------------------------------------------------------*/  
    public void setContent(final String newContent) {
        
        content = newContent;
        scanner = new Scanner(content);
        
    }//setContent
    
    /**
     * Retorna o conteúdo atual do arquivo que foi lido.
     * 
     * @return O conteúdo atual do arquivo lido.
     */
    /*-------------------------------------------------------------------------
 
    --------------------------------------------------------------------------*/     
    public String getContent() {
        
        return content;
        
    }//getContent    

    /*-------------------------------------------------------------------------
      O objetivo deste método é bloquear certos padrões de substrings, para 
      que não sejam localizadas pelo método edit()
    --------------------------------------------------------------------------*/     
    private void lock(final String[] patterns) {
        
        if (patterns == null) return;
               
        for (String pattern : patterns) {
            
            Regex regex = new Regex(pattern);
            
            regex.setTarget(content);

            String match;

            while ((match = regex.find()) != null) {

                lock.put("\u0ca0\u13c8" + countLocks++ + "\u13da\u0ca0", match);
            }

            for (String key : lock.keySet()) {

                content = content.replace(lock.get(key), key);
            }
        }
        
    }//lock    
    
    /*-------------------------------------------------------------------------
             Restaura todos os blocos que foram travados para edição.
    --------------------------------------------------------------------------*/       
    private void restoreLocks() {
        
        for (String key : lock.keySet()) {
            
            content = content.replace(key, lock.get(key));
        } 
        
        lock = new HashMap<>();
        
        countLocks = 0;
        
    }//restoreLocks
    
    /**
     * Possibilita editar todas as substrings do arquivo que corresponderem ao 
     * padrão.
     * 
     * @param regex Um objeto Regex construído com uma expressão regular que
     * localize o tipo de padrão a ser editado.
     * @param lockPatterns
     * 
     * @param editor Um objeto de uma classe que estenda toolbox.TextFileEditor
     * e forneça o método para editar propriamente as substrings localizadas por
     * este método.
     */
    /*-------------------------------------------------------------------------
 
    --------------------------------------------------------------------------*/      
    public void edit(
        final Regex regex,
        final String[] lockPatterns,
        final TextFileEditor editor
    ) {
        
        if (content == null) return;
        
        lock(lockPatterns);
        
        Map<String, String> map = new HashMap<>();  
        
        regex.setTarget(content);
        
        String match;
        
        while ((match = regex.find()) != null) {
            
            String edited = editor.edit(match);
            
            if (edited != null) map.put(match, edited);
        }
        
        for (String key : map.keySet()) {
            
            content = content.replace(key, map.get(key));
        }
        
        if (lockPatterns != null) restoreLocks();
        
        scanner = new Scanner(content);
           
    }//edit    
    
    /**
     * Possibilita editar todas as substrings do arquivo que corresponderem ao 
     * padrão.
     * 
     * @param regex Uma expressão regular que localize o tipo de padrão a ser 
     * editado.
     * @param lockPatterns
     * 
     * @param editor Um objeto de uma classe que estenda toolbox.TextFileEditor
     * e forneça o método para editar propriamente as substrings localizadas por
     * este método.
     */    
    /*-------------------------------------------------------------------------
 
    --------------------------------------------------------------------------*/      
    public void edit(
        final String regex,
        final String[] lockPatterns,
        final TextFileEditor editor) {
        
        edit(new Regex(regex), lockPatterns, editor);
           
    }//edit
    
    /**
     * Lê o arquivo para a memória em uma única e atômica operação.
     * 
     * <p>Suporta somente arquivos texto de até 2GB.</p>
     * 
     * @throws IOException Em caso de erro de IO.
     */
    /*-------------------------------------------------------------------------
 
    --------------------------------------------------------------------------*/     
    public void read() throws IOException {
                   
        setContent( Files.readString(Path.of(pathname), charset) );
        
    }//read
    
    /**
     * Escreve o arquivo no disco, sobrescrevendo o arquivo pathname, se este já
     * existir.
     * 
     * @param pathname O pathname do arquivo que será gravado.
     * 
     * @throws IOException Em caso de erro de IO.
     */
    /*-------------------------------------------------------------------------
 
    --------------------------------------------------------------------------*/     
    public void write(final String pathname) throws IOException {
        
        if (content == null) return;
        
        Files.writeString(
            Path.of(pathname),
            content,
            charset , 
            StandardOpenOption.CREATE
        );
        
    }//write
    
    /**
     * Grava o arquivo no disco, sobrescrevendo o arquivo que foi lido.
     * 
     * @throws IOException Em caso de erro de IO.
     */
    /*-------------------------------------------------------------------------
 
    --------------------------------------------------------------------------*/        
    public void write() throws IOException {
        
        write(pathname);
        
    }//write 
    
    /**
     * Obtém o nome do arquivo que será gravado pelo método 
     * {@link toolbox.TextFileHandler#writeWithExtPrefix(java.lang.String) 
     * writeWithExtPrefix}, caso o argumento <b><i>extensionPrefix</i></b> seja
     * passado a este.
     * 
     * @param extensionPrefix O prefixo.
     * 
     * @return O nome do arquivo que seria gravado com esste prefixo usado na
     * extensão.
     */
    /*-------------------------------------------------------------------------
 
    --------------------------------------------------------------------------*/      
    public String getFilenameWithExtPrefix(final String extensionPrefix) {
        
       String ext = getExt(); 

       return pathname.replace(ext, extensionPrefix + '.' + ext);      
       
    }//getFilenameWithExtPrefix
    
    /**
     * Grava o arquivo no disco em outro arquivo diferente do que foi lido, mas
     * com o mesmo nome, porém com um prefixo acrescido à extensão do arquivo
     * original que foi lido pelo método {@link TextFileHandler#read() read} desta 
     * classe.
     * 
     * @param extensionPrefix Prefixo, sem o ponto, que será usado para 
     * diferenciar o nome do arquivo que será gravado, do nome do arquivo que
     * foi lido. Evitando que o original seja sobrescrito.
     *  
     * @throws IOException Em caso de erro de IO.
     */
    /*-------------------------------------------------------------------------
 
    --------------------------------------------------------------------------*/     
    public void writeWithExtPrefix(final String extensionPrefix)
        throws IOException {
       
        write(getFilenameWithExtPrefix(extensionPrefix));
        
    }//writeWithExtPrefix
    
    /**
     * Informa se existe mais alguma linha ou token a ser lido no arquivo.
     * 
     * @return true se existir mais algum token ou linha a ser lida. false se
     * não.
     */
    /*-------------------------------------------------------------------------
 
    --------------------------------------------------------------------------*/     
    public boolean hasNext() {
        
        if (content == null) return false;
        
        boolean hasNext = scanner.hasNext();
        
        if (!hasNext) scanner = new Scanner(content);
        
        return hasNext;
        
    }//hasNext
    
    /**
     * Lê a próxima linha, se houver.
     * 
     * @return A próxima linha ainda não lida ou null se não houver.
     */
    /*-------------------------------------------------------------------------
 
    --------------------------------------------------------------------------*/     
    public String nextLine() {
        
        if (scanner.hasNext()) 
            return scanner.nextLine();
        else {
            scanner = new Scanner(content);
            return null;
        }
        
    }//nextLine
    
    /**
     * Lê o próximo token, se houver.
     * 
     * @return O próximo token ainda não lido ou null se não houver.
     */    
    /*-------------------------------------------------------------------------
 
    --------------------------------------------------------------------------*/     
    public String nextToken() {
        
        if (scanner.hasNext()) 
            return scanner.next();
        else {
            scanner = new Scanner(content);
            return null;
        }
        
    }//nextToken
    
    /**
     * O conteúdo atual na memória.
     * 
     * @return O conteúdo que será gravado no disco se o método write for
     * chamado.
     */
    /*-------------------------------------------------------------------------
 
    --------------------------------------------------------------------------*/      
    @Override
    public String toString() {
        
        return content;
        
    }//toString
     
}//classe TextFileHandler