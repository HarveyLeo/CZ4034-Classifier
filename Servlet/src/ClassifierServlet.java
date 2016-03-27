import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Haihui on 24/3/2016.
 */
@WebServlet(name = "ClassifierServlet", urlPatterns = {"/classify"})
public class ClassifierServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        classify();
        response.getWriter().println("OK");
    }

    private void classify() {
        try {
            //BBC
            FileConverter.convertJSONtoARFF(getServletContext().getRealPath("files/news-sources/bbc.json"));
            ClassPredictor.predict(getServletContext().getRealPath("files/news-sources/bbc.arff"),
                    getServletContext().getRealPath("files/adaboost-smo-classifier.model"),
                    getServletContext().getRealPath("files/string-to-word-vector-filter.model"));
            Merger.merge(getServletContext().getRealPath("files/news-sources/bbc.json"),
                    getServletContext().getRealPath("files/news-sources/bbc-labelled.arff"), "BBC");

            //CNN
            FileConverter.convertJSONtoARFF(getServletContext().getRealPath("files/news-sources/cnn.json"));
            ClassPredictor.predict(getServletContext().getRealPath("files/news-sources/cnn.arff"),
                    getServletContext().getRealPath("files/adaboost-smo-classifier.model"),
                    getServletContext().getRealPath("files/string-to-word-vector-filter.model"));
            Merger.merge(getServletContext().getRealPath("files/news-sources/cnn.json"),
                    getServletContext().getRealPath("files/news-sources/cnn-labelled.arff"), "CNN");

            //Guardian
            FileConverter.convertJSONtoARFF(getServletContext().getRealPath("files/news-sources/guardian.json"));
            ClassPredictor.predict(getServletContext().getRealPath("files/news-sources/guardian.arff"),
                    getServletContext().getRealPath("files/adaboost-smo-classifier.model"),
                    getServletContext().getRealPath("files/string-to-word-vector-filter.model"));
            Merger.merge(getServletContext().getRealPath("files/news-sources/guardian.json"),
                    getServletContext().getRealPath("files/news-sources/guardian-labelled.arff"), "Guardian");

            //Reuters
            FileConverter.convertJSONtoARFF(getServletContext().getRealPath("files/news-sources/reuters.json"));
            ClassPredictor.predict(getServletContext().getRealPath("files/news-sources/reuters.arff"),
                    getServletContext().getRealPath("files/adaboost-smo-classifier.model"),
                    getServletContext().getRealPath("files/string-to-word-vector-filter.model"));
            Merger.merge(getServletContext().getRealPath("files/news-sources/reuters.json"),
                    getServletContext().getRealPath("files/news-sources/reuters-labelled.arff"), "Reuters");

            //Straits Times
            FileConverter.convertJSONtoARFF(getServletContext().getRealPath("files/news-sources/straits-times.json"));
            ClassPredictor.predict(getServletContext().getRealPath("files/news-sources/straits-times.arff"),
                    getServletContext().getRealPath("files/adaboost-smo-classifier.model"),
                    getServletContext().getRealPath("files/string-to-word-vector-filter.model"));
            Merger.merge(getServletContext().getRealPath("files/news-sources/straits-times.json"),
                    getServletContext().getRealPath("files/news-sources/straits-times-labelled.arff"), "Straits Times");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
