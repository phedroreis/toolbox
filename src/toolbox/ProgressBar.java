package toolbox;

/**
 * Implementa uma barra de progresso simples no terminal.
 * 
 * <p>Devido ao fato de que uma aplicação Java não tem controle sobre o 
 * posicionamento do cursor em terminais de sistemas Windows, implementar uma
 * barra de progresso está sujeito a algumas limitações. A principal delas é que
 * enquanto um objeto desta classe estiver escrevendo na barra de progresso, 
 * <b>o programa não poderá efetuar nenhuma outra saída de texto para o terminal.
 * </b></p>
 * 
 * @since 1.0 - 14 de janeiro de 2024
 * @version 1.0
 * @author Pedro Reis
 */
public final class ProgressBar {
    
    private final int total;
    private final int barLength;
    private final int stepsPerDot;
    private int countSteps;
    
    /**
     * Construtor.
     * 
     * @param total O total de passos que serão executados para a realização do
     * processo.
     * 
     * @param barLength A largura máxima (em caracteres), que a barra poderá ter. 
     * 
     * <p>Esta largura poderá ser ajustada para um tamanho menor</p>
     * 
     * @throws IllegalArgumentException Se o total passado for menor que 1.
     */
    /*-------------------------------------------------------------------------
         
    --------------------------------------------------------------------------*/     
    public ProgressBar(final int total, int barLength) 
        throws IllegalArgumentException {
        
        if (total < 1) 
            throw new IllegalArgumentException("Largura da barra < 1");
        
        barLength++;
        
        int steps;  

        do {

        } while (
            (total % --barLength) >= 
            (steps = total / barLength)
        ); 
        
        this.total = total;
        this.barLength = barLength;
        stepsPerDot = steps;
        countSteps = 0;
        
    }//construtor
    
    /**
     * Exibe a barra no terminal.
     */
    /*-------------------------------------------------------------------------
         
    --------------------------------------------------------------------------*/     
    public void showBar() {
        
        System.out.print("\n0%|" + " ".repeat(barLength) + "|100%\n   ");
        
    }//showBar
    
    /**
     * Deve ser chamado a cada passo executado do processo.
     */
    /*-------------------------------------------------------------------------
          Retorna uma String contendo o caractere c repetido length vezes   
    --------------------------------------------------------------------------*/ 
    public void increment() {
        
        if (++countSteps % stepsPerDot == 0) System.out.print(".");  
        
    }//increment
    
    /**
     * Exemplo de uso.
     * 
     * <p>Ilustra como a barra poderia exibir o progresso de um processo que se
     * daria em 50 passos, cada um levando um segundo para ser executado
     * dentro de um loop.</p>
     * 
     * @param args Não usado.
     */
    @SuppressWarnings("empty-statement")
    public static void main(String[] args) {
        
        int steps = 50;
        
        //Cria a barra de progresso
        ProgressBar progressBar = new ProgressBar(steps, 40);
        
        //Exibe a barra vazia no terminal
        progressBar.showBar();
        
        long startTime = System.currentTimeMillis();        
        
        //Loop que executaria os passos do processo
        for (int i = 0; i < steps; i++) {
            
            //Simula a execução de um passo do processo
            while ((System.currentTimeMillis() - startTime) < 100);
            startTime = System.currentTimeMillis();
            
            //Informa para a barra de progresso que um passo foi executado
            progressBar.increment();
            
        }
        
        System.out.println();
         
    }//main
    
}//classe ProgressBar
