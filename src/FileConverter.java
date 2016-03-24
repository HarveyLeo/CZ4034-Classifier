import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.CDL;
import org.json.JSONObject;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.FilenameUtils;

import weka.core.Attribute;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.core.FastVector;


public class FileConverter {

    public static final String URL_REGEX = "(https?://)?([\\da-z.-]+)\\.([a-z.]*)([/\\w.-]*)*/?";
    public static final String NON_LETTER_SPACE_REGEX = "[^A-Za-z ]";
    public static final String SPACE_REGEX = "\\s+";


    /**
     * Generate ARFF file given the JSON file.
     * Only the "message" attribute is kept, with URL, non-letter character and redundant spaces removed.
     * Add a new attribute called "@class@" to specify the category.
     * @param filename The full path of a JSON file.
     * @return The ARFF file name.
     * @throws Exception
     */
    public static String convertJSONtoARFF(String filename) throws Exception {
        String csvFilename, arffFilename;
        csvFilename = convertJSONtoCSV(filename);
        arffFilename = convertCSVtoARFF(csvFilename);
        File csvFile = new File(csvFilename);
        boolean hasDeleted = csvFile.delete();
        if (!hasDeleted) {
            throw new Exception();
        }
        return arffFilename;
    }


    /**
     * Generate the pruned CSV file given the JSON file.
     * Only the "message" attribute is kept, with URL, non-letter character and redundant spaces removed.
     * @param filename The full path of a JSON file.
     * @return The CSV file name.
     * @throws Exception
     */
    public static String convertJSONtoCSV(String filename) throws Exception {

        //Read the JSON file into a JSON array.
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String jsonString = IOUtils.toString(br);
        br.close();
        JSONArray jsonArray = new JSONArray(jsonString);

        //Create a new JSON array to store the pruned JSON objects.
        JSONArray prunedJsonArray = new JSONArray();

        //Only "message" attribute is kept, and URL, non-letter character and redundant spaces are removed.
        JSONObject jsonObject, prunedJsonObject, attachment;
        String message;
        for (int i = 0; i < jsonArray.length(); i++) {
            jsonObject = jsonArray.getJSONObject(i);

            //Concatenate the post message together with the attachment name.
            message = jsonObject.getString("message");
            if (jsonObject.has("attachment")) {
                attachment = jsonObject.getJSONObject("attachment");
                if (attachment.has("name")){
                    message = message.concat(" " + attachment.getString("name"));
                }
            }

            //Remove URL, non-letter character and redundant spaces.
            message = message.replaceAll(URL_REGEX, " ").replaceAll(NON_LETTER_SPACE_REGEX, " ");
            message = message.replaceAll(SPACE_REGEX, " ").trim();

            //Set message to "nil" if it is empty.
            if (message.length() == 0) {
                message = "nil";
            }

            prunedJsonObject = new JSONObject();
            prunedJsonObject.put("message", message);
            prunedJsonArray.put(prunedJsonObject);
        }

        //Convert the pruned JSON array to CSV string.
        String csv = CDL.toString(prunedJsonArray);

        //Write the CSV string into .csv file.
        String csvFilename = FilenameUtils.getPath(filename) + FilenameUtils.getBaseName(filename) + ".csv";
        FileWriter fw = new FileWriter(csvFilename);
        IOUtils.write(csv, fw);
        IOUtils.closeQuietly(fw);

        return csvFilename;
    }


    /**
     * Generate ARFF file given the CSV file.
     * Add a new attribute called "@class@" to specify the category.
     * @param filename The full path of a CSV file.
     * @return The ARFF file name.
     * @throws Exception
     */
    public static String convertCSVtoARFF(String filename) throws Exception {

       //Load CSV.
        CSVLoader loader = new CSVLoader();
        loader.setSource(new File(filename));
        Instances data = loader.getDataSet();

        //Create the nominal class type: categories.
        ArrayList<String> categories = new ArrayList<String>();
        categories.add("Politics");
        categories.add("Economy");
        categories.add("Social");
        categories.add("Technology");

        data.insertAttributeAt(new Attribute("@class@", categories), 0);

        //Save ARFF.
        ArffSaver saver = new ArffSaver();
        saver.setInstances(data);
        String arffFilename = FilenameUtils.getPath(filename) + FilenameUtils.getBaseName(filename) + ".arff";
        saver.setFile(new File(arffFilename));
        saver.writeBatch();

        return arffFilename;
    }

}
