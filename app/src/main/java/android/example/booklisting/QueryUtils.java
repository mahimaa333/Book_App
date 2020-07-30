package android.example.booklisting;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {
    public QueryUtils(){}

    public static List<Books> fetchBooksData(String requestUrl){
        try{
            Thread.sleep(2000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpResponse(url);
        } catch (Exception e) {
            Log.e("QueryUtils","Error closing input stream",e);
        }

        List<Books> books = extractBooks(jsonResponse);
        return books;
    }

    private static URL createUrl(String stringUrl){
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e("QueryUtils","Error with creating Url",e);
        }
        return url;
    }

    private static String makeHttpResponse(URL url) throws IOException{
        String jsonResponse = "";
        if(url == null){
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else {
                Log.e("QueryUtils", "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e("QueryUtils","Problem retrieving the earthquake JSON results",e);
        }finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }
            if (inputStream != null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();
        if(inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null){
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public static String[] toStringArray(JSONArray array) {
        if(array==null)
            return null;

        String[] arr=new String[array.length()];
        for(int i=0; i<arr.length; i++) {
            arr[i]=array.optString(i);
        }
        return arr;
    }

    public static String convertArrayToStringMethod(String[] strArray) {
        if(strArray==null)
            return null;
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < strArray.length; i++) {
            stringBuilder.append(strArray[i]);
        }
        return stringBuilder.toString();
    }

    private static List<Books> extractBooks(String booksJson){
        if(TextUtils.isEmpty(booksJson)){
            return null;
        }
        List<Books> books = new ArrayList<>();
        try {
            JSONObject baseJsonResponse = new JSONObject(booksJson);
            JSONArray booksArray = baseJsonResponse.getJSONArray("items");

            for (int i = 0; i < booksArray.length(); i++){
                JSONObject currentBook = booksArray.getJSONObject(i);
                JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");
                String title = volumeInfo.getString("title");
                JSONArray authors = volumeInfo.optJSONArray("authors");
                String [] authorArray = toStringArray(authors);
                String author = convertArrayToStringMethod(authorArray);

//                if (volumeInfo.has("authors")) {
//                    JSONArray authors = volumeInfo.getJSONArray("authors");
//                    Log.println(Log.INFO, "QueryUtils", String.valueOf(authors));
//
//                    // Check JSONArray Returns true if this object has no mapping for name or if it has a mapping whose value is NULL
//                    if (!volumeInfo.isNull("authors")) {
//                        // Get 1st element
//                        author = (String) authors.get(0);
//                    } else {
//                        // assign info about missing info about author
//                        author = "*** unknown author ***";
//                    }
//                } else {
//                    // assign info about missing info about author
//                    author = "*** missing info of authors ***";
//                }
                String description = volumeInfo.getString("description");
                String url = volumeInfo.getString("canonicalVolumeLink");

                Books book = new Books(title,author,description,url);
                books.add(book);
            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }
        return books;
    }
}
