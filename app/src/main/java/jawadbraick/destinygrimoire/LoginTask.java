package jawadbraick.destinygrimoire;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginTask extends AsyncTask<LoginTaskWrapper, LoginTaskWrapper, JsonObject>{
    private static final String API_KEY = "7ae277d2af4849e6831211ac28001a06";
    private Context context;

    public LoginTask(Context context){
        this.context = context;
    }

    @Override
    protected JsonObject doInBackground(LoginTaskWrapper... wrapper){
        try {
            URL url = new URL("https://www.bungie.net/platform/Destiny/" + wrapper[0].platform + "/Stats/GetMembershipIdByDisplayName/" + wrapper[0].username + "/");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("x-api-key", API_KEY);

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
            SharedPreferences sharedPreferences = context.getSharedPreferences("userData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("memberId", memberId);
            editor.commit();


            url = new URL("https://www.bungie.net/d1/platform/Destiny/Vanguard/Grimoire/" + wrapper[0].platform + "/" + memberId + "/");
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("x-api-key", API_KEY);

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
            Toast.makeText(context, "Incorrect Username or Platform", Toast.LENGTH_LONG).show();
            return;
        }
        json = json.getAsJsonObject("Response").getAsJsonObject("data");
        String score = json.get("score").toString();
        SharedPreferences sharedPreferences = context.getSharedPreferences("userData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        GrimoireContainer.getObject().setUserCardCollection(json.getAsJsonArray("cardCollection"));
        editor.putString("score", score);
        editor.commit();

        Intent i = new Intent(context, Themes.class);
        context.startActivity(i);

    }

}

class LoginTaskWrapper{
    String username;
    int platform;
    public LoginTaskWrapper(String username, int platform){
        this.username = username;
        this.platform = platform;
    }
}
