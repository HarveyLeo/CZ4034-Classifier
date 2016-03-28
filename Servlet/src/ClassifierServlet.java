import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

@WebServlet(name = "ClassifierServlet", urlPatterns = {"/classify"})
public class ClassifierServlet extends HttpServlet {


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        classify();
        sendPostRequest("bbc-updated");
        sendPostRequest("cnn-updated");
        sendPostRequest("guardian-updated");
        sendPostRequest("reuters-updated");
        sendPostRequest("straits-times-updated");
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


    private void sendPostRequest(String filename) {

        try {
            BufferedReader br = new BufferedReader(new FileReader(getServletContext().getRealPath("files/news-sources/" + filename + ".json")));
            String jsonString = IOUtils.toString(br);
            br.close();

            String data = URLEncoder.encode("text", "UTF-8") + "=" + URLEncoder.encode(jsonString, "UTF-8");
            data += "&" + URLEncoder.encode("filename", "UTF-8") + "=" + URLEncoder.encode(filename, "UTF-8");

//            URL url = new URL("http://192.168.109.3:3000/file");
            URL url = new URL("http://solr.kenrick95.xyz:3000/file");
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setDoOutput(true);
            httpCon.setRequestMethod("POST");
            OutputStreamWriter out = new OutputStreamWriter(
                    httpCon.getOutputStream());
            out.write(data);
            out.close();
            System.out.println(httpCon.getResponseMessage());
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
