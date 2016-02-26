/*@author 
 * NAME:		Ameya Parab
 * UTD ID:		2021166954
 * NET ID:		axp132530
 * SUB: 		NLP HOMEWORK TWO
 * Description: To use the LM File computed from the Train program and compute the
 * 				perplexity for testing set.
 * 
 * Note: 		This Class requires Unigram and Bigram class objects.
 */


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class NLP_ASS2_AParab_Test {
	public static void main(String args[])
	{
		String lm_file = "lm.txt";
		String [] line_split = null; 
		String line = null;
		boolean flag_unigram=false;
		HashMap<String,Unigram> unigram_train = new HashMap();
		HashMap<String,Bigram> bigram_train = new HashMap();

		try {
			FileReader fileReader =  new FileReader(lm_file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			//method to get the total words from the LM file of training set
			while((line = bufferedReader.readLine()) != null) {
				line_split = line.split("[\n]+");

				if(line_split[0].equalsIgnoreCase("unigrams:"))
				{
					flag_unigram=true;
				}
				else if(line_split[0].equalsIgnoreCase("bigrams:"))
				{
					flag_unigram=false;
				}
				else   	//to store the values and create hash map and objects
				{
					String [] second_split=null;
					if(flag_unigram)		//unigram
					{
						second_split=line_split[0].split(" ");			//there will be three splits   prob , word , backoff
						Unigram ugObj=new Unigram(second_split[1]);
						ugObj.setBackoffWeight(Double.parseDouble(second_split[2]));
						ugObj.setLogProbability(Double.parseDouble(second_split[0]));
						unigram_train.put(second_split[1],ugObj);
					}
					else				//bigram
					{
						second_split=line_split[0].split(" ");			//there will be three splits   prob , his , word
						String hs=second_split[1];						//history
						String w=second_split[2];						//word
						String bgWord=hs+"\t"+w;						//complete word seperated by tab
						Bigram bgObj=new Bigram(hs,w);	 				//to create object of class Bigram
						//                		 if(second_split[0].equalsIgnoreCase("-Infinity"))
						//                		 	bgObj.setLogProbability(-10e100);
						//                		 else			
						bgObj.setLogProbability(Double.parseDouble(second_split[0]));

						bigram_train.put(bgWord,bgObj);
					}
				}            	 
			}	
		}
		catch(Exception e){}
		TreeMap<String,Double> predictionWords;
		Scanner sc=new Scanner(System.in);
		String word;
		Spelling spObj=new Spelling();
		word=sc.nextLine();
		while(!word.equalsIgnoreCase("###"))
		{
			predictionWords = new TreeMap();
			spObj.execute(word);
			Iterator it = bigram_train.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pairs = (Map.Entry)it.next();
				Bigram bgObj=(Bigram) pairs.getValue();
				if((bgObj.getHistory()).equalsIgnoreCase(word))
					predictionWords.put(bgObj.getWord(), bgObj.getCount());
			}

			if(predictionWords.size()!=0)
			{
				System.out.println("The predicted word is "+predictionWords.firstEntry().getKey());
				predictionWords = null;
			}

			word=sc.nextLine();
		}
		System.out.println("Execution Stopped");
	}
}