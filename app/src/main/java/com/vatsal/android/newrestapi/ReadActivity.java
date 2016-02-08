package com.vatsal.android.newrestapi;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Lenovo on 2/8/16.
 */
public class ReadActivity extends Activity {

    private TextView mContentTV;
    private String mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        init();

        new ReadFile().execute();
    }

    private void init(){
        mContentTV = (TextView)findViewById(R.id.tv_content);
    }

    private void updateUI(){
        mContentTV.setText(mContent);
    }

    private class ReadFile extends AsyncTask<Void,Void,String>{

        private static final String
                URL_STRING = "http://52.18.186.202/kushaldata/read_file.php",
                JSON_S_CONTENT = "content";

        @Override
        protected String doInBackground(Void... params) {
            try{
                URL url = new URL(URL_STRING);

                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(10000);
                connection.setDoInput(true);
                connection.setDoOutput(true);

                int responseCode = connection.getResponseCode();

                StringBuilder builder = new StringBuilder();

                if(responseCode == HttpURLConnection.HTTP_OK){
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while((line = br.readLine())!=null){
                        builder.append(line);
                    }
                    br.close();
                }
                connection.disconnect();
                return builder.toString();

            }catch(Exception e){
                return null;
            }
        }


        @Override
        protected void onPostExecute(String s) {
            if(s!=null){
                try{
                    Log.d(getClass().getSimpleName(),s);
                    JSONObject object = new JSONObject(s);
                    mContent = object.getString(JSON_S_CONTENT);
                    updateUI();
                }catch (JSONException e){
                    return;
                }
            }
        }
    }
}
