package toolbox;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Uma classe para localizar expressões regulares em objetos Strings.
 * 
 * @since 1.0 - 14 de janeiro de 2024
 * @version 1.0
 * @author Pedro Reis
 */
public final class Regex {
    
    private final Pattern pattern;
    
    private Matcher matcher;
    
    private static final String ERR_MSG = "String de pesquisa indefinida.";
    
    /**
     * Construtor da classe. 
     * 
     * @param regex Uma expressão regular sintaticamente válida.
     * 
     * @throws PatternSyntaxException Caso haja erro de sintaxe na regex.
     */
    /*-------------------------------------------------------------------------

    --------------------------------------------------------------------------*/     
    public Regex(final String regex) throws PatternSyntaxException {
        
        matcher = null;
        
        pattern = Pattern.compile(regex);  
        
    }//construtor
    
    /**
     * Define a String na qual a pesquisa será realizada.
     * 
     * @param target A String a ser pesquisada.
     */
    /*-------------------------------------------------------------------------
 
    --------------------------------------------------------------------------*/     
    public void setTarget(final String target) {        
   
        matcher = pattern.matcher(target);  
        
    }//setTarget
    
    /**
     * Retorna true cada vez que o padrao for localizado. 
     * 
     * <p>Na primeira vez que o metodo for chamado, ira localizar a primeira
     * substring que corresponda ao padrao (se houver, caso contrario retorna 
     * false).</p>
     * 
     * <p>E cada chamada subsequene, o método retorna a próxima substring
     * a corresponder com o padrão, se houver. Se não houver substring que
     * corresponda, o método retornará <code>null</code>.</p>
     * 
     * @return A próxima substring localizada, ou <code>null</code> se não mais
     * substrings para localizar.
     */
     /*-------------------------------------------------------------------------
 
    --------------------------------------------------------------------------*/    
    public String find() {
        
        try {
        
            if (matcher.find()) return matcher.group(); else return null;
        
        }
        catch (NullPointerException e) {
            
            throw new NullPointerException(ERR_MSG);
            
        }       
        
    }//find()
    
    /**
     * Obtém um grupo na String localizada pelo padrão.
     * 
     * @param group O índice do grupo.
     * 
     * @return A substring que correspondeu ao grupo, ou String vazia se não
     * foi econtrada correspondência para este grupo.
     */
     /*-------------------------------------------------------------------------
 
    --------------------------------------------------------------------------*/     
    public String group(final int group) {
        
        String s = matcher.group(group);
        if (s == null) return "";
        return s;
        
    }//group
    
    /**
     * Exemplo de uso.
     * 
     * <p>Como um objeto desta classe poderia ser usado para localizar um padrão
     * de números telefônicos em uma String.</p>
     * 
     * @param args Não usado.
     */  
    public static void main(String[] args) {
        
        String target = """
                   (21)99754-8855 e um numero de celular com DDD. 
                   2711-2605 e um numero de telefone fixo sem DDD. 
                   (21)0000-4366 nao e um numero valido.    
                   (22)3366-1234 e telefone fixo com DDD.
                   """;
        
        System.out.println("A String a ser pesquisada:\n\n" + target);
        Regex regex = 
            new Regex("(\\(\\d{2}\\))?((9\\d{4})|[32]\\d{3})-\\d{4}");
        
        regex.setTarget(target);
        
        String match;
        
        System.out.println(
            "Localizando numeros telefonicos e respectivos DDDs...\n"
        );
        
        //localiza todas as ocorrencias do padrao
        while ((match = regex.find()) != null) {
            
            String group1 = regex.group(1);//grupo 1 na string localizada
            String group3 = regex.group(3);//grupo 3 na string localizada
            
            System.out.println(
                match + " -->" +
                (group1.isEmpty() ? "" : (" codigo de area " + group1) + " -") +
                " (telefone " +
                (group3.isEmpty() ? "fixo)" : "celular)")
            ); 
            
        }//while    
    }//main
    
}//classe Regex
