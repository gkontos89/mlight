package com.marshmallow.mlight;


import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;


public class MainActivity extends AppCompatActivity {

    private LinearLayout layout;
    private Socket s = null;
    private BufferedReader in = null;
    private OutputStream out = null;

    enum Status {
        CONNECTED_AND_ON,
        CONNECTED_AND_OFF,
        DISCONNECTED,
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout = findViewById(R.id.layout);

        new Connect().execute();

        ImageButton ib = findViewById(R.id.power_button);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (s == null || s.isClosed()) {
                    new Connect().execute();
                } else {
                    new SendLightCommandTask().execute();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            s.close();
            in.close();
            out.close();
        } catch (IOException e) {
            s = null;
            in = null;
            out = null;
        }
    }

    private void uiUpdate(MainActivity.Status status) {
        switch (status) {
            case CONNECTED_AND_ON:
                layout.setBackgroundColor(Color.parseColor("#3358FF"));
                break;
            case CONNECTED_AND_OFF:
                layout.setBackgroundColor(Color.parseColor("#84ace0"));
                break;
            case DISCONNECTED:
            default:
                layout.setBackgroundColor(Color.parseColor("#555555"));
                break;
        }
    }

    private class Connect extends AsyncTask<Void, Void, MainActivity.Status> {
        @Override
        protected void onPostExecute(MainActivity.Status status) {
            uiUpdate(status);
        }

        @Override
        protected MainActivity.Status doInBackground(Void... voids) {
            try {
                s = new Socket("192.168.1.4", 8006);
                in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                out = s.getOutputStream();
                String ack = in.readLine();
                if (ack != null) {
                    return ack.equals("on") ? MainActivity.Status.CONNECTED_AND_ON : MainActivity.Status.CONNECTED_AND_OFF;
                } else {
                    return MainActivity.Status.DISCONNECTED;
                }
            } catch (IOException e) {
                return MainActivity.Status.DISCONNECTED;
            }
        }
    }

    private class SendLightCommandTask extends AsyncTask<Void, Void, MainActivity.Status> {

        @Override
        protected void onPostExecute(MainActivity.Status status) {
            uiUpdate(status);
        }

        @Override
        protected MainActivity.Status doInBackground(Void... voids) {
            try {
                byte[] data = new byte[]{1};
                out.write(data);
                String ack = in.readLine();
                if (ack != null) {
                    return ack.equals("on") ? MainActivity.Status.CONNECTED_AND_ON : MainActivity.Status.CONNECTED_AND_OFF;
                } else {
                    return MainActivity.Status.DISCONNECTED;
                }
            } catch (IOException e) {
                return MainActivity.Status.DISCONNECTED;
            }
        }
    }
}
