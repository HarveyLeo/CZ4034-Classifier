import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import weka.core.Instances;
import weka.core.converters.ArffLoader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;


public class Merger {

    public static void merge(String jsonFile, String arffFile, String source) throws Exception {

        //Read the JSON file into a JSON array.
        BufferedReader br = new BufferedReader(new FileReader(jsonFile));
        String jsonString = IOUtils.toString(br);
        JSONArray jsonArray = new JSONArray(jsonString);

        //Read the ARFF file.
        ArffLoader loader = new ArffLoader();
        loader.setSource(new File(arffFile));
        Instances data = loader.getDataSet();

        JSONObject jsonObject;
        String category;
        for (int i = 0; i < jsonArray.length(); i++) {
            category = data.instance(i).stringValue(0);
            jsonObject = jsonArray.getJSONObject(i);
            jsonObject.put("source", source);
            jsonObject.put("category", category);
        }

        String updatedJsonFile = FilenameUtils.getFullPath(jsonFile) + FilenameUtils.getBaseName(jsonFile) + "-updated.json";
        FileWriter fw = new FileWriter(updatedJsonFile);
        jsonArray.write(fw);
        fw.close();
    }
}
