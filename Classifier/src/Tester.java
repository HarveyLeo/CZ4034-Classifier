import weka.classifiers.meta.AdaBoostM1;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class Tester {

    public static void main(String[] args) throws Exception {

        //Train data
        StringToWordVectorFilter.filter("files/training-set.arff", 0);
        BoostClassifier.classifyBySMOBoosting("files/training-set-filtered.arff", 0);

        //BBC
        FileConverter.convertJSONtoARFF("files/news-sources/bbc.json");
        ClassPredictor.predict("files/news-sources/bbc.arff", "files/adaboost-smo-classifier.model", "files/string-to-word-vector-filter.model");
        Merger.merge("files/news-sources/bbc.json", "files/news-sources/bbc-labelled.arff", "BBC");

        //CNN
        FileConverter.convertJSONtoARFF("files/news-sources/cnn.json");
        ClassPredictor.predict("files/news-sources/cnn.arff", "files/adaboost-smo-classifier.model", "files/string-to-word-vector-filter.model");
        Merger.merge("files/news-sources/cnn.json", "files/news-sources/cnn-labelled.arff", "CNN");

        //Guardian
        FileConverter.convertJSONtoARFF("files/news-sources/guardian.json");
        ClassPredictor.predict("files/news-sources/guardian.arff", "files/adaboost-smo-classifier.model", "files/string-to-word-vector-filter.model");
        Merger.merge("files/news-sources/guardian.json", "files/news-sources/guardian-labelled.arff", "Guardian");

        //Reuters
        FileConverter.convertJSONtoARFF("files/news-sources/reuters.json");
        ClassPredictor.predict("files/news-sources/reuters.arff", "files/adaboost-smo-classifier.model", "files/string-to-word-vector-filter.model");
        Merger.merge("files/news-sources/reuters.json", "files/news-sources/reuters-labelled.arff", "Reuters");

        //Straits Times
        FileConverter.convertJSONtoARFF("files/news-sources/straits-times.json");
        ClassPredictor.predict("files/news-sources/straits-times.arff", "files/adaboost-smo-classifier.model", "files/string-to-word-vector-filter.model");
        Merger.merge("files/news-sources/straits-times.json", "files/news-sources/straits-times-labelled.arff", "Straits Times");
    }
}
