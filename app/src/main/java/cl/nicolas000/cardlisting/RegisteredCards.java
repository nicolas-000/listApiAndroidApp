package cl.nicolas000.cardlisting;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class RegisteredCards extends AppCompatActivity {
    private ListView listRegistered;
    private ArrayList<String> listCards;
    private ArrayAdapter<String> adapter;
    private RequestQueue data;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_cards);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        listRegistered = findViewById(R.id.listRegistered);
        listCards = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listCards);
        listRegistered.setAdapter(adapter);

        data = Volley.newRequestQueue(this);

        // Inicia la obtención de datos inmediatamente
        mHandler.post(Refresh);
    }

    private Runnable Refresh = new Runnable() {
        @Override
        public void run() {
            obtainData();
            mHandler.postDelayed(this, 1000);
        }
    };

    private void obtainData() {
        String url = "http://18.205.16.41/getRegisteredCards.php";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    // Limpia la lista antes de agregar nuevos elementos
                    listCards.clear();

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject responseObj = response.getJSONObject(i);
                        String idCard = responseObj.getString("id");
                        String uuidCard = responseObj.getString("uuid");
                        String registerDateCard = responseObj.getString("register_date");
                        listCards.add(idCard + " " + uuidCard + " " + registerDateCard);
                    }

                    // Notifica al adaptador que los datos han cambiado
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("test", "Error al procesar el JSON: " + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("test", "Error en la solicitud HTTP: " + error.getMessage());
            }
        });
        data.add(request);
    }
}
