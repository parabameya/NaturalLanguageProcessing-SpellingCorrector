public class Unigram {
     private String word;
     private Double count;
     private Double probability;
     private Double backoff_weight;
     private Double log_probability;
    
    public Unigram (String name) {
        word = name;
    }

     String getName(){
        return word;
    }

     Double getCount(){
        return count;
    }
    
     Double getProbability(){
        return probability;
    }
    
     Double getBackoffWeight(){
        return backoff_weight;
    }
    
     Double getLogProbability(){
        return log_probability;
    }

     void setCount(Double c)
    {
    	count=c;
    }
    
     void setProbability(Double pB){
        probability=pB;
    }
    
     void setBackoffWeight(Double bW){
        backoff_weight=bW;
    }
    
     void setLogProbability(Double lP){
        log_probability=lP;
    }
}