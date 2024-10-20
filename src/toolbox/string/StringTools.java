package toolbox.string;

import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.MissingResourceException;

/*******************************************************************************
* Métodos estáticos para manipulação de strings.
*
* @since 1.0 - 14 de janeiro de 2024
* @version 1.0
* @author Pedro Reis
*******************************************************************************/
public final class StringTools {
    
    public static final String NEWLINE;
    
    private static String msg$1;
    private static String msg$2;
    private static String msg$3;   
    
    static {
        
       NEWLINE = System.lineSeparator();
        
        try {
            ResourceBundle rb = 
                ResourceBundle.getBundle("toolbox.properties.StringTools", toolbox.locale.Localization.getLocale());
            msg$1 = rb.getString("msg$1");//      
            msg$2 = rb.getString("msg$2");//
            msg$3 = rb.getString("msg$3");//            
            
       } catch (NullPointerException | MissingResourceException | ClassCastException e) {
           
            // Opcaoes default caso falhe a chamada a rb.getString() [Locale en_US : default]
            msg$1 = "Negative length";        
            msg$2 = "String null";
            msg$3 = "Invalid utf8 code :";          
       }        
    
    }
    
    /***************************************************************************
    * Retorna uma string contendo o caractere <b><i>c</i></b> repetido 
    * <b><i>length</i></b> vezes. Se <b><i>length</i></b> = 0 retorna string
    * vazia.
    *
    * @param c O caractere.
    *
    * @param length O comprimento da string.
    *
    * @return Um objeto <b><i>String</i></b> contendo o caractere 
    * <b><i>c</i></b> repetido <b><i>length</i></b> vezes.
    * 
    * @throws IllegalArgumentException Se <b><i>length</i></b>
    ***************************************************************************/
    public static String repeatChar(final char c, final int length) 
        throws IllegalArgumentException {    
            
        if (length < 0) throw new IllegalArgumentException(msg$1);    
        
        char[] ch = new char[length];
        
        Arrays.fill(ch, c);
        
        return new String(ch);       
        
    }//repeatChar 
    
    /***************************************************************************
    * Retorna uma string contendo a string <b><i>s</i></b> repetida 
    * <b><i>length</i></b> vezes.
    * 
    * <p><b>OBSERVACAO: ESTE METODO SO DEVE SER USADO EM FONTES QUE PRETENDAM
    * COMPILAR COM JDK ANTERIOR AO 11. VERSAO 11 da CLASSE STRING JA POSSUI 
    * METODO ESTATICO COM FUNCIONALIDADE EQUIVALENTE.</b></p>
    * 
    * @see <a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/String.html#repeat(int)">repeat</a>
    *
    * @param s A String
    *
    * @param length O numero de repetiçoes.
    *
    * @return Um objeto <b><i>String</i></b> contendo a string
    * <b><i>s</i></b> repetida <b><i>length</i></b> vezes.
    * 
    * @throws IllegalArgumentException Se <b><i>length</i></b>
    * 
    * @throws NullPointerException Se <b><i>s</i></b> for <code>null</code>.
    ***************************************************************************/
    public static String repeatString(final String s, final int length) 
        throws IllegalArgumentException, NullPointerException {   
        
        if (length < 0) throw new IllegalArgumentException(msg$1);
        
        if (s == null) throw new NullPointerException(msg$2);
        
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < length; i++) sb.append(s);
        
        return sb.toString();       
        
    }//repeatString  
    
    /***************************************************************************
    * Substitui, em um objeto <b><i>StringBuilder</i></b>, todas as ocorrencias
    * de uma determinada string por outra.
    * 
    * @param sb O objeto <b><i>StringBuilder</i></b>.
    *
    * @param target A string a ser substituida.
    * 
    * @param replacement A string de substituicao.
    ***************************************************************************/    
    public static void replace(
        final StringBuilder sb,
        final String target, 
        final String replacement
    ) {   
        
        int p; int length = target.length();
        
        while ((p = sb.indexOf(target)) != -1) 
            sb.replace(p, p + length, replacement); 
            
    }//replace
    
    /**
     * Padroniza (normaliza) strings para que possam ser comparadas lexigraficamente com outras 
     * strings tambem normalizadas por este metodo. 
     * 
     * <p>A normalizacao evita que caracteres com acento
     * sejam considerados diferentes do mesmo caractere sem acento. Ou que maiusculas sejam diferentes
     * de minusculas. 
     * 
     * <p>Ainda, caracteres que nao sejam letras ou digitos serao excluidos da string 
     * normalizada para nao interferirem na comparacao lexigrofica, que deve levar em consideracao apenas
     * a ordem lexigrafica de letras do alfabeto e dos digitos de 0 a 9.
     * 
     * @param str A String a ser normalizada.
     * 
     * @return A string normalizada.
     */
    public static String normalizeToCompare(final String str) {
                
        char[] charArray = str.toLowerCase().toCharArray();

        for (int i = 0; i < charArray.length; i++) {

            switch(charArray[i]) {
                
                case '\u00e0':
                case '\u00e1':
                case '\u00e2':
                case '\u00e3':
                case '\u00e4':
                    charArray[i] = 'a'; break;
                case '\u00e8':
                case '\u00e9':
                case '\u00ea':
                case '\u00eb':
                case '\u0113':
                case '\u0115':
                    charArray[i] = 'e'; break;
                case '\u00ec':
                case '\u00ed':
                case '\u00ee':
                case '\u00ef':
                case '\u0129':
                case '\u012b':
                    charArray[i] = 'i'; break;
                case '\u00f2':
                case '\u00f3':
                case '\u00f4':
                case '\u00f5':
                case '\u00f6':
                    charArray[i] = 'o'; break;
                case '\u00f9':
                case '\u00fa':
                case '\u00fb':
                case '\u00fc':
                case '\u0169':
                case '\u016b':
                    charArray[i] = 'u'; break;
                case '\u00e7':
                    charArray[i] ='c'; break;                    
                case '\u00f1':
                    charArray[i] = 'n';
            }
            
        }//for
 
        return new String(charArray).replaceAll("\\W", "");

    }//normalizeToCompare 
    
    /**
     * Compara lexigraficamente duas strings normalizadas pelo metodo 
     * {@link normalizeToCompare(String) normalizeToCompare}
     * 
     * @param str1 Uma String.
     * @param str2 Outra String.
     * 
     * @return 0 se forem lexigraficamente equivalentes. Um valor negativo se <i><b>str1</b></i>
     * for lexigraficamente menor que <i><b>str2</b></i> ou um valor positivo se for o contrario.
     */
    public static int compare(final String str1, final String str2) {
        
        return normalizeToCompare(str1).compareTo(normalizeToCompare(str2)); 
        
    }//compare
    
    /**
     * Converte um cdodigo UTF8 em hexa para seu correspondente codepoint em hexa.
     * 
     * @param utf8 Codigo UTF8 em Hexadecimal SEM prefixo indicador da base numerica 16.
     * 
     * @return Codigo unicode em Hexadecimal sem prefixo ou formatacao.
     * 
     * @throws NumberFormatException Se o argumento nao tiver representacao numerica ou exceder
     * o limite de representacao do tipo <code>long</code>
     * 
     * @throws IllegalArgumentException Se nao representar um codigo UTF8 valido.
     */
    public static String utf8ToUnicode(final String utf8)
        throws IllegalArgumentException, NumberFormatException  { 

        long decimalValue = Long.parseLong(utf8, 16); 
        
        if (decimalValue < 0) throw new IllegalArgumentException(msg$3 + utf8);//Codigo utf8 < 0         
        
        long unicode;
        
        if (decimalValue < 256) {
            
            if ((decimalValue & 128) != 0) throw new IllegalArgumentException(msg$3 + utf8);
            
            unicode = decimalValue;
        
        } else if (decimalValue < 65536) {
            
            if ((decimalValue & 57536) != 49280) throw new IllegalArgumentException(msg$3 + utf8);            
            
            unicode = decimalValue & 7999; 
            
            unicode = 
                ((unicode & 7936) >> 2) + 
                (unicode & 63); 
            
        } else if (decimalValue < 16777216) {
            
            if ((decimalValue & 15777984) != 14712960) throw new IllegalArgumentException(msg$3 + utf8);             
            
            unicode = decimalValue & 999231;
            
            unicode = 
                ((unicode & 983040) >> 4) + 
                ((unicode & 16128) >> 2) + 
                (unicode & 63);
            
        } else if (decimalValue < 4294967296l) {
            
            if ((decimalValue & 4173381824l) != 4034953344l) throw new IllegalArgumentException(msg$3 + utf8); 
            
            unicode = decimalValue & 121585471;
            
            unicode = 
                ((unicode & 117440512) >> 6) + 
                ((unicode & 4128768) >> 4) + 
                ((unicode & 16128) >> 2) + 
                (unicode & 63);  
            
        } else throw new IllegalArgumentException(msg$3 + utf8);//Codigo utf8 excedeu 4 bytes 
        
        //Maior possivel codepoint = 1.114.111
        if (unicode > 1114111) throw new IllegalArgumentException(msg$3 + utf8);        
        
        return Long.toHexString(unicode);  
        
    }//utf8ToUnicode 
    
    /**
     * Converte codepoint em hexa para correspondente UTF8 em hexa.
     * 
     * @param unicode O codepoint em hexadecimal SEM o prefixo indicador da base numerica 16.
     * 
     * @return Codigo UTF8 em hexa sem formatacao.
     * 
     * @throws NumberFormatException Se o argumento nao tiver representacao numerica ou exceder
     * o limite de representacao do tipo <code>long</code>
     * 
     * @throws IllegalArgumentException Se o argumento passado nao representar um codepoint valido
     * pertencente ao intervalo [00000, 10FFFF].
     */
    public static String unicodeToUtf8(final String unicode) 
        throws IllegalArgumentException, NumberFormatException {
        
        long decimalValue = Long.parseLong(unicode, 16); 
        
        //Maior possivel codepoint = 1.114.111
        if (decimalValue > 1114111 || decimalValue < 0) throw new IllegalArgumentException();         
        
        long utf8;

        if (decimalValue < 128) {
            
            utf8 = decimalValue;
            
        } else if (decimalValue < 2048) {
            
            utf8 = 
                49280 + 
                (decimalValue & 63) +
                ((decimalValue & 1984) << 2);  
            
        }  else if (decimalValue < 65536) {
           
           utf8 =
               14712960 + 
               (decimalValue & 63) +
               ((decimalValue & 4032) << 2) +
               ((decimalValue & 61440) << 4);
            
        }  else {

           utf8 = 
               4034953344l +
               (decimalValue & 63) +
               ((decimalValue & 4032) << 2) + 
               ((decimalValue & 258048) << 4) +
               ((decimalValue & 1835008) << 6);
            
        } 
        
        return Long.toHexString(utf8);
        
    }//unicodeToUtf8    
    
}//classe StringTools
