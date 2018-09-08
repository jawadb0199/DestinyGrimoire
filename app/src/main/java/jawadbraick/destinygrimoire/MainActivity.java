package jawadbraick.destinygrimoire;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String API_KEY = "7ae277d2af4849e6831211ac28001a06";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(new parseGrimoire()).start();

    }

    public void viewGrimoire(View view){
        SharedPreferences sharedPreferences = getSharedPreferences("userData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("score", "");
        editor.commit();


        GrimoireContainer.getObject().setUserCardCollection(null);

        startActivity(new Intent(MainActivity.this, Themes.class));
    }

    public void login(View view){
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        EditText usernameText = (EditText) findViewById(R.id.username);
        String username = usernameText.getText().toString();
        usernameText.getText().clear();
        SharedPreferences sharedPreferences = getSharedPreferences("userData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (view.getId() == R.id.psButton){
            new loginTask().execute(new AsyncTaskWrapper(username, 2));
            editor.putInt("platform", 2);
            editor.commit();
        } else {
            new loginTask().execute(new AsyncTaskWrapper(username, 1));
            editor.putInt("platform", 1);
            editor.commit();
        }
        editor.putString("username", username);
        editor.commit();

    }

    private class AsyncTaskWrapper{
        String username;
        int platform;
        public AsyncTaskWrapper(String username, int platform){
            this.username = username;
            this.platform = platform;
        }
    }

    private class loginTask extends AsyncTask<AsyncTaskWrapper, AsyncTaskWrapper, JsonObject>{

        @Override
        protected JsonObject doInBackground(AsyncTaskWrapper... wrapper){
            try {
                URL url = new URL("https://www.bungie.net/platform/Destiny/" + wrapper[0].platform + "/Stats/GetMembershipIdByDisplayName/" + wrapper[0].username + "/");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("x-api-key", API_KEY);

                int responseCode = con.getResponseCode();

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();

                JsonParser parser = new JsonParser();
                JsonObject json = (JsonObject) (parser.parse(response.toString()));

                String memberId = json.get("Response").toString().replace("\"", "");
                SharedPreferences sharedPreferences = getSharedPreferences("userData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("memberId", memberId);
                editor.commit();


                url = new URL("https://www.bungie.net/d1/platform/Destiny/Vanguard/Grimoire/" + wrapper[0].platform + "/" + memberId + "/");
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("x-api-key", API_KEY);

                responseCode = con.getResponseCode();

                in = new BufferedReader(new InputStreamReader(con.getInputStream()));

                response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();

                json = (JsonObject) (parser.parse(response.toString()));

                return json;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(JsonObject json){
            if (json == null || !(json.get("ErrorCode").toString().equals("1"))) {
                Toast.makeText(MainActivity.this, "Incorrect Username or Platform", Toast.LENGTH_LONG).show();
                return;
            }
            json = json.getAsJsonObject("Response").getAsJsonObject("data");
            String score = json.get("score").toString();
            SharedPreferences sharedPreferences = getSharedPreferences("userData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            GrimoireContainer.getObject().setUserCardCollection(json.getAsJsonArray("cardCollection"));
            editor.putString("score", score);
            editor.commit();

            Intent i = new Intent(MainActivity.this, Themes.class);
            startActivity(i);

        }

    }

//    Runnable to parse Grimoire Json. Parsed in initial activity to improve performace
    private class parseGrimoire implements Runnable{

        @Override
        public void run(){
            StringBuilder sb = new StringBuilder();
            BufferedReader br;
            try {
                Resources res = getResources();
                InputStreamReader input = new InputStreamReader(res.openRawResource(R.raw.grimoire));
                br = new BufferedReader(input);
                String temp;
                while ((temp = br.readLine()) != null) {
                    sb.append(temp);
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("GRIMOIRE JSON PARSE", "Pre-Parse");

            JsonObject json = new JsonParser().parse(sb.toString()).getAsJsonObject();
            JsonArray themeCollection = json.getAsJsonObject("Response").getAsJsonArray("themeCollection");
            Log.d("GRIMOIRE JSON PARSE", "Parce Successful");

            GrimoireContainer.getObject().setThemeCollection(themeCollection);

            Log.d("GRIMOIRE JSON PARSE", "ThemeCollection set");
        }
    }
}
