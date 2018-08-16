package jawadbraick.destinygrimoire;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String API_KEY = "7ae277d2af4849e6831211ac28001a06";
    private int memberId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    public void login(View view){
        String username = ((EditText)this.findViewById(R.id.username)).getText().toString();
        SharedPreferences sharedPreferences = getSharedPreferences("userData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);

        if (view.getId() == R.id.psButton){
            editor.putInt("platform", 2);
            editor.commit();
            new MemberIdTask().execute("https://www.bungie.net/platform/Destiny/2/Stats/GetMembershipIdByDisplayName/" + username + "/");
        } else {
            editor.putInt("platform", 1);
            editor.commit();
            new MemberIdTask().execute("https://www.bungie.net/platform/Destiny/1/Stats/GetMembershipIdByDisplayName/" + username + "/");
        }

    }
    public void viewGrimoire(View view){
        SharedPreferences sharedPreferences = getSharedPreferences("userData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("score", "");
        editor.commit();

        startActivity(new Intent(MainActivity.this, Themes.class));
    }

    private class MemberIdTask extends AsyncTask<String, String, JsonObject>{

        @Override
        protected JsonObject doInBackground(String... url){
            try{
                URL obj = new URL(url[0]);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("x-api-key", API_KEY);

                int responseCode = con.getResponseCode();

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                String response = "";

                while ((inputLine = in.readLine()) != null) {
                    response += inputLine;
                }

                in.close();

                JsonObject json = (JsonObject) (new JsonParser().parse(response));

                return json;

            } catch (IllegalStateException i) {
                return null;
            } catch (NullPointerException n){
                return null;
            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(JsonObject json){
            if (json.get("Response").toString().equals("0")){
                return;
            }
            String memberId = json.get("Response").toString().replace("\"", "");
            SharedPreferences sharedPreferences = getSharedPreferences("userData", Context.MODE_PRIVATE);
            int platform = sharedPreferences.getInt("platform", 2);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("memberId", memberId);
            editor.commit();
            new CardCollectionTask().execute("https://www.bungie.net/d1/platform/Destiny/Vanguard/Grimoire/" + platform + "/" + memberId + "/");
        }

    }

    private class  CardCollectionTask extends AsyncTask<String, String, JsonObject>{
        @Override
        protected JsonObject doInBackground(String... url){
            try{
                URL obj = new URL(url[0]);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("x-api-key", API_KEY);

                int responseCode = con.getResponseCode();

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                String response = "";

                while ((inputLine = in.readLine()) != null) {
                    response += inputLine;
                }

                in.close();

                JsonElement parsed = new JsonParser().parse(response);
                JsonObject json = parsed.getAsJsonObject();

                return json;
            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(JsonObject j){
            j = j.getAsJsonObject("Response");
            String score = j.getAsJsonObject("data").get("score").toString();

            SharedPreferences sharedPreferences = getSharedPreferences("userData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("score", score);
            editor.putString("userCardCollection", j.toString());
            editor.commit();

            Intent i = new Intent(MainActivity.this, Themes.class);
            startActivity(i);
        }

    }
}
