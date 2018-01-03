package com.example.home.weather;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Home on 27/07/2017.
 */

public class getRequest {

    int reqResponseCode;


    /*Request method to do a Post*/
    public String makeWebServiceCall(String urladdress){
        URL url;
        String response     = "";
        int myTimeOut       = 60000;
        String headerName   = null;
        String line;
        String UTF8 = "UTF-8";
        String headerRegistration = "Registration";


        try {

            url                     = new URL(urladdress);
            HttpURLConnection conn  = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(myTimeOut);
            conn.setConnectTimeout(myTimeOut);
            conn.setRequestMethod("GET");


            reqResponseCode = conn.getResponseCode();//get responseCode from request

            System.out.println("reqResponseCode: " + reqResponseCode);

            if (reqResponseCode == HttpsURLConnection.HTTP_OK) {

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream())); // gets response String

                while ((line = br.readLine()) != null) {
                    response += line;
                }

            } else {
                BufferedReader brError = new BufferedReader(new InputStreamReader(conn.getErrorStream())); //gets error response if different from 200
                while ((line = brError.readLine()) != null) {
                    response += line;
                }
            }

            if(reqResponseCode == 503){
                response = "";
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }// end try/catch

        return  response;

    }//end makeWebServiceCall2

    public int getStatusCode() throws IOException {
        return reqResponseCode;
    }
}
