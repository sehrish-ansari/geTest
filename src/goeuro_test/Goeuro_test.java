/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package goeuro_test;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.json.JSONArray;
 
import org.json.JSONObject;
 
/**
 *
 * @author Ansari
 */
public class Goeuro_test {

    /**
     * @param args the command line arguments
     */
    
        public static String getCsvFormat(JSONArray jsonarray, String seperator){
            String csv_str=" _id"+seperator+" name"+seperator+" type"+seperator+" latitude"+seperator+" longitude\n";
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                String _id = jsonobject.get("_id").toString();
                String name = jsonobject.getString("name");
                String type = jsonobject.getString("type");
                JSONObject geo_position = jsonobject.getJSONObject("geo_position");
                String latitude=geo_position.get("latitude").toString();
                String longitude=geo_position.get("longitude").toString();
                String csv=_id+seperator+" "+name+seperator+" "+type+seperator+" "+latitude+seperator+" "+longitude+"\n";
                csv_str+=csv;
            }
            return csv_str;
        }
        public static void processLocation(String city_name){
        String csv_data="";
        try {
                        URL url = new URL("http://api.goeuro.com/api/v2/position/suggest/en/"+city_name);
                        URLConnection connection = url.openConnection();
                        connection.setDoOutput(true);
                        connection.setRequestProperty("Content-Type", "application/json");
                        connection.setConnectTimeout(5000);
                        connection.setReadTimeout(5000);
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String string = "";
		        String line;
                        while ((line=in.readLine())!= null) {
                            string += line + "\n";
                        }
                        JSONArray jsonarray = new JSONArray(string);
                        csv_data=getCsvFormat(jsonarray,",");
                        in.close();
                } catch (Exception e) {
                        System.out.println("\nError while calling Cityy REST Service");
                        System.out.println(e);
                        e.printStackTrace();
                }
        writeFile(csv_data, city_name+".csv");
        System.out.println(csv_data);
        
    }
        public static void writeFile(String data, String filename){
            try {        
                Files.write(Paths.get("./"+filename), data.getBytes());

            } catch (Exception x) {
                System.err.format("IOException: %s%n", x);
            }
        }
 	public static void main(String[] args) {
            //System.out.println(args[0]);
            String city_name="Berlin";
            processLocation(city_name);
        }
    
    
}
