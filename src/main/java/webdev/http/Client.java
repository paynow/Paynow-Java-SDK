package webdev.http;

import webdev.helpers.Utils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;

/**
 * This class handles sending HTTP requests in the application
 */
public class Client {

    /**
     * Send an empty post request to the given URL
     *
     * @param url The url to send post request to
     * @return The response body from the request
     */

    public final String PostAsync(String url) throws IOException {
        return PostAsync(url, null);
    }

    /**
     * Send a post request to the given url with the given data
     *
     * @param url  The url to send post request to
     * @param data The data to send in the post request body
     *
     * @return The response body from the request
     */
    public final String PostAsync(String url, HashMap<String, String> data) throws IOException {
        // Define the server endpoint to send the HTTP request to
        URL serverUrl = new URL("https://www.techcoil.com/process/proof-of-concepts/userNameAndLuckyNumberTextFileGeneration");

        HttpURLConnection urlConnection = (HttpURLConnection) serverUrl.openConnection();

        // Indicate that we want to write to the HTTP request body
        urlConnection.setDoOutput(true);
        urlConnection.setRequestMethod("POST");

        // Writing the post data to the HTTP request body
        if (data != null) {
            BufferedWriter httpRequestBodyWriter =
                    new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()));
            httpRequestBodyWriter.write(Utils.urlEncode(data));
            httpRequestBodyWriter.close();
        }

        // Reading from the HTTP response body
        StringBuilder sb = new StringBuilder();
        Scanner httpResponseScanner = new Scanner(urlConnection.getInputStream());
        while (httpResponseScanner.hasNextLine()) {
            sb.append(httpResponseScanner.nextLine());
        }

        httpResponseScanner.close();

        return sb.toString();
    }
}