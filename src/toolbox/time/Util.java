package toolbox.time;

import java.util.Calendar;
import java.util.Date;

/**
 * Metodos estaticos uteis para manipulacao de data e hora.
 * 
 * @author Pedro Reis
 * @since 1.0
 * @version 1.0 - 24 de agosto de 2024
 */
public class Util {
    /**
     * Converte o formato do atributo datetime de uma tag time para {@link java.util.Calendar Calendar}.
     * 
     * 
     * @param datetime Data e hora no formato do atributo datetime da tag HTML time. Os decimos e
     * centesimos de segundo nesse argumento serao ignorados na conversao.
     * 
     * @param gmt O fuso horario (em multiplo de hora, apenas). Pode ser positivo ou negativo.
     * 
     * @return Um objeto Calendar com a data e hora fornecida ao metodo ajustada para o fuso
     * horario passado ao metodo.
     */
    public static Calendar htmlDateTimeToCalendar(final String datetime, final int gmt) {
        
        int gmtInMilliseconds = gmt * 3600000;
        
        Calendar c = Calendar.getInstance();
         
        c.set(Calendar.YEAR, Integer.parseInt(datetime.substring(0, 4)));
        
        c.set(Calendar.MONTH, Integer.parseInt(datetime.substring(5, 7)));
        
        c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(datetime.substring(8, 10)));
        
        c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(datetime.substring(11, 13)));
        
        c.set(Calendar.MINUTE, Integer.parseInt(datetime.substring(14, 16)));
        
        c.set(Calendar.SECOND, Integer.parseInt(datetime.substring(17, 19)));   
        
        c.set(Calendar.ZONE_OFFSET, gmtInMilliseconds);
        
        c.add(Calendar.MILLISECOND, gmtInMilliseconds); 
        
        return c;
        
    }//htmlDateTimeToCalendar
    
    /*******************************************************************************************************************
     * Imprime a data passada no formato dd/mm/aaaa.
     * 
     * @param date A data-hora.
     * 
     * @return A data extraida de um objeto Date no formato dd/mm/aaaa
     ******************************************************************************************************************/
    public static String dateFormat(final Date date) {
        
        return String.format("%1$td/%1$tm/%1$tY", date);
        
    }//dateFormat
    
    /*******************************************************************************************************************
     * Imprime a hora passada no formato hh:mm:ss:ms.
     * 
     * @param date A data-hora.
     * 
     * @return A data extraida de um objeto Date no formato hh:mm:ss:ms
     ******************************************************************************************************************/
    public static String timeFormat(final Date date) {
        
        return String.format("%1$tHh:%1$tMm:%1$tSs:%1$tLms", date);
        
    }//timeFormat
    
    /*******************************************************************************************************************
     * Retorna a data e a hora no momento em que este metodo foi chamado no formato dd/mm/aaaa hh:mm:ss:ms.
     * 
     * @return Data e a hora no momento em que este metodo foi chamado no formato dd/mm/aaaa hh:mm:ss:ms.
     ******************************************************************************************************************/
    public static String rightNowFormat() {
        
        Calendar c = Calendar.getInstance();
        Date now = c.getTime();
        return dateFormat(now) + " " + timeFormat(now);
        
    }//rightNowFormat  

}//classe Util
