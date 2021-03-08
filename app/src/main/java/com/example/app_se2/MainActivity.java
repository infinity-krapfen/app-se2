package com.example.app_se2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    public static final String SERVER_DOMAIN = "se2-isys.aau.at";
    public static final int SERVER_PORT = 53212;
    private Button buttonsend = null;
    private Button buttonCheck = null;
    private TextView matrView = null;
    private TextView numbSend = null;
    private TextView ergView = null;
    private Socket clientSocket =null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonsend = (Button) findViewById(R.id.buttonSend);
        buttonCheck = (Button) findViewById(R.id.buttonCheck);
        matrView = (TextView) findViewById(R.id.textView);
        ergView = (TextView) findViewById(R.id.outputView);
        numbSend = (TextView) findViewById(R.id.numberInput);

        buttonsend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new RunTaskSeparate().execute(numbSend.getText().toString());
            }
        });

        buttonCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBinaryQuersumme(numbSend.getText().toString());
            }
        });

    }

    public class RunTaskSeparate extends AsyncTask<String, String, String> {
                @Override
                protected String doInBackground(String... message) {
                    String result =  sendMessage(message[0]);
                    publishProgress(result);
                    return null;
                }
                @Override
                protected void onProgressUpdate(String... values) {
                    super.onProgressUpdate(values);
                    ergView.setText("");
                    ergView.setText(values[0]);
                }
            }

   protected String sendMessage(final String message)
    {
        String erg = "";
        try {
            //Socket Verbindung wird aufgebaut
            Socket socket = new Socket(SERVER_DOMAIN, SERVER_PORT);
            //schick die Nachricht zu dem Server
            PrintWriter mBufferOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            //Fang die Nachricht von Server auf
            BufferedReader mBufferIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //TCP- text senden
            mBufferOut.println(message);
            mBufferOut.flush();
            //lesen
            erg = mBufferIn.readLine();
            socket.close();
        }catch (Exception ex)
        {
            Log.d("TCPClient", "ERROR:"+ ex.getMessage());
            ex.printStackTrace();
        }
        Log.d("TCPClient", "Daten wurden gesendet");
        return erg;
    }

    protected void getBinaryQuersumme (String matr){

        int result = 0;
        String[] storage = matr.split(""); //teile jede Ziffer auf

        for(int i = 0; i<storage.length; i++){

            result += Integer.parseInt(storage[i]); // array wird durchgegangen und jedesmals mit dem Wert davor addiert
        }

        ergView.setText("");
       ergView.setText(Integer.toBinaryString(result));
    }


}
