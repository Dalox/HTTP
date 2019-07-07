package com.app.dalox.http;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText nombre, dni, telefono, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nombre = (EditText)findViewById(R.id.etName);
        dni = (EditText)findViewById(R.id.etDNI);
        telefono = (EditText)findViewById(R.id.etPhone);
        email = (EditText)findViewById(R.id.etEmail);
    }

    private boolean insert(){
        HttpClient httpClient;
        List<NameValuePair> nameValuePairs;
        HttpPost httpPost;
        String uri = "http://172.16.58.136/aHttp/insert.php";
        httpClient = new DefaultHttpClient();
        httpPost = new HttpPost(uri);
        //Valores a Anadir
        nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("nombre",nombre.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("dni",dni.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("telefono",telefono.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("email",email.getText().toString().trim()));

        try{
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httpClient.execute(httpPost);
            /*HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            Toast.makeText(MainActivity.this, EntityUtils.toString(httpEntity),Toast.LENGTH_LONG).show();*/
            //httpClient.execute(httpPost);
            return true;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void onInsert(View view){
        if (!dni.getText().toString().trim().equalsIgnoreCase("")||
                !nombre.getText().toString().trim().equalsIgnoreCase("")||
                !telefono.getText().toString().trim().equalsIgnoreCase("")||
                !email.getText().toString().trim().equalsIgnoreCase("")){
            new Insert(MainActivity.this).execute();
        }else{
            Toast.makeText(MainActivity.this,"Hay informacion por Rellenar", Toast.LENGTH_LONG).show();
        }
    }

    class Insert extends AsyncTask<String, String, String>{

        private Activity context;

        Insert(Activity context){
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params){
            if (insert())
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Persona insertada con Exito", Toast.LENGTH_LONG).show();
                        nombre.setText("");
                        dni.setText("");
                        telefono.setText("");
                        email.setText("");
                    }
                });
            else{
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context,"Persona no se a ingresado con Exito", Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }
    }
}
