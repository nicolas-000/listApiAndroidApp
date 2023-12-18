package cl.nicolas000.cardlisting;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RegisteredCards extends AppCompatActivity {
    ListView listRegistered;
    ArrayList<String> listCards;
    RequestQueue data;
    Handler mHandler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_cards);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        listRegistered = (ListView) findViewById(R.id.listRegistered);

        data = Volley.newRequestQueue(this);
        Refresh.run();
    }

    public Runnable Refresh =new Runnable() {
        @Override
        public void run() {
            obtainData();
            mHandler.postDelayed(this,1000);
        }
    };

    public void obtainData()
    {
        String url="http://18.205.16.41/getAllCardData.php";

        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response);
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject responseObj = response.getJSONObject(i);
                        String idCard = responseObj.getString("id");
                        String uuidCard = responseObj.getString("uuid");
                        String registerDateCard = responseObj.getString("register_date");
                        listCards.add(idCard + " " + uuidCard + " " + registerDateCard);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        data.add(request);
    }

}