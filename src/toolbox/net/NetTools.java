package toolbox.net;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;


/***********************************************************************************************************************
 * Metodos utilitarios para operacoes de rede.
 * 
 * @author Pedro Reis
 * @version 1.0 - 14 de agosto de 2024
 * @since 1.0
 **********************************************************************************************************************/
public final class NetTools {
    
    /*******************************************************************************************************************
     * Baixa o arquivo indicado pela URL para o pathname passado ao metodo.
     * 
     * @param url A URL do arquivo.
     * 
     * @param pathname Nome para gravar o arquivo e caminho.
     * 
     * @throws java.io.IOException Em caso de erro de IO
     * 
     * @throws java.net.MalformedURLException Em caso de URL invalida.
     ******************************************************************************************************************/
    public static void downloadUrlToPathname(
        final String url,
        final String pathname
    ) throws IOException, MalformedURLException {
            
        URL theUrl = new URL(url);
        
        try (

            FileOutputStream fos = new FileOutputStream(pathname);

            ReadableByteChannel rbc = Channels.newChannel(theUrl.openStream());

        ) {

            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        }    
        
    }//downloadUrlToPathname()

}//classe NetTools
