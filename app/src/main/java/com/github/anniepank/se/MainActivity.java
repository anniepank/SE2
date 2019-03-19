package com.github.anniepank.se;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int number = Integer.parseInt(((EditText)findViewById(R.id.number_input)).getText().toString());
                new MyThread(number).start();
            }
        });
    }

    class MyThread extends Thread {
        int number;

        MyThread(int n) {
            number = n;
        }


        @Override
        public void run() {
            try {
                Socket clientSocket = new Socket("se2-isys.aau.at", 53212);
                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                outToServer.writeBytes(String.valueOf(number) + '\n');

                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                final String answer = inFromServer.readLine();

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((TextView)findViewById(R.id.answer_text)).setText(answer);
                    }
                });

                clientSocket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
