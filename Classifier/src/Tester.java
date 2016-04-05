import weka.classifiers.meta.AdaBoostM1;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class Tester {

    public static void main(String[] args) throws Exception {

        //Train data
        StringToWordVectorFilter.filter("../Servlet/web/files/training-set.arff", 0);
        BaggingClassifier.classifyBySMOBagging("../Servlet/web/files/training-set-filtered.arff", 0);

        //Cross-validation
        ClassifierEvaluator.crossValidate("../Servlet/web/files/adaboost-smo-classifier.model", "../Servlet/web/files/training-set-filtered.arff", 0);

        //BBC
        FileConverter.convertJSONtoARFF("../Servlet/web/files/news-sources/bbc.json");
        ClassPredictor.predict("../Servlet/web/files/news-sources/bbc.arff", "../Servlet/web/files/adaboost-smo-classifier.model", "../Servlet/web/files/string-to-word-vector-filter.model");
        Merger.merge("../Servlet/web/files/news-sources/bbc.json", "../Servlet/web/files/news-sources/bbc-labelled.arff", "BBC");

        //CNN
        FileConverter.convertJSONtoARFF("../Servlet/web/files/news-sources/cnn.json");
        ClassPredictor.predict("../Servlet/web/files/news-sources/cnn.arff", "../Servlet/web/files/adaboost-smo-classifier.model", "../Servlet/web/files/string-to-word-vector-filter.model");
        Merger.merge("../Servlet/web/files/news-sources/cnn.json", "../Servlet/web/files/news-sources/cnn-labelled.arff", "CNN");

        //Guardian
        FileConverter.convertJSONtoARFF("../Servlet/web/files/news-sources/guardian.json");
        ClassPredictor.predict("../Servlet/web/files/news-sources/guardian.arff", "../Servlet/web/files/adaboost-smo-classifier.model", "../Servlet/web/files/string-to-word-vector-filter.model");
        Merger.merge("../Servlet/web/files/news-sources/guardian.json", "../Servlet/web/files/news-sources/guardian-labelled.arff", "Guardian");

        //Reuters
        FileConverter.convertJSONtoARFF("../Servlet/web/files/news-sources/reuters.json");
        ClassPredictor.predict("../Servlet/web/files/news-sources/reuters.arff", "../Servlet/web/files/adaboost-smo-classifier.model", "../Servlet/web/files/string-to-word-vector-filter.model");
        Merger.merge("../Servlet/web/files/news-sources/reuters.json", "../Servlet/web/files/news-sources/reuters-labelled.arff", "Reuters");

        //Straits Times
        FileConverter.convertJSONtoARFF("../Servlet/web/files/news-sources/straits-times.json");
        ClassPredictor.predict("../Servlet/web/files/news-sources/straits-times.arff", "../Servlet/web/files/adaboost-smo-classifier.model", "../Servlet/web/files/string-to-word-vector-filter.model");
        Merger.merge("../Servlet/web/files/news-sources/straits-times.json", "../Servlet/web/files/news-sources/straits-times-labelled.arff", "Straits Times");
    }
}
