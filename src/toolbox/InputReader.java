package toolbox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

/******************************************************************************
 * Classe responsável por processar entradas de usuário pelo terminal.
 * 
 * <p>Um objeto desta classe exibe um prompt para entrada de dados no terminal
 * e valida a entrada por meio de um objeto InputParser passado ao seu 
 * construtor.</p>
 * 
 * @see toolbox.InputParser
 * @since 1.0 - 2 de janeiro de 2024
 * @version 1.0
 * @author Pedro Reis
 ******************************************************************************/
public final class InputReader {
    
private String label;
private String enterOptionLabel;
private String defaultOption;
private InputParser parser;
private final BufferedReader inputReader;
private final PrintStream console;

/*-----------------------------------------------------------------------------

------------------------------------------------------------------------------*/
/**
 * Construtor.
 * 
 * @param charset O encoding que o terminal irá usar para ler os dados.
 * 
 * <p>Este encoding é definido pelo sistema. As opções possíveis são:</p>
 * 
 *<ul>
 *<li>iso-8859-1
 *<li>us-ascii
 *<li>utf16
 *<li>utf_16be
 *<li>utf_16le
 *<li>utf8
 *</ul> 
 * 
 * @throws UnsupportedEncodingException Se o código do charset nao for um dos
 * listados acima.
 */
public InputReader(final String charset) throws UnsupportedEncodingException {
    
    console = new PrintStream(System.out, true, charset);
    
    inputReader = new BufferedReader(new InputStreamReader(System.in, charset));
   
}//construtor

/*-----------------------------------------------------------------------------

------------------------------------------------------------------------------*/
/**
 * Configura o prompt de entrada de dados.
 * 
 * @param label Uma mensagem especifica que tipo de dado é esperado.
 * 
 * @param enterOptionLabel Uma mensagem para informar ao usuário qual o valor
 * default para esta entrada.
 * 
 * @param defaultOption O valor default que deve ser retornado se o usuario 
 * teclar ENTER ou fornecer uma entrada em branco.
 * 
 * @param parser Um objeto de uma classe que estenda a classe abstrata
 * {@link toolbox.InputParser InputParser} e que sera responsável por validar
 * esta entrada.
 */
public void setPrompt(
    final String label,
    final String enterOptionLabel,
    final String defaultOption,
    final InputParser parser
){
    
    this.label = label;
    this.enterOptionLabel = enterOptionLabel;
    this.defaultOption = defaultOption;
    this.parser = parser;      
  
}//setPrompt

/*-----------------------------------------------------------------------------

------------------------------------------------------------------------------*/
/**
 * O metodo apresenta a mensagem adequada especificando o tipo de entrada, le a
 * entrada e retorna um valor validado.
 * 
 * @return Uma entrada validada
 * 
 * @throws java.io.IOException O método usa um objeto tipo 
 * {@link java.io.BufferedReader BufferedReader} para ler dos dados no terminal.
 * 
 * <p>Um objeto deste tipo poderia lançar uma IOException ao ler um arquivo,
 * por exemplo. Por esta razão o método é obrigado a declarar este tipo de 
 * exceção. Muito embora, neste caso, não vá ocorrer, uma vez que 
 * o objeto BufferedReader estará lendo dados a partir de um terminal.</p>
 */
public String readInput() throws IOException {
    
    boolean err;
    String input = null;
    
    do {
        err = false;
        
        console.println('\n' + label + ':');
        console.print("[ENTER = " + enterOptionLabel + "] >");
        
        try {           
      
            input = inputReader.readLine(); 
            
            if (input.isBlank()) return defaultOption;
            
            input = parser.parse(input);
        }
        catch (IllegalArgumentException e) {
            
            console.println("\n" + e.getMessage());
            err = true;             
        }
        
    } while (err);
    
    return input;
    
}//readInput   
    
}//classe InputReader

