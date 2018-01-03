package com.example.home.weather;


import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.View.OnClickListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class lookWeatherActivity extends AppCompatActivity {

    ImageButton searchButton;
    EditText    cityEditText;
    TextView    resultText;
    ImageView   resultImage;

    private getRequest getReq       = new getRequest();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_weather);

        addListenerOnButton();
    }

    public void addListenerOnButton() {

        searchButton    = (ImageButton) findViewById(R.id.search_button);
        cityEditText    = (EditText) findViewById(R.id.cityName);
        resultText      = (TextView) findViewById(R.id.resultText);


        searchButton.setOnClickListener(new OnClickListener() {



            @Override
            public void onClick(View view) {

                new GetWeatherbyCiTy().execute();

            }

        });
    }

    private class GetWeatherbyCiTy extends AsyncTask<Void, Void, Void> {
        int parseStatusCode;
        List<String> jsonResponse;
        List<String> wheaterList;
        String response;
        Editable city = cityEditText.getText();
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(lookWeatherActivity.this);


        @Override
        protected void onPreExecute() {

        }//end onPreExecute

        @Override
        protected Void doInBackground(Void... params) {
            try {

                //creating a servivce  handler class instance
                //Making a request to url and getting response
                response        = getReq.makeWebServiceCall("http://api.openweathermap.org/data/2.5/forecast/daily?q=" + city + "&cnt=1&appid=dfac9d9f4a7e26870187e5836dad5556");
                parseStatusCode = getReq.getStatusCode();

                if (!response.equals("")) {
                    wheaterList = parseJSON(response);


                } else {
                    response = "";
                }
            } catch (IOException e) {
               // e.printStackTrace();
            }//end try/catch*/
            return null;
        }//end doInBackground

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            try {
                if (!response.equals("")) {
                    /*switch/case to manage statuscode to show different Messages*/

                    System.out.println("parseStatusCode" + parseStatusCode);

                    switch (parseStatusCode) {
                        case 408:
                        case 401:
                            //func.sessionExpired(CreditorMsg_list.this);
                            break;
                        case 426:
                           // func.upgradeApp(CreditorMsg_list.this, jsonResponse.get(0));
                            break;
                        case 200:

                            String myText;
                            myText  = "Name:" + "\t\t\t\t\t\t" + wheaterList.get(0) + "\n" +
                                    "General:" + "\t\t\t\t" + wheaterList.get(4)+ "\n" +
                                    "Description:" + "\t" + wheaterList.get(5) + "\n" +
                                    "Pressure:" + "\t\t\t" + wheaterList.get(1) + "\n" +
                                    "Humidity:" + "\t\t\t" + wheaterList.get(2) + "\n" +
                                    "Deg: " + "\t\t\t\t\t\t\t\t" + wheaterList.get(3) + "\n";
                            resultText.setText(myText);

                            resultImage      = (ImageView) findViewById(R.id.resultImage);

                            String imagename = wheaterList.get(4).toLowerCase();
                            int res = getResources().getIdentifier(imagename, "drawable", "com.example.home.weather");

                            resultImage.setImageResource(res);

                           // resultImage.setImageResource(R.drawable.searchicon);

                            break;
                        case 404:
                            resultText.setText("City no found");
                            break;
                        default:
                            resultText.setText("");
                            break;
                    }
                } else {
                   // func.noInternetConnection(CreditorMsg_list.this);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }//end try/catch
        }//end onPostExecute
    }//end GetCreditorMessages



    //function in charge of PARSING string
    public List<String> parseJSON(String json) {

        if (json != null) {
            try {
                JSONObject Response = new JSONObject(json);

                JSONObject cityJSON         = Response.getJSONObject("city");

                JSONArray listJSON          = Response.getJSONArray("list");
                JSONObject listObjJSON      = listJSON.getJSONObject(0);

                JSONArray weatherJSON       = listObjJSON.getJSONArray("weather");
                JSONObject weatherObjJSON   = weatherJSON.getJSONObject(0);

                Object cityName             = cityJSON.get("name");
                Object pressure             = listObjJSON.get("pressure");
                Object humidity             = listObjJSON.get("humidity");
                Object deg                  = listObjJSON.get("deg");
                Object main                 = weatherObjJSON.get("main");
                Object description          = weatherObjJSON.get("description");

                List<String> weatherInfo = new ArrayList<String>();

                weatherInfo.add(String.valueOf(cityName));
                weatherInfo.add(String.valueOf(pressure));
                weatherInfo.add(String.valueOf(humidity));
                weatherInfo.add(String.valueOf(deg));
                weatherInfo.add(String.valueOf(main));
                weatherInfo.add(String.valueOf(description));

                return weatherInfo;

            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            //Log.e("ServiceHandler", "No data received from HTTP Request");
            return null;
        }
    }
}
