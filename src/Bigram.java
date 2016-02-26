public class Bigram {
	private String word;
	private String history;
	private Double count;
	private Double probability;
	private Double log_probability;
    
    public Bigram (String hs,String name) {
        history=hs;
    	word = name;
    }

     String getWord(){
        return word;
    }

     String getHistory(){
        return history;
    }
    
     Double getCount(){
        return count;
    }
    
     Double getProbability(){
        return probability;
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
    
     void setLogProbability(Double lP){
        log_probability=lP;
    }
}