package com.example.moon7.server_get_exam;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends Activity {

    String myJSON;

    private static final String TAG_RESULTS="result";
    private static final String TAG_NAME = "name";
    private static final String TAG_ADD ="address";
    private static final String site="http://moon70717.dothome.co.kr/get.php";
    HashMap<String,String> persons;
    JSONArray peoples = null;

    ArrayList<HashMap<String, String>> personList;

    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = (ListView) findViewById(R.id.listView);
        personList = new ArrayList<HashMap<String,String>>();
        getData(site);
    }


    public void same(String name){
        Toast.makeText(MainActivity.this,name+"이 있음",Toast.LENGTH_SHORT).show();
    }

    protected void showList(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);

            for(int i=0;i<peoples.length();i++){
                JSONObject c = peoples.getJSONObject(i);
                String name = c.getString(TAG_NAME);
                String address = c.getString(TAG_ADD);

                persons = new HashMap<String,String>();

                persons.put(TAG_NAME,name);
                persons.put(TAG_ADD,address);
                Log.d("d",""+name);
                if (name.equals("moon")) {
                    same(name);
                    Log.d("d","있음");
                }
                personList.add(persons);
            }

            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, personList, R.layout.list_item,
                    new String[]{TAG_NAME,TAG_ADD},
                    new int[]{R.id.name, R.id.address}
            );

            list.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn:
                getData(site);
                break;
            /*case R.id.btn_up:
                Intent i=new Intent(MainActivity.this,UpLoad.class);

                startActivity(i);
                break;*/
        }
    }




    public void getData(String url){
        class GetDataJSON extends AsyncTask<String, Void, String>{

            @Override
            protected String doInBackground(String... params) {
                personList = new ArrayList<HashMap<String,String>>();
                String uri = params[0];

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while((json = bufferedReader.readLine())!= null){
                        sb.append(json+"\n");
                        if (isCancelled()) break;
                    }

                    return sb.toString().trim();

                }catch(Exception e){
                    return null;
                }
            }
//
            @Override
            protected void onPostExecute(String result){
                myJSON=result;

                showList();

            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }

}