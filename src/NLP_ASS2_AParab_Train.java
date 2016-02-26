/*@author 
 * NAME:		Ameya Parab
 * UTD ID:		2021166954
 * NET ID:		axp132530
 * SUB: 		NLP HOMEWORK TWO
 * Description: To parse the files and find the unigram and bigram probabilities and
 * 				output the LM file of those values.
 * 
 * Note: 		This Class requires Unigram and Bigram class object.
 * 				Please run the entire application to test the program.
 */


import java.io.*;
import java.util.*;


public class NLP_ASS2_AParab_Train {
    
	@SuppressWarnings("resource")
	public static void main(String [] args) {

        // The name of the file to open.
        String fileName = "training.txt";
        ArrayList<String> words = new ArrayList<String>();
        ArrayList<String> words_backup = new ArrayList<String>();
        String [] line_temp = null; 
        HashMap<String,Unigram> unigram = new HashMap();
        HashMap<String,Bigram> bigram = new HashMap();
//        Double sumProbGT=0.0,sumProbML=0.0;
        String line = null;

        try {
            FileReader fileReader =  new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            //method to get the total words from the file
            while((line = bufferedReader.readLine()) != null) {
            	 line_temp = line.split("[\\s\\t\\n\\.]+");
            	 for (int i= 0; i<line_temp.length; i++) 
            	 {
            		 words.add(line_temp[i]);
            		 words_backup.add(line_temp[i]);
            	 }
            	 line_temp=null;
            }	
        }
        catch(Exception e){}

            //method to find total count of words
            int total_unigram_count=words.size();
            
            System.out.println("Total unigram count ="+total_unigram_count);
            
            //method to find unigram
            for(int i=0;i<words.size();i++)
            {
	            		String temp=words.get(i);
	            		Double value_temp;
	            		Unigram tempUObj;	//object of class Unigram
	            		if(unigram.get(temp)==null)
	            		{
	            			tempUObj=new Unigram(temp);
	            			tempUObj.setCount(1.0);
	            			unigram.put(temp,tempUObj);
	            			tempUObj.setProbability(1.0/total_unigram_count);
	            		}
	            		else
	            		{
	            			tempUObj=unigram.get(temp);		//geting the Object of that word
	            			value_temp=tempUObj.getCount();	//getting the count from that Object  
	            			value_temp++;					//to increase one instance of that variable
	            			tempUObj.setCount(value_temp);	//to set the Object
	            			tempUObj.setProbability(value_temp/total_unigram_count);
	            		}
	            		tempUObj.setLogProbability(Math.log(tempUObj.getProbability())/Math.log(2));
            }
            
            //method to count bigram pairs
            for(int i=0;i<words.size()-1;i++)
            {
            	String hs=words.get(i);		//history
            	String w=words.get(i+1); 	//word
            	Double value_temp;
            	String temp=hs+"\t"+w;		//complete word seperated by tab
        		
        		if(bigram.get(temp)==null)				//no object exists
        		{	
        			Bigram bgObj=new Bigram(hs,w);
        			bgObj.setCount(1.0);
        			bigram.put(temp,bgObj);
        		}
        		else
        		{
        			Bigram tempBObj;				//object of class Unigram
        			tempBObj=bigram.get(temp);		//getting the Object of that word
        			value_temp=tempBObj.getCount();	//getting the count from that Object  
        			value_temp++;					//to increase one instance of that variable
        			tempBObj.setCount(value_temp);	//to set the Object 
        		}
            } 
            //to remove </s> <s>
            String temp="</s>\t<s>";
            bigram.remove(temp);

            //to count the no of single and double counts for Probability of Good-Turing Numerator            
            Double countOfSingle = 0.0,countOfDouble=0.0;
            Double countHistory;
            Set set = bigram.entrySet();
            Iterator i = set.iterator();
            while(i.hasNext()) {
               Map.Entry me = (Map.Entry)i.next();
               Bigram obj=(Bigram) me.getValue();
               //to remove the count of single and double
               if(obj.getCount()==1.0)
            	   countOfSingle++;
               else if(obj.getCount()==2.0)
            	   countOfDouble++;
            }
            Double probGTNumerator=(2*countOfDouble/countOfSingle);
            
            //to set the probabilities of the Bigram i.e pGT & pML
            Set set1 = bigram.entrySet();
            Iterator i1 = set1.iterator();
            while(i1.hasNext()) {
               Map.Entry me = (Map.Entry)i1.next();
               //to get count of history word
               String wrd,hs;
               Double countBg,countHs;
               Bigram objBg; 
               objBg=(Bigram) me.getValue();
               
               wrd=objBg.getWord();
               hs=objBg.getHistory();
               countBg=objBg.getCount();
               
               Unigram objUg;
               objUg=unigram.get(hs);
               countHs=objUg.getCount();  
               
               //if count is 1
               if(countBg==1.0)
               {	//case b
            	   objBg.setProbability(probGTNumerator/countHs);    						// find log values and store as prob in bigram
               }
               else if(countBg>1.0)   //most likely
               {	//case a
            	   objBg.setProbability(countBg/countHs);		
               } 
               objBg.setLogProbability(Math.log(objBg.getProbability())/Math.log(2));  		 //to store log Prob

            }//end of iterator
            
            //to count alpha(h)
            Set set2 = unigram.entrySet();
            Iterator i2 = set2.iterator();
            while(i2.hasNext()) {
               Map.Entry me = (Map.Entry)i2.next();  
               Unigram objUg=(Unigram) me.getValue();
	           Double countHs=objUg.getCount();			//to store history word count for efficient algo
	           String wordUg=objUg.getName();
	           Double pbGT = 0.0;						//for summation of prob GT
	           Double pbML = 0.0;						//for summation of prob ML
	           Double pbWord = 0.0;						//for summation of denominator
	           
               Set set3 = bigram.entrySet();
               Iterator i3 = set3.iterator();
               while(i3.hasNext()&&(countHs!=0)) {    	//check countHs so that the loop is stopped fast
                  Map.Entry me1 = (Map.Entry)i3.next();
                
                  Bigram objBg=(Bigram) me1.getValue();
                  if(objBg.getHistory().equalsIgnoreCase(wordUg)) //if true we need to process further
                  {
                		if(objBg.getCount()==1)
                	  		pbGT+=objBg.getProbability();
                	  	else if(objBg.getCount()>1)
                	  		pbML+=objBg.getProbability();
                		
                		String tempWord=objBg.getWord();			//to find prob of that word
                	  	Unigram tempUg=unigram.get(tempWord);
                	  	pbWord+=tempUg.getProbability();
                	  	
                	  	countHs-=objBg.getCount();
                  }	
              
               }//end of iterator i3 i.e inner loop      bigram loop
               
               //to check if the pbGt ==0 so that we multiply pbML by 0.99
               if(pbGT==0)
             	  pbML*=.99;		//0.99 is gamma to count on the fly
               
               Double alpha=(1-pbML-pbGT)/(1-pbWord);
               objUg.setBackoffWeight(Math.log(alpha)/Math.log(2.0));
            }//end of iterator for unigram i.e outer loop
            //traverse through unigram
            //and go through all words.
          
            //to create a LM file

            BufferedWriter writer=null;
            
            try{
            	writer=new BufferedWriter(new FileWriter("lm.txt"));
            	
            	writer.write("unigrams:\n");  
            	//iterator for unigram
            	Set setWrite = unigram.entrySet();
                Iterator iWrite = setWrite.iterator();
                while(iWrite.hasNext()) {
                     Map.Entry me = (Map.Entry)iWrite.next();
                     Unigram obj=(Unigram) me.getValue();
                     //to remove the count of single and double
                     writer.write(obj.getLogProbability().toString());
                     writer.write(" ");
                     writer.write(obj.getName());
                     writer.write(" ");
                     writer.write(obj.getBackoffWeight().toString());
                     writer.write("\n");
                  }
            	
            	writer.write("bigrams:\n");
            	//iterator for bigram
            	Set setWriteB = bigram.entrySet();
                Iterator iWriteB = setWriteB.iterator();
                while(iWriteB.hasNext()) {
                     Map.Entry me = (Map.Entry)iWriteB.next();
                     Bigram obj=(Bigram) me.getValue();
                     //to remove the count of single and double
                     writer.write(obj.getLogProbability().toString());
                     writer.write(" ");
                     writer.write(obj.getHistory());
                     writer.write(" ");
                     writer.write(obj.getWord());    
                     writer.write("\n");
                  }
            }
            catch(Exception e)
            {}
            finally
            {
            	if(writer!=null)
            		try{
            			writer.close();
            		}
            	catch(Exception e)
            	{}
            }    
            System.out.println("LM File Generated");
	}	}