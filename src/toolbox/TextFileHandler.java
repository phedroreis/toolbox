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
     * O objetivo deste método é bloquear certos padrões de substrings, para 
     * que não sejam localizadas pelo método 
     * {@link TextFileHandler#edit(toolbox.Regex, toolbox.TextFileEditor) edit} ou
     * o método {@link TextFileHandler#edit(String, toolbox.TextFileEditor) edit}.
     * 
     * <p>As substrings que corresponderem ao padrão passado no argumento, não 
     * serão localizadas pela regex de nenhum dos métodos edit(). Mesmo 
     * que correspondam ao padrão.</p>
     * 
     * <p>No entanto, ainda existe possibilidade desta substring ser editada
     * caso ela ocorra como substring de uma String localizada por algum dos
     * métodos edit.</p>
     * 
     * <p>Neste caso, o método edit não terá localizado a substring bloqueada,
     * mas sim a String que a contém. E ao editar esta String, poderá, 
     * eventualmente, vir a editar a própria substring bloqueada.</p>
     * 
     * <p>Suponha que se queira editar, em um arquivo tipo HTML, todas as tags
     * img para alterar seus atributos alt. Porém imagine que não seja 
     * desejável editar os títulos h1 deste arquivo, mesmo que estes 
     * contenham tags img.</p>
     * 
     * <p>Neste caso, uma expressão regular que localize tags h1 e seus 
     * escopos, se passada a este método previamente, antes da execução de um
     * dos métodos edit, impediria que as tags img no escopo destes h1 fossem
     * eitadas</p>
     * 
     * @param regex Um objeto Regex construído com uma expressão regular
     * definindo quais substrings não serão localizadas pelos métodos edit.
     */
    /*-------------------------------------------------------------------------
 
    --------------------------------------------------------------------------*/     
    public void lock(final Regex regex) {
        
        if (content == null) return;
       
        regex.setTarget(content);
        
        String match;
        
        while ((match = regex.find()) != null) {
            
            lock.put("\u0ca0\u13c8" + countLocks++ + "\u13da\u0ca0", match);
        }
        
        for (String key : lock.keySet()) {
            
            content = content.replace(lock.get(key), key);
        }
        
    }//lock
    
  /**
     * O objetivo deste método é bloquear certos padrões de substrings, para 
     * que não sejam localizadas pelo método 
     * {@link TextFileHandler#edit(toolbox.Regex, toolbox.TextFileEditor) edit} ou
     * o método {@link TextFileHandler#edit(String, toolbox.TextFileEditor) edit}.
     * 
     * <p>As substrings que corresponderem ao padrão passado no argumento, não 
     * serão localizadas pela regex de nenhum dos métodos edit(). Mesmo 
     * que correspondam ao padrão.</p>
     * 
     * <p>No entanto, ainda existe possibilidade desta substring ser editada
     * caso ela ocorra como substring de uma String localizada por algum dos
     * métodos edit.</p>
     * 
     * <p>Neste caso, o método edit não terá localizado a substring bloqueada,
     * mas sim a String que a contém. E ao editar esta String, poderá, 
     * eventualmente, vir a editar a própria substring bloqueada.</p>
     * 
     * <p>Suponha que se queira editar, em um arquivo tipo HTML, todas as tags
     * img para alterar seus atributos alt. Porém imagine que não seja 
     * desejável editar os títulos h1 deste arquivo, mesmo que estes 
     * contenham tags img.</p>
     * 
     * <p>Neste caso, uma expressão regular que localize tags h1 e seus 
     * escopos, se passada a este método previamente, antes da execução de um
     * dos métodos edit, impediria que as tags img no escopo destes h1 fossem
     * eitadas</p>
     * 
     * @param regex Um expressão regular definindo quais substrings não serão
     * localizadas pelos métodos edit.
     */   
     /*-------------------------------------------------------------------------
 
    --------------------------------------------------------------------------*/     
    public void lock(final String regex) {
       
        lock(new Regex(regex));        

    }//lock  
    
    /**
     * Restaura todos os blocos que foram travados para edição.
     */
    /*-------------------------------------------------------------------------
 
    --------------------------------------------------------------------------*/       
    public void restoreLocks() {
        
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
     * 
     * @param editor Um objeto de uma classe que estenda toolbox.TextFileEditor
     * e forneça o método para editar propriamente as substrings localizadas por
     * este método.
     */
    /*-------------------------------------------------------------------------
 
    --------------------------------------------------------------------------*/      
    public void edit(final Regex regex, final TextFileEditor editor) {
        
        if (content == null) return;
        
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
        
        restoreLocks();
        
        scanner = new Scanner(content);
           
    }//edit    
    
    /**
     * Possibilita editar todas as substrings do arquivo que corresponderem ao 
     * padrão.
     * 
     * @param regex Uma expressão regular que localize o tipo de padrão a ser 
     * editado.
     * 
     * @param editor Um objeto de uma classe que estenda toolbox.TextFileEditor
     * e forneça o método para editar propriamente as substrings localizadas por
     * este método.
     */    
    /*-------------------------------------------------------------------------
 
    --------------------------------------------------------------------------*/      
    public void edit(final String regex, final TextFileEditor editor) {
        
        edit(new Regex(regex), editor);
           
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
    
    /**
     * Exemplo de uso.
     * 
     * @param args Não usado.
     * 
     * @throws IOException Em caso de erro de IO.
     */
    /*-------------------------------------------------------------------------
 
    --------------------------------------------------------------------------*/     
    public static void main(String[] args) throws IOException {

        TextFileHandler textFile = new TextFileHandler("teste.txt");
        
        textFile.read();
        
        textFile.lock(new Regex("\\(22\\)3256-8890"));
        
        textFile.edit(
            "(\\(\\d{2}\\))?((9\\d{4})|[32]\\d{3})-\\d{4}", 
            new MyEditor()
        );
        
        textFile.writeWithExtPrefix("test");
        
        System.out.println(textFile.getPath() + "---" + getPath("teste.txt"));

        
     }//main
    
    private static class MyEditor extends TextFileEditor {

        @Override
        public String edit(String match) {
            
            if (match.startsWith("(22)")) 
                return match.replace("(22)" , "(33)");
            else
                return match;
        }
        
    }
    
}//classe TextFileHandler