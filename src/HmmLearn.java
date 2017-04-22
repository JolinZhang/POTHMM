import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by Jonelezhang on 4/2/17.
 */
public class HmmLearn {

    public static Hashtable<String, Float> transition_probabilities = new Hashtable<>();
    public static Hashtable<String, Float> observation_likelihood = new Hashtable<>();
    public static Hashtable<String, Float> initial_probability = new Hashtable<>();

    public void hmmLearn(){
        // Transition Probabilities (ti/ ti-1)  = C(ti-1, ti)/C(ti-1)
        // tag_tag_table(ti-1, ti)

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
                    transition_probabilities.put(key_tag_pair_value, (float) value_tag / value);
                }else{
                    transition_probabilities.put(key_tag_pair_value, (float) 0);
                }


            }
        }




        //Observation likelihood (word/tag)  C(tag, word)/C(tag)

        for(Map.Entry<String, Integer> entry_word_current : AnalysisData.word_table.entrySet()) {
            String current_word = entry_word_current.getKey();

            for (Map.Entry<String, Integer> entry_tag_current : AnalysisData.tag_table.entrySet()) {
                String current_tag = entry_tag_current.getKey();
                String key_word_tag_value = current_word + "/" + current_tag;
                Integer value = AnalysisData.tag_table.get(current_tag);
                String key_word_tag_value_table = current_tag +"/"+current_word;


                if(AnalysisData.tag_word_table.containsKey(key_word_tag_value_table)){
                    Integer value_tag_word = AnalysisData.tag_word_table.get(key_word_tag_value_table);
                    observation_likelihood.put(key_word_tag_value, (float) value_tag_word/ value);
                }else{
                    observation_likelihood.put(key_word_tag_value, (float) 0/value);
                }



            }
        }




        //initial tag probability
        for (String tag: AnalysisData.tag_list) {

            if(AnalysisData.Initial_tag_table.containsKey(tag)) {
                Integer value = AnalysisData.Initial_tag_table.get(tag);
                initial_probability.put(tag, (float) value / AnalysisData.sentence_count );
            }else{
                initial_probability.put(tag, (float) 0);
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



//        for( String tag : tag_list){
//            System.out.println ("tag_list: " +tag);
//        }
//        System.out.println(tag_list.size());
////
//        for (Map.Entry<String, Float> entry : transition_probabilities.entrySet()) {
//            String key = entry.getKey();
//            Float value = entry.getValue();
//            System.out.println ("tag_tag: " + key + " Value: " + value);
//        }
//        for (Map.Entry<String, Float> entry : observation_likelihood.entrySet()) {
//            String key = entry.getKey();
//            Float value = entry.getValue();
//            System.out.println ("tag_word: " + key + " Value: " + value);
//        }
//        for (Map.Entry<String, Float> entry : initial_probability.entrySet()) {
//            String key = entry.getKey();
//            Float value = entry.getValue();
//            System.out.println ("tag_word: " + key + " Value: " + value);
//        }
//        System.out.println(initial_probability.size());





        System.out.println("sentence count: "+AnalysisData.sentence_count);
    }
}
