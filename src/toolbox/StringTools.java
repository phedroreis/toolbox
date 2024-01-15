package toolbox;

/**
 * Métodos estáticos para manipulação de Strings.
 * 
 * @since 1.0 - 14 de janeiro de 2024
 * @version 1.0
 * @author Pedro Reis
 */
public final class StringTools {
    
    /**
     * Retorna uma String contendo o caractere c repetido length vezes.
     * 
     * @param c O caractere.
     * 
     * @param length O comprimento da String.
     * 
     * @return uma String contendo o caractere c repetido length vezes.
     */
    /*-------------------------------------------------------------------------
            
    --------------------------------------------------------------------------*/  
    public static String repeatChar(final char c, final int length) {
        
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < length; i++) s.append(c);
        return s.toString();
        
    }//repeatChar    
    
}//classe StringTools
