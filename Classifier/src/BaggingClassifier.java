import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.functions.SMO;
import weka.classifiers.meta.Bagging;
import weka.core.Instances;
import weka.core.SerializationHelper;


public class BaggingClassifier {

    /**
     * Build the classifier by SMO with bagging and serialize it.
     * @param filename The full path of the ARFF file.
     * @param classIndex The index of the class attribute.
     * @return The built classifier.
     * @throws Exception
     */
    public static Bagging classifyBySMOBagging(String filename, int classIndex) throws Exception {

        //Load the instance.
        Instances instances = ArffFileManager.loadARFF(filename, classIndex);

        //Set Bagging classifier.
        Bagging baggingClassifier = new Bagging();
        SMO SMOClassifier = new SMO();
        baggingClassifier.setClassifier(SMOClassifier);
        baggingClassifier.setBagSizePercent(100);
        baggingClassifier.setNumIterations(10);

        //Build the classifier.
        baggingClassifier.buildClassifier(instances);

        //Serialize the classifier.
        SerializationHelper.write("../Servlet/web/files/adaboost-smo-classifier.model", baggingClassifier);

        return baggingClassifier;
    }


}
