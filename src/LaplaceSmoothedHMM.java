import java.io.*;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by Jonelezhang on 4/18/17.
 */
public class LaplaceSmoothedHMM {


    public static Hashtable<String, Float> transition_probabilities = new Hashtable<>();
    public static Hashtable<String, Float> observation_likelihood = new Hashtable<>();
    public static Hashtable<String, Float> initial_probability = new Hashtable<>();


    public void laplaceSmoothedHMM(){


        // Transition Probabilities (ti/ti-1)  = C(ti-1,ti)+1 / C(ti-1)+k
        // tag_tag_table(ti-1, ti)
        int k = AnalysisData.tag_table.size();

        for(Map.Entry<String, Integer> entry_tag_current : AnalysisData.tag_table.entrySet()){
            String current_tag = entry_tag_current.getKey();

            for(Map.Entry<String, Integer> entry_tag_former: AnalysisData.tag_table.entrySet()){
                String former_tag = entry_tag_former.getKey();
                // get the count for (ti-1)
                Integer value = entry_tag_former.getValue();
                String key_tag_pair_value = current_tag+"/"+former_tag;
                String key_tag_pair_value_table = former_tag+"/"+current_tag;


                if(AnalysisData.tag_tag_table.containsKey(key_tag_pair_value_table)){
                    Integer value_tag = AnalysisData.tag_tag_table.get(key_tag_pair_value_table);
                    //key of Transition Probabilities
                    transition_probabilities.put(key_tag_pair_value, (float) (value_tag+1) / (value+k));
                }else{
                    transition_probabilities.put(key_tag_pair_value, (float) (0+1)/(value+k));
                }


            }
        }




        //Observation likelihood (word／tag)  C(tag, word)+1/C(tag)+m
        int m =  AnalysisData.word_table.size();


        for(Map.Entry<String, Integer> entry_word_current : AnalysisData.word_table.entrySet()){
            String current_word = entry_word_current.getKey();


            for(Map.Entry<String, Integer> entry_tag_current : AnalysisData.tag_table.entrySet()){
                String current_tag = entry_tag_current.getKey();
                Integer value = AnalysisData.tag_table.get(current_tag);
                String key_word_tag_value = current_word+ "/"+ current_tag;
                String key_word_tag_value_table = current_tag +"/"+current_word;


                if(AnalysisData.tag_word_table.containsKey(key_word_tag_value_table)){
                    Integer value_tag_word = AnalysisData.tag_word_table.get(key_word_tag_value_table);
                    observation_likelihood.put(key_word_tag_value, (float) (value_tag_word+1)/ (value+m));
                }
                  else{
                    observation_likelihood.put(key_word_tag_value, (float) 1/(value+m));
                }


            }
        }


        //initial tag probability
        for (String tag: AnalysisData.tag_list) {

            if(AnalysisData.Initial_tag_table.containsKey(tag)) {
                Integer value = AnalysisData.Initial_tag_table.get(tag);
                initial_probability.put(tag, (float) (value + 1 )/ (AnalysisData.sentence_count + k));
            }else{
                initial_probability.put(tag, (float) 1/(AnalysisData.sentence_count + k));
            }
        }




        // print transition_probabilities into a file
        try{
            PrintWriter writer = new PrintWriter("transition_probabilities.txt", "UTF-8");
            for (Map.Entry<String, Float> entry : transition_probabilities.entrySet()) {
                String key = entry.getKey();
                Float value = entry.getValue();
                writer.println ("tag_tag: " + key + " Value: " + value);
            }
            writer.close();
        } catch (IOException e) {
            // do something
        }


        // print observation_likelihood into a file
        try{
            PrintWriter writer = new PrintWriter("observation_likelihood.txt", "UTF-8");
            for (Map.Entry<String, Float> entry : observation_likelihood.entrySet()) {
                String key = entry.getKey();
                Float value = entry.getValue();
                writer.println ("word_tag: " + key + " Value: " + value);
            }
            writer.close();
        } catch (IOException e) {
            // do something
        }

        // print initial tag probability into a file
        try{
            PrintWriter writer = new PrintWriter("initial tag probability.txt", "UTF-8");
            for (Map.Entry<String, Float> entry : initial_probability.entrySet()) {
                String key = entry.getKey();
                Float value = entry.getValue();
                writer.println ("tag: " + key + " Value: " + value);
            }
            writer.close();
        } catch (IOException e) {
            // do something
        }



    }
}
