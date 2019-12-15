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
                new SendLightCommandTask().execute();
            }
        });
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
                Socket s = new Socket("192.168.1.4", 8006);
                BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                String ack = in.readLine();
                in.close();
                s.close();
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
                Socket s = new Socket("192.168.1.4", 8006);
                BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                String ack = in.readLine();
                if (ack != null && !ack.equals("off") && !ack.equals("on")) {
                    in.close();
                    s.close();
                    return MainActivity.Status.DISCONNECTED;
                }

                OutputStream out = s.getOutputStream();
                byte[] data = new byte[]{1};
                out.write(data);
                ack = in.readLine();
                in.close();
                out.close();
                s.close();
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
