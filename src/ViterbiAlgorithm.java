import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by Jonelezhang on 3/30/17.
 */
public class ViterbiAlgorithm  extends LaplaceSmoothedHMM{



    public void viterbi( String path_test) {
        ReadFile files = new ReadFile();
        for (String s : files.readFiles(path_test)) {
            if (s.length() > 0) {
                s = s.replaceAll("\t", " ");
                String[] word_tag = s.split(" ");

                ArrayList<String> tag_sequence = new ArrayList<>();
                ArrayList<String> word_sequence = new ArrayList<>();

                if (word_tag.length > 0) {
                    for (String single_word_tag : word_tag) {
                        if (single_word_tag.contains("/")) {
                            String[] single_word_tag_pair = single_word_tag.split("/");
                            String word = single_word_tag_pair[0];
                            String tag = single_word_tag_pair[1];
                            //get all tag sequence in a sentence
                            tag_sequence.add(tag);
                            // get all word sequence in a word
                            word_sequence.add(word);
                        }
                    }

                    // use Viterbi_Al for each sentence, get the back trace.
                    if(word_sequence.size()>0){
                        viterbi_Al(word_sequence,tag_sequence );
                    }


                }
            }

        }
    }


    public void viterbi_Al(ArrayList<String> words_sequence, ArrayList<String> tag_sequence) {



        //order for tag is as tag_list
        int  state_num = AnalysisData.tag_list.size();
        int  observation_num = words_sequence.size();

        float[][] viterbiArray = new float[state_num+2][observation_num];
        int[][] backPointer = new int[state_num+2][observation_num];



        //initialization step

        for(int i = 1; i <= state_num; i++){
            String current_tag = AnalysisData.tag_list.get(i-1);
            if(words_sequence.size()>0) {
                String word_tag_pair = words_sequence.get(0) + "/" + current_tag;
                if (observation_likelihood.containsKey(word_tag_pair)) {
                    viterbiArray[i][0] = (float)1 * initial_probability.get(current_tag) * observation_likelihood.get(word_tag_pair);
                } else {
                    viterbiArray[i][0] = 1 * initial_probability.get(current_tag) * 0;
                }
            }
            backPointer[i][0] = 0;
        }

        //recursion step
        for(int t = 1; t < observation_num; t++){
            for(int s = 1; s <= state_num; s++){
                String current_tag = AnalysisData.tag_list.get(s-1);
                float max = 0.0f;
                for(int sl  =1; sl <= state_num; sl++ ){
                    String former_tag = AnalysisData.tag_list.get(sl-1);
                    String tag_tagformer_pair = current_tag+"/"+former_tag;
                    String word_tag_pair = words_sequence.get(t)+"/"+current_tag;
                    float value =  0.0f;
                    if(transition_probabilities.containsKey(tag_tagformer_pair) && observation_likelihood.containsKey(word_tag_pair) ){
                        value = viterbiArray[sl][t-1]*transition_probabilities.get(tag_tagformer_pair)*observation_likelihood.get(word_tag_pair);
                    }else if(!transition_probabilities.containsKey(tag_tagformer_pair) && !observation_likelihood.containsKey(word_tag_pair) ){
                        value = viterbiArray[sl][t-1]*(0.0f)*(0.0f);
                    }else if( transition_probabilities.containsKey(tag_tagformer_pair) && !observation_likelihood.containsKey(word_tag_pair) ){
                        value = viterbiArray[sl][t-1]*transition_probabilities.get(tag_tagformer_pair)*(0.0f);
                    }else if( !transition_probabilities.containsKey(tag_tagformer_pair) && observation_likelihood.containsKey(word_tag_pair)){
                        value = viterbiArray[sl][t-1]*(0.0f)*observation_likelihood.get(word_tag_pair);
                    }

                    if(value > max){
                        max = value;
                        backPointer[s][t] = sl;
                    }
                }
                viterbiArray[s][t] = max;

            }
        }

        //end step
        float max = 0.0f;
        for(int s0 =1; s0<=state_num; s0++ ){
            float value = viterbiArray[s0][observation_num-1]*1;
            if(value > max){
                max = value;
                backPointer[state_num+1][observation_num-1] = s0;
            }
        }
        viterbiArray[state_num+1][observation_num-1] = max;


        //print value in backPointer
//        for(int j = 0; j< observation_num; j++){
//            for(int i = 0; i< state_num+2; i++){
//                System.out.print( backPointer[i][j]+ "|");
//            }
//            System.out.println();
//        }

        //print value for viterbiArray


        // print transition_probabilities into a file
        try{
            PrintWriter writer = new PrintWriter("backPointer.txt", "UTF-8");
            for(int i = 0; i< state_num+2; i++){
                for(int j = 0; j< observation_num; j++){
                   writer.print( backPointer[i][j]+ "|");
                }
                writer.println();
            }

            writer.close();
        } catch (IOException e) {
            // do something
        }







        //get the predict tag for each word
        int column = observation_num;
        String tag_predict ="";
        int tag_index =  backPointer[state_num+1][observation_num-1];
        if(tag_index >=1){
            tag_predict = AnalysisData.tag_list.get(tag_index-1);
        }
        String word = words_sequence.get(column-1);
        String tag_true = tag_sequence.get(column-1);
        ArrayList<String> result = new ArrayList<>();
        result.add(word+"/"+tag_true+"/"+tag_predict);

        while(column > 1){
            tag_index = backPointer[tag_index][column-1];
            column--;
            if(tag_index >= 1){
                tag_predict = AnalysisData.tag_list.get(tag_index-1);
            }
            word = words_sequence.get(column-1);
            tag_true = tag_sequence.get(column-1);
            result.add(word+"/"+tag_true+"/"+tag_predict);
        }


        //print the predict result
        try{
            PrintWriter writer = new PrintWriter("predict.txt", "UTF-8");
            for(String prediction_word_tag : result){
                writer.println(prediction_word_tag);
            }

            writer.close();
        } catch (IOException e) {
            // do something
        }

    }



}
