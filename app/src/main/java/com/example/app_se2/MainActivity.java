package com.example.app_se2;

import androidx.appcompat.app.AppCompatActivity;

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
    private TextView matrView = null;
    private TextView numbSend = null;
    private TextView ergView = null;
    private Socket clientSocket =null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonsend = (Button) findViewById(R.id.buttonSend);
        matrView = (TextView) findViewById(R.id.textView);
        ergView = (TextView) findViewById(R.id.outputView);
        numbSend = (TextView) findViewById(R.id.numberInput);

        buttonsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(numbSend.getText().toString());
            }
        });
    }

    protected void sendMessage(final String message)
    {
        String result = "";
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
            result = mBufferIn.readLine();

            ergView.setText(result.toString());

            socket.close();
        }catch (Exception ex)
        {
            Log.d("TCPClient", "ERROR:"+ ex.getMessage());
            ex.printStackTrace();
        }
        Log.d("TCPClient", "Daten wurden gesendet");

    }



}