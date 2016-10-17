package bonus.bit.com.multiproc;
import android.os.Bundle;
import android.widget.Toast;

import io.socket.client.IO;
import io.socket.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

import io.socket.emitter.Emitter;
import processing.core.PApplet;

public class Sketch extends PApplet {

    private Socket mSocket;
    public int x, y, tx, ty;
    float speed;
    float minSpeed;
    float thresh;
    public void Sketch(){
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SocketApp app = (SocketApp) getActivity().getApplication();
        mSocket = app.getSocket();

//        mSocket.on(Socket.EVENT_CONNECT,onConnect);
//        mSocket.on(Socket.EVENT_DISCONNECT,onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.on("coordsDown", onCoordsDown);
        mSocket.on("users_update", onUsersUpdate);
        mSocket.connect();



    }
    public void settings() {
        fullScreen(P3D);
    }
    public void setup() {
        x = displayWidth/2;
        y = displayHeight/2;

        speed = 0.03f * displayWidth;
        minSpeed = 0.01f * displayWidth;
        thresh = 0.05f*displayWidth;
    }
    public void draw() {
        background(100,60,120);
        if (dist(tx, ty, x, y)>= thresh) {
            followTarget();
        }
        ellipse(x, y, 50, 50);
    }
    public void mouseDragged(){
        coordsUp((int) (mouseX * 100 / displayWidth), (int) (mouseY * 100 / displayHeight));
    }

    public void coordsUp(int x, int y) {
//        println("EMITTING UP: x: " + x + ", y: " + y);

        mSocket.emit("coordsUp", x, y);
    }
    public void coordsDown(int x, int y){
        System.out.println("x: " + x + ", y: " + y);
        this.tx = x * displayWidth/100;
        this.ty = y * displayHeight /100;
    }

    public void followTarget(){
        int vx = tx - x;
        int vy = ty - y;
        double l =  Math.sqrt(Math.pow(vx, 2)+Math.pow(vy, 2));

        double ux = vx/l;
        double uy = vy/l;

        float sp = (float) (0.1f*l);


        x = x + (int)(Math.max(minSpeed, sp)*ux);
        y = y + (int)(Math.max(minSpeed, sp)*uy);

    }

    public double dist(int x1, int y1, int x2, int y2){
        return  Math.sqrt(Math.pow(x1-x2, 2)+Math.pow(y1-y2, 2));
    }



    /////////////////////////////////////////////////////////////
    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity().getApplicationContext(),
                            R.string.error_connect, Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    private Emitter.Listener onUsersUpdate = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            System.out.println("onUsersUpdate: " + args);


        }
    };

    private Emitter.Listener onCoordsDown = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

//            System.out.println("onCoordsDown: " + args);
            coordsDown((Integer) args[0], (Integer) args[1]);

        }
    };



}