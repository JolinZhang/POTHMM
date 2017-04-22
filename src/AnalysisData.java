import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;

/**
 * Created by Jonelezhang on 4/2/17.
 */
public class AnalysisData {

    public static Hashtable<String, Integer> word_table = new Hashtable<>();
    public static Hashtable<String, Integer> tag_table = new Hashtable<>();
    public static Hashtable<String, Integer> tag_tag_table = new Hashtable<>();
    public static Hashtable<String, Integer> tag_word_table = new Hashtable<>();
    public static Hashtable<String, Integer> Initial_tag_table = new Hashtable<>();

    public static ArrayList<String> tag_list = new ArrayList<>();
    public static int sentence_count = 0;



    public void analysisData( String path_train){

        ReadFile file = new ReadFile();

        //get array of sentences.
        for (String s: file.readFiles( path_train )) {
//            System.out.println(s);
            //get array of current sentence
            s = s.replaceAll("\t", " ");
            String[] word_tag =s.split(" ");
            ArrayList<String> tag_sequence = new ArrayList<>();
            for(String single_word_tag:word_tag){
                if(single_word_tag.contains("/")){
                    String[] single_word_tag_pair = single_word_tag.split("/");
                    String word = single_word_tag_pair[0];
                    String tag = single_word_tag_pair[1];

                    //get all tag sequence in a sentence
                    tag_sequence.add(tag);


                    //count for each distinct word
                    if(word_table.containsKey(word)){
                        word_table.put(word, word_table.get(word)+1);
                    }else{
                        word_table.put(word, 1);
                    }

                    //count for each distinct tag
                    if(tag_table.containsKey(tag)){
                        tag_table.put(tag,tag_table.get(tag)+1);
                    }else{
                        tag_table.put(tag,1);
                    }

                    //count for (ti, wk)
                    if(tag_word_table.containsKey(tag+"/"+word)){
                        tag_word_table.put(tag+"/"+word,tag_word_table.get(tag+"/"+word)+1);
                    }else{
                        tag_word_table.put(tag+"/"+word,1);
                    }
                }
            }


            //count for (Ti-1, Ti)
            int token_num = tag_sequence.size();
            String token1 = "";
            String token2 = "";
            int i =0;
            while(i < token_num){
                if( token_num > 0){
                    token1 = tag_sequence.get(i);
                    i++;
                }
                while( i < token_num ){
                    token2 = tag_sequence.get(i);
                    String tag_tag_pair = token1+"/"+token2;
                    if( tag_tag_table.containsKey(tag_tag_pair)){
                        tag_tag_table.put(tag_tag_pair, tag_tag_table.get(tag_tag_pair)+1);
                    }else{
                        tag_tag_table.put(tag_tag_pair, 1);
                    }
                    token1 =token2;
                    i++;
                }

            }


            //count for the initial probability for each tag
            if(token_num>0){
                String initial_tag = tag_sequence.get(0);
                sentence_count++;
                if(Initial_tag_table.containsKey(initial_tag))
                {
                    Initial_tag_table.put(initial_tag,Initial_tag_table.get(initial_tag)+1);
                }else{
                    Initial_tag_table.put(initial_tag, 1);
                }
            }


        }

//
//        for (Map.Entry<String, Integer> entry : word_table.entrySet()) {
//            String key = entry.getKey();
//            Integer value = entry.getValue();
//            System.out.println ("word: " + key + " Value: " + value);
//        }

//
        System.out.println("distinct word: "+ word_table.size());
//        for (Map.Entry<String, Integer> entry : tag_table.entrySet()) {
//            String key = entry.getKey();
//            Integer value = entry.getValue();
//            System.out.println ("tag: " + key + " Value: " + value);
//        }
        System.out.println("distinct tag: "+ tag_table.size());
//        for (Map.Entry<String, Integer> entry : tag_tag_table.entrySet()) {
//            String key = entry.getKey();
//            Integer value = entry.getValue();
//            System.out.println ("tag_tag: " + key + " Value: " + value);
//        }
//        for (Map.Entry<String, Integer> entry : tag_word_table.entrySet()) {
//            String key = entry.getKey();
//            Integer value = entry.getValue();
//            System.out.println ("tag_word: " + key + " Value: " + value);
//        }
//        for (Map.Entry<String, Integer> entry : Initial_tag_table.entrySet()) {
//            String key = entry.getKey();
//            Integer value = entry.getValue();
//            System.out.println ("initial_tag: " + key + " Value: " + value);
//        }



        // get fixed tag list
        tag_list = Collections.list(tag_table.keys());




    }
}
