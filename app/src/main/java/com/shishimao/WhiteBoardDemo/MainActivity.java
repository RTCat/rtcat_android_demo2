package com.shishimao.WhiteBoardDemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.shishimao.sdk.RTCat;
import com.shishimao.sdk.Receiver;
import com.shishimao.sdk.Sender;
import com.shishimao.sdk.Session;
import com.shishimao.sdk.http.RTCatRequests;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    PaintView paintView;
    String token;
    Session session;
    RTCat cat;
    private static final String TAG = "WhiteBoard";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        paintView = (PaintView)findViewById(R.id.board);
        paintView.setAct(this);

        cat = new RTCat(this);
        createSession();
    }

    public void createSession(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RTCatRequests requests = new RTCatRequests(Config.APIKEY, Config.SECRET);
                    token = requests.getToken(Config.SESSION, "pub");
                    session = cat.createSession(token, Session.SessionType.P2P);

                    session.addObserver(new Session.SessionObserver() {
                        @Override
                        public void in(String token) {

                        }

                        @Override
                        public void out(String token) {

                        }

                        @Override
                        public void connected(ArrayList wits) {

                        }

                        @Override
                        public void remote(Receiver receiver) {

                        }

                        @Override
                        public void local(Sender sender) {

                        }

                        @Override
                        public void message(String token, final String message) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    paintView.receiveFromOthers(message);
                                }
                            });
                        }

                        @Override
                        public void error(String s) {
                            Log.e(TAG,s);
                        }

                        @Override
                        public void close() {
                            finish();
                        }
                    });

                    session.connect();

                } catch (Exception e) {
                    Log.e(TAG,e.getMessage());
                }
            }
        }).start();
    }

    public void broadcastMessage(String msg){
        //TODO broadcast message
        session.broadcastMessage(null,msg);
    }
}
