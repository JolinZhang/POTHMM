/**
 * Created by Jonelezhang on 3/28/17.
 */
public class Start {

    public static void main(String args[]){
        String path_train = "/Users/Jonelezhang/Documents/NLP/Project/brown_train_1";
        AnalysisData analysisData = new AnalysisData();
        analysisData.analysisData(path_train);

//        HmmLearn hmmLearn = new HmmLearn();
//        hmmLearn.hmmLearn();

        LaplaceSmoothedHMM laplaceSmoothedHMM = new LaplaceSmoothedHMM();
        laplaceSmoothedHMM.laplaceSmoothedHMM();


        String path_test = "/Users/Jonelezhang/Documents/NLP/Project/brown_test_1";
        ViterbiAlgorithm viterbiAlgorithm = new ViterbiAlgorithm();
        viterbiAlgorithm.viterbi(path_test);


    }
}
