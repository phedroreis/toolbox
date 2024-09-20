package toolbox.file;

import java.awt.Image;
import java.io.InputStream;
import java.net.URL;
import javax.swing.ImageIcon;

/**
 * O objetivo eh que uma implementacao desta interface seja uma "ancora" para um pacote do projeto
 * que contenha recursos (arquivos de imagens, texto, etc...) que serao inseridos no jar do
 * projeto e precisem ser obtidos mesmo estando inseridos em um arquivo jar.
 * 
 * <p>A classe que implemente esta interface deve ser declarada justamente dentro deste pacote
 * onde estao inseridos os recursos e por meio de seus metodos os recursos sao obtidos por outras
 * classes.
 * 
 * <p>Qualquer implementacao desta interface pode (e talvez deva) simplesmente copiar o
 * codigo abaixo:
 * <code>
 * <pre>
public final class Resources implements toolbox.file.ResourcesAnchor {
    
    private static final Class ANCHOR = new Resources().getClass();
    
    public Class getAnchor() {
        
        return ANCHOR;
    }
    
    public URL getResource(final String filename) {
        
        return ANCHOR.getResource(filename);
    }
    
    public ImageIcon getImageIcon(final String filename) {
        
        return new ImageIcon(ANCHOR.getResource(filename));
        
    }
    
    public Image getImage(final String filename) {
        
        return getImageIcon("filename").getImage();
    }
    
    public InputStream getResourceAsStream(final String filename) {
        
        return ANCHOR.getResourceAsStream(filename);
    }

}//classe Resources
</pre>
</code>

 * @author Pedro Reis
 * @since 1.0 - 20 de setembro de 2024
 * @version 1.0 
 */
public interface ResourcesAnchor {
    
    /**
     * Deve retornar um objeto Class de qualquer classe que pertenca ao paconte onde estao
     * os recursos.
     * 
     * @return Um objeto Class de uma classe qualquer no pacote.
     */
    public Class getAnchor();

    /**
     * Retorna uma URL para um arquivo no pacote de recursos do projeto.
     * 
     * @param filename O nome de um arquivo no mesmo pacote da classe que implementa esta
     * interface.
     * 
     * @return A URL do arquivo obtida com <code>getClass().getResources()</code>
     */
    public URL getResource(final String filename);
    
    /**
     * Retorna um objeto ImageIcon relativo a um arquivo de icone inserido no pacote de recursos.
     * 
     * @param filename O nome do arquivo de icone.
     * 
     * @return Objeto ImageIcon do arquivo de icone.
     */
    public ImageIcon getImageIcon(final String filename);

    /**
     * Retorna um objeto Image referente ao arquivo de imagem <i><b>filename</b></i>.
     * 
     * @param filename Um arquivo de imagem no pacote de recursos do projeto.
     * 
     * @return um objeto Image referente ao arquivo de imagem <i><b>filename</b></i>.
     */
    public Image getImage(final String filename);
    
    /**
     * Retorna  um objeto InputStream para o arquivo de texto <i><b>filename</b></i>.
     * 
     * @param filename Um arquivo texto no pacote de recursos do projeto.
     * 
     * @return um objeto InputStream para o arquivo de texto <i><b>filename</b></i>.
     */
    public InputStream getResourceAsStream(final String filename); 

}//interface ResourcesAnchor
