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
import java.nio.file.Files;
import java.nio.file.Paths;
import org.json.JSONArray;
import org.json.JSONObject;
 
/**
 *
 * @author Ansari
 */
public class Goeuro_test {

      /*
        This function takes two strings data and and filename, 
        and tries to create a file with the given name and store the data in it.
        In this case, the filename is same as the city_name 
        and it will be created in the same folder as the jar file
    */
        public static void writeFile(String data, String filename){
            try {        
                Files.write(Paths.get("./"+filename), data.getBytes());

            } catch (Exception x) {
                System.err.format("IOException: %s%n", x);
            }
        }
        /*
        This function takes a JSONArray and string for sepertor
           It transforms that JSONArray into csv data format to store in the file using the given separator.
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
        /*
       This function takes the cityname as a string and invokes the goeuro service for that city name
        and returns the data as a string in csv format to be stored in a file later.
        
        */
        public static String getLocationData(String city_name){
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
                        System.out.println("\nError while calling City Service");
                        System.out.println(e);
                        e.printStackTrace();
                }
            return csv_data;
        }
        /*
        This function takes city name as a string input
        and calls the goeuro service for the city name and writes the response in csv format in afile
        that is created in the same folder as the jar file with the template [CITY_NAME].csv
        */
        public static void processLocation(String city_name){
            writeFile(getLocationData(city_name), city_name+".csv");
        }
        /*
        Extracts the city name from the command line argument and creates a csv file with the same name as the city
        in the folder that containes the execuatable file, containing the response of the goeuro location service.
        
        */
 	public static void main(String[] args) {
            String city_name="";
            try{
             city_name=args[0];
             processLocation(city_name);
            }catch(Exception e){
                System.out.println("Exception: City name argument is missing!!");
                e.printStackTrace();
            }
        }
    
    
}

