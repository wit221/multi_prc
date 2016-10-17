package bonus.bit.com.multiproc;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;

//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.net.MalformedURLException;
//import io.socket.*;
//import java.net.URISyntaxException;
//import io.socket.SocketIO;

public class MainActivity extends Activity {
    int currentapi = android.os.Build.VERSION.SDK_INT;
    int minImmersiveApi = android.os.Build.VERSION_CODES.JELLY_BEAN;

    Sketch prc;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getFragmentManager();
        prc = new Sketch();
        fragmentManager.beginTransaction()
                .replace(R.id.container, prc)
                .commit();
    }

    @SuppressLint("NewApi")
    public void onResume() {
        super.onResume();
        //p3
        View decorView = getWindow().getDecorView();
        int uiOptions;
        if (currentapi>minImmersiveApi){
            uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }else{
            uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
        }
        decorView.setSystemUiVisibility(uiOptions);
    }

}
