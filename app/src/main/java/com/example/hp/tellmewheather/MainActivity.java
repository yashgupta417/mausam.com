package com.example.hp.tellmewheather;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    TextView textView;

    public class Downloadtask extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... strings) {
            String result="";
            try{
                URL url=new URL(strings[0]);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                InputStream in=httpURLConnection.getInputStream();
                InputStreamReader reader=new InputStreamReader(in);
                int data=reader.read();
                while (data!=-1){
                    char current=(char) data;
                    result+=current;
                    data=reader.read();
                }
                return result;
            }catch (Exception e){
                return e.toString();
            }
        }


    }
    public void work(View view){
        EditText editText=(EditText)findViewById(R.id.editText);
        Downloadtask task=new Downloadtask();

        if(!editText.getText().toString().isEmpty()){
            try {
                String jcode = task.execute("https://openweathermap.org/data/2.5/weather?q="+ editText.getText().toString()+"&appid=b6907d289e10d714a6e88b30761fae22" ).get();

                JSONObject jsonObject = new JSONObject(jcode);
                String weather = jsonObject.getString("weather");
                String w1=jsonObject.getString("main");
                JSONObject maininfo=new JSONObject(w1);
                textView.setText("Temperature: "+maininfo.getString("temp")+" c\nPressure: "+maininfo.getString("pressure")+" hPa\nHumidity: "+maininfo.getString("humidity")+" %");
                JSONArray arr=new JSONArray(weather);
                for(int i=0;i<arr.length();i++){
                    JSONObject jsonpart=arr.getJSONObject(i);
                    textView.append("\n"+jsonpart.getString("main")+","+jsonpart.getString("description"));

                }

            }catch (Exception e){
                textView.setText("Could not find your place :(");
            }
        }
        InputMethodManager manager=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(editText.getWindowToken(),0);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView=(TextView)findViewById(R.id.info);
    }
}
