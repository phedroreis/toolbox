package toolbox.file;

import java.util.List;
import java.util.LinkedList;
import java.io.IOException;
import java.io.File;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.PatternSyntaxException;


/***********************************************************************************************************************
 * Um objeto desta classe e usado para listar arquivos (e/ou diretorios) contidos em um diretorio e, opcionalmente, nos
 * subdiretorios deste customizadamente filtrados. A pesquisa pode ser filtrada por uma regex confrontada com os nomes dos arquivos ou um objeto de 
 * uma classe que implemente a interface {@link java.nio.file.DirectoryStream.Filter DirectoryStream.Filter&lt;Path&gt;}.
 * 
 * @author Pedro Reis
 * @version 1.0
 * @since 1.0 - 5 de abril de 2024
 **********************************************************************************************************************/
public final class SearchFolders {
    
    private enum Mode {PATH, PATHNAME, FILE};
    
    private List<Path> pathList;
    private List<String> pathNameList;
    private List<File> fileList;

    private final DirectoryStream.Filter<Path> filterFiles;
    
    /*******************************************************************************************************************
     * Cria um objeto SearchFolders que lista arquivos selecionados pelo filtro passado no argumento deste construtor.
     * 
     * @param filterFiles Um filtro que implemente a interface 
     * {@link java.nio.file.DirectoryStream.Filter DirectoryStream.Filter.&lt;Path&gt;}.
     * 
     * <p>No metodo <b><i>accept(Path file)</i></b> desta interface, se for desejado que a pesquisa se estanda tambem
     * aos subdiretorios, e necessario que este retorne <code>true</code> quando o argumento <b><i>file</i></b> for um 
     * subdiretorio.
     ******************************************************************************************************************/
    public SearchFolders(final DirectoryStream.Filter<Path> filterFiles) {
        
        this.filterFiles = filterFiles;
   
    }//construtor
    
    /*******************************************************************************************************************
     * Cria um objeto SearchFolders que lista arquivos cujo nome corresponder a expressao regular passado no 
     * parametro <b><i>regex</i></b>.
     * 
     * @param regex Serao listados apenas os arquivos cujos nomes corresponderem a esta expressao regular. 
     * 
     * <p>Se <code>null</code>, qualquer arquivo e aceito. A regex nao testa o caminho, apenas os nomes dos arquivos.
     * 
     * @param searchSubdirs Se <b><i>true</i></b>, a pesquisa se estendera aos subdiretorios recursivamente.
     * 
     * @throws PatternSyntaxException Caso <b><i>regex</i></b> seja invalida.
     ******************************************************************************************************************/
    public SearchFolders(final String regex, final boolean searchSubdirs) 
        throws PatternSyntaxException {
        
        this(new FilterFiles(regex, searchSubdirs));   
       
    }//construtor   
    
    /*==================================================================================================================
     * 
     =================================================================================================================*/
    private void recursiveSearch(final Path path, final Mode mode) 
        throws IOException, NotDirectoryException, SecurityException {
        
        DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path, filterFiles);
        
        LinkedList<Path> directoryList = new LinkedList<>();
        
        for (Path p : directoryStream) 
        
            if (Files.isDirectory(p))
            
                directoryList.add(p);
                
            else {
                
                switch (mode) {
                    case PATH:
                        pathList.add(p); 
                        break;
                    case PATHNAME:                        
                        pathNameList.add(p.toString()); 
                        break;
                    case FILE:
                        fileList.add(p.toFile());
                }//switch
                
            }//if-else
            
        for (Path p : directoryList) recursiveSearch(p, mode);   
                  
    }//recursiveSearch  
    
    /*******************************************************************************************************************
     * Retorna uma lista de objetos Path que representam os arquivos localizados.
     * 
     * @param path Um objeto Path representando o diretorio raiz da pesquisa.
     *
     * 
     * @throws IOException Em caso de erro de IO.
     * 
     * @throws NotDirectoryException Se <b><i>path</i></b> nao corresponder a um diretorio.
     * 
     * @throws SecurityException Se o S.O., de acordo com a politica de segurança do sistema, nao permitir acesso
     * a algum diretorio ou arquivo.
     * 
     * @return Uma lista de objetos <b><i>Path</i></b> com os arquivos localizados.
     ******************************************************************************************************************/
    public LinkedList<Path> search(final Path path) 
        throws IOException, NotDirectoryException, SecurityException {
        
       pathList = new LinkedList<>();
       
       recursiveSearch(path, Mode.PATH);
       
       return (LinkedList)pathList;
       
    }//search
    
    /*******************************************************************************************************************
     * Retorna uma lista de objetos String com os caminhos absolutos dos arquivos localizados
     * 
     * @param pathName Uma String com o caminho do diretorio raiz da pesquisa.
     * 
     * @throws IOException Em caso de erro de IO.
     * 
     * @throws NotDirectoryException Se <b><i>pathName</i></b> nao corresponder a um diretorio.
     * 
     * @throws SecurityException Se o S.O., de acordo com a politica de segurança do sistema, nao permitir acesso
     * a algum diretorio ou arquivo.
     * 
     * @return Uma lista de objetos <b><i>String</i></b> com os caminhos dos arquivos localizados.
     ******************************************************************************************************************/
    public LinkedList<String> search(final String pathName) 
        throws IOException, NotDirectoryException, SecurityException {
        
       pathNameList = new LinkedList<>();
       
       recursiveSearch(Paths.get(pathName), Mode.PATHNAME);
       
       return (LinkedList)pathNameList;
       
    }//search  
    
    /*******************************************************************************************************************
     * Retorna uma lista de objetos File que representam os arquivos localizados.
     * 
     * @param dir Um objeto File representando o diretorio raiz da pesquisa.
     * 
     * @throws IOException Em caso de erro de IO.
     * 
     * @throws NotDirectoryException Se <b><i>dir</i></b> nao corresponder a um diretorio.
     * 
     * @throws SecurityException Se o S.O., de acordo com a politica de segurança do sistema, nao permitir acesso
     * a algum diretorio ou arquivo.
     * 
     * @return Uma lista de objetos <b><i>File</i></b> com os arquivos localizados.
     ******************************************************************************************************************/
    public LinkedList<File> search(final File dir) 
        throws IOException, NotDirectoryException, SecurityException {
        
       fileList = new LinkedList<>();
       
       recursiveSearch(dir.toPath(), Mode.FILE);
       
       return (LinkedList)fileList;
       
    }//search
    
    
/*======================================================================================================================
 * Classe privada implementa o filtro padrao.
======================================================================================================================*/
private static class FilterFiles implements DirectoryStream.Filter<Path> {
    
    private final String regex;
    private final boolean searchSubdirs; 
    
    public FilterFiles(final String r, final boolean s) throws PatternSyntaxException {
        
        if ("teste".matches(r));//Lança exceçao no caso de regex invalida
        
        regex = r;
        searchSubdirs = s;
        
    }
     
    @Override
    public boolean accept(final Path file) throws IOException {
         
        try {
            
            if (Files.isDirectory(file)) return searchSubdirs; 
            
        }
        catch (SecurityException e) {
            
            return false;
        }
         
         if (regex == null) return true;
         
         return file.getFileName().toString().matches(regex);
    }
     
}//classe FilterFiles
        

}//classe SearchFolders
