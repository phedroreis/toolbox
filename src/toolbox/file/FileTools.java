package toolbox.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Metodos estaticos para manipulacao de arquivos e diretorios.
 * 
 * @author Pedro Reis
 * @since 1.0
 * @version 1.0 - 26 de agosto de 2024
 */
public final class FileTools {
    
    private static String msg$1;
    
    static {
        
        try {
            
            ResourceBundle rb = 
                ResourceBundle.getBundle("toolbox.properties.FileTools", toolbox.locale.Localization.getLocale());
            msg$1 = rb.getString("msg$1");
 
        
        } catch (NullPointerException | MissingResourceException | ClassCastException e) {
            
            msg$1 = "Unable to create dirs :";
  
        }    
        
    }//static block    

    /**
     * Cria um diretorio se este nao existir, criando, se necessario, todos os diretorios pais no caminho que
     * tambem ainda nao existam.
     * 
     * @param path O diretorio.
     * 
     * @throws FileNotFoundException No caso de nao ser possivel criar o diretorio.
     */
    public static void createDirsIfNotExists(final File path) throws FileNotFoundException {
 
        if (!path.exists()) { 
            
            boolean sucess = path.mkdirs(); 
        
            if (!sucess) throw new FileNotFoundException(msg$1 + path.getAbsolutePath());
        }
        
    }//createDirsIfNotExists

    /**
     * Cria um diretorio se este nao existir, criando, se necessario, todos os diretorios pais no caminho que
     * tambem ainda nao existam.
     * 
     * @param path O diretorio.
     * 
     * @throws FileNotFoundException No caso de nao ser possivel criar o diretorio.
     */
    public static void createDirsIfNotExists(final String path) throws FileNotFoundException {
 
        createDirsIfNotExists(new File(path));
        
    }//createDirsIfNotExists
    
    /**
     * Deleta todos os diretorios no caminho ate encontrar um diretorio pai nao vazio.
     * 
     * @param dir O diretorio e o caminho do diretorio.
     * 
     * @throws SecurityException Se o Security Manager do sistema impedir o acesso ao diretorio.
     */
    public static void deleteDirsIfEmpty(File dir) throws SecurityException {
        
        while (dir != null) {
            
            if (!dir.delete()) return;
            dir = dir.getParentFile();
            
        } 
        
    }//deleteDirsIfEmpty    
 
    /**
     * Deleta todos os diretorios no caminho ate encontrar um diretorio pai nao vazio.
     * 
     * @param dir O diretorio e o caminho do diretorio.
     * 
     * @throws SecurityException Se o Security Manager do sistema impedir o acesso ao diretorio.
     */
    public static void deleteDirsIfEmpty(final String dir) throws SecurityException {
        
        deleteDirsIfEmpty(new File(dir));
        
    }//deleteDirsIfEmpty  
    
}//classe FileTools
