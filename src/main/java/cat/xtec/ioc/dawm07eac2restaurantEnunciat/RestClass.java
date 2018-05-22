package cat.xtec.ioc.dawm07eac2restaurantEnunciat;

import java.util.ArrayList;

/**
 *
 * @author Alex
 */
public class RestClass {
    private String name;
    private ArrayList<Integer> valoracio; 
    private Double mitjana;

    RestClass() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Double getMitjana() {
        return mitjana;
    }

    public String getName() {
        return name;
    }

    public Integer getValoracio() {
        return (int) valoracio.get(valoracio.size()-1);
    }
    
    public ArrayList<Integer>  getTotesValoracions() {
        return  valoracio;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setValoracio(Integer valoracio) {
        this.valoracio.add(valoracio);
        this.mitjana = recalculateMedian();
    }
    
    public void eliminaUltimaValoracio(){
        this.valoracio.remove(this.valoracio.size() - 1);
        this.mitjana = recalculateMedian();
    }
    
    public RestClass(String name, Integer valoracio) {
        this.valoracio = new ArrayList();
        this.name = name;
        this.valoracio.add(valoracio);
        this.mitjana = (double) valoracio;
    }
    
    private Double recalculateMedian (){
        int sum = 0;
        if (valoracio.size() <=1){
            return -1.0;
        }
        for (int rate: valoracio) {
              sum += rate;
        }
        return (double) sum/valoracio.size();
    }
    
}
