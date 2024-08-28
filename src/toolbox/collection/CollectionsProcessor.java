package toolbox.collection;

import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedList;
import java.util.TreeMap;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/***********************************************************************************************************************
 * Metodos funcionais que processam objetos Collection.
 * 
 * @author Pedro Reis
 * 
 * @since 1.0 - 18 de julho de 2024
 * 
 * @version 1.0
 * 
 * @param <T> Representa o tipo de elemento contido na Collection que sera processada
 * 
 * @param <R> Representa o tipo dos objetos que serao retornados pelos metodos desta classe ao 
 * processarem a Collection. Eventualmente, se for desejado que o retorno seja um objeto do mesmo tipo
 * daqueles contidos na propria Collection passado ao construtor, entao o tipo T sera o de retorno
 * do metodo.
 **********************************************************************************************************************/
public class CollectionsProcessor<T,R> {
    
    private final Collection<T> collection;
    
    /*******************************************************************************************************************
     * Construtor da classe. Recebe o objeto {@link java.util.Collection Collection} que pode ser 
     * processado por metodos da classe.
     * 
     * @param c Qualquer Collection.
     ******************************************************************************************************************/
    public CollectionsProcessor(Collection<T> c) {
        
        collection = c;
        
    }//construtor
    
    /*******************************************************************************************************************
     * Obtem um objeto como resultado do processamento de todos os objetos contidos no Collection.
     * 
     * <p>Aplica a funcao <i><b>apply()</b></i> que recebe como operandos um objeto do Collection e outro objeto 
     * "result" de mesmo tipo parametrizado T (inicializado como <code>null</code> na execucao do metodo), e atribui
     * ao proprio "result" o retorno desta funcao.</p>
     * 
     * <p>A funcao <i><b>apply()</b></i> e aplicada sequencialmente a cada objeto do Collection.</p>
     * 
     * <p>Exemplo:</p>
     * 
     * <p>Obtendo o somatorio de todos os inteiros de um Collection</p>
     * 
     * <pre>
     * <code>
     * Collection&lt;Integer&gt; list = new LinkedList&lt;&gt;();
     * list.add(1); list.add(2); list.add(3);
     * 
     * CollectionProcessor&lt;Integer, Integer&gt; cp = new CollectionProcessor&lt;&gt;(list);
     * 
     * int sum = cp.reduce(
     *   (result, item) -&gt;  {
     *      if (result == null) return item;
     *      return result + item;
     *   }    
     * );
     * 
     * System.out.println("A soma eh: " + sum);//A soma eh: 6
     * </code>
     * </pre>
     * 
     * @param reductor Implementa a funcao <i><b>apply()</b></i> da interface {@link java.util.function.BinaryOperator BinaryOperator}. 
     * 
     * @return T Um objeto do tipo parametrizado T.
     ******************************************************************************************************************/
    public T reduce(BinaryOperator<T> reductor) {
        
        T result = null;
        
        for (T item : collection) result = reductor.apply(result, item);
        
        return result;
        
    }//reduce
    
    /*******************************************************************************************************************
     * Obtem um objeto como resultado do processamento de todos os objetos contidos no Collection.
     * 
     * <p>Aplica a funcao <i><b>apply()</b></i> que recebe como operandos um objeto do Collection e outro objeto 
     * "result" de mesmo tipo parametrizado T e atribui ao proprio "result" o retorno desta funcao.</p>
     * 
     * <p>"result" e inicializado com a funcao <i><b>get()</b></i> passada ao metodo no parametro <i><b>initializer</b></i>.</p>
     * 
     * <p>A funcao <i><b>apply()</b></i> e aplicada sequencialmente a cada objeto do Collection.</p>
     * 
     * <p>Exemplo:</p>
     * 
     * <p>Obtendo o somatorio de todos os inteiros de um Collection somado ao valor 10</p>
     * 
     * <pre>
     * <code>
        Collection&lt;Integer&gt; list = new LinkedList&lt;&gt;();
        
        list.add(1); list.add(2); list.add(3); list.add(4);
        
        CollectionProcessor&lt;Integer, Integer&gt; cp = new CollectionProcessor(list);
        
        sum = cp.reduce(
            () -&gt; 10 , 
            (result, item) -&gt; result + item
        );        
                
        System.out.println(sum);        
     * </code>
     * </pre>
     * 
     * @param initializer Implementa o metodo <i><b>get()</b></i> da interface {@link java.util.function.Supplier<T> Supplier}
     * para inicilizar a variavel <i><b>result</b></i>.
     * 
     * @param reductor Implementa a funcao <i><b>apply()</b></i> da interface {@link java.util.function.BinaryOperator<T> BinaryOperator}. 
     * 
     * @return T Um objeto do tipo parametrizado T.
     ******************************************************************************************************************/
    public T reduce(Supplier<T> initializer, BinaryOperator<T> reductor) {
         
        T result = initializer.get();
        
        for (T item : collection) result = reductor.apply(result, item);
        
        return result;
        
    }//reduce    
    
    /*******************************************************************************************************************
     * Uma versao sobrecarregada do metodo que permite obter um objeto de tipo diferente do tipo dos objetos 
     * contidos no Collection processado.
     * 
     * @param transform Obtem um objeto de tipo parametrizado R para cada item do Collection.
     * 
     * @param reductor Opera o objeto produzido por transform juntamente com a variavel <i><b>result</b></i>
     * e retorna o valor obtido para o proprio <i><b>result</b></i>.
     * 
     * @return R Um objeto do tipo parametrizado R.
     ******************************************************************************************************************/
    public R reduce(Function<T,R> transform, BinaryOperator<R> reductor) {
        
        R result = null;
        
        for (T item : collection) {
            
            R r = transform.apply(item);
            
            result = reductor.apply(result, r);
        }
        
        return result;        
        
    }//reduce
    
    /*******************************************************************************************************************
     * Uma versao sobrecarregada do metodo que permite obter um objeto de tipo diferente do tipo dos objetos 
     * contidos no Collection processado.
     * 
     * <p>"result" e inicializado com a funcao <i><b>get()</b></i> passada ao metodo no parametro <i><b>initializer</b></i>.</p>
     * 
     * @param initializer
     * 
     * @param transform Obtem um objeto de tipo parametrizado R para cada item do Collection.
     * 
     * @param reductor Opera o objeto produzido por transform juntamente com a variavel <i><b>result</b></i>
     * e retorna o valor obtido para o proprio <i><b>result</b></i>.
     * 
     * @return R Um objeto do tipo parametrizado R.
     ******************************************************************************************************************/
    public R reduce(Supplier<R> initializer, Function<T,R> transform, BinaryOperator<R> reductor) {
        
        R result = initializer.get();
        
        for (T item : collection) {
            
            R r = transform.apply(item);
            
            result = reductor.apply(result, r);             
        }

        return result;        
        
    }//reduce  
    
    /*******************************************************************************************************************
     * Obtem um subconjunto de Collection apenas com itens de satisfacam a condicao implementada pela funcao 
     * passada pelo parametro <i><b>p</b></i>.
     *
     * @param collectionObject Uma referencia inicializada para alguma classe que implemente Collection.
     * Esse objeto sera retornado pelo metodo com o subconjunto de objetos filtrados.
     * 
     * @param p Implementa o teste booleano com a condicao de pertinencia.
     * 
     * @return R Um subconjunto dos objetos selecionados pelo filtro p.
     ******************************************************************************************************************/
    public R filter(Supplier<R> collectionObject, Predicate<T> p) {
        
        R set = collectionObject.get();
        
        for (T item : collection) if (p.test(item)) ((java.util.Collection)set).add(item);
        
        return set;
        
    }//filter
    
    /*******************************************************************************************************************
     * Executa a funcao <i><b>accept()</b></i> para cada objeto da colecao.
     * 
     * @param c A funcao que sera executada para todo objeto da colecao.
     ******************************************************************************************************************/
    public void forEach(Consumer<T> c) {
       
        for (T item : collection) c.accept(item);
        
    }//forEach
    
    /*******************************************************************************************************************
     * Produz um mapa associando cada elemento do tipo T da coleçao a algum objeto de tipo R.
     * 
     * @param f A funcao de mapeamento.
     * 
     * @return Um objeto {@link java.util.TreeMap TreeMap}.
     ******************************************************************************************************************/
    public TreeMap<T,R> map(Function<T,R> f) {
        
        TreeMap<T,R> treeMap = new TreeMap<>();
        
        for (T key : collection) treeMap.put(key, f.apply(key));

        return treeMap;
        
    }//map
    
    /*******************************************************************************************************************
     * Representaçao textual do objeto.
     * 
     * @return Uma lista com os elementos da <i><b>Collection</b></i>
     ******************************************************************************************************************/
    @Override
    public String toString() {
        
        return collection.toString();
        
    }//toString
    
  
    public static void main(String[] args) {
        
        Collection<Integer> list = new LinkedList<>();
        
        list.add(1); list.add(2); list.add(3); list.add(4);
        
        CollectionsProcessor<Integer, Integer> cp = new CollectionsProcessor(list);
        
        System.out.print("Imprimindo os itens do Collection com forEach: ");
        
        cp.forEach((item) -> System.out.print(item + " ")); System.out.println();
        
        System.out.print("Obtendo o somatorio dos itens: ");
        
        int sum = cp.reduce(
            (result, item) ->  {
                if (result == null) return item;
                return result + item;
            }    
        );
      
        System.out.println(sum);
        
        System.out.print("Obtendo o somatorio dos itens + 10: ");
        
        sum = cp.reduce(
            () -> 10 , 
            (result, item) -> result + item
        );        
                
        System.out.println(sum);
        
        System.out.println("Mapeando cada item com seu dobro: ");
        
        TreeMap<Integer, Integer> map = cp.map((key) -> (2 * key)); 
        
        for (int i : map.keySet()) System.out.println(i + " : " + map.get(i));  
        
        System.out.println(cp);
        
        Collection<Calendar> clist = new LinkedList<>();
        
        Calendar d = Calendar.getInstance(); d.set(Calendar.YEAR, 0);    
        Calendar d1 = Calendar.getInstance(); d1.set(Calendar.YEAR, 2950);
        Calendar d2 = Calendar.getInstance(); d2.set(Calendar.YEAR, 2050);
        Calendar d3 = Calendar.getInstance(); d3.set(Calendar.YEAR, 1940);
        Calendar d4 = Calendar.getInstance(); d4.set(Calendar.YEAR, 1999); 
        
        clist.add(d1); clist.add(d2); clist.add(d3); clist.add(d4);
        
        CollectionsProcessor<Calendar, Calendar> cc = new CollectionsProcessor(clist); 
        
        Calendar max = 
            cc.reduce(
                () -> d,
                (result, item) -> { 
                    if (item.compareTo(result) > 0) return item;
                    return result;
                }
            );
        
        System.out.println(max);
   
        
    }//main

}//classe CollectionsProcessor
