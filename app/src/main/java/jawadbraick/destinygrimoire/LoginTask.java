package jawadbraick.destinygrimoire;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

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

            InputStream input;
            try{
                input = con.getInputStream();
            } catch (UnknownHostException u){
                JsonObject jsonError =  new JsonObject();
                jsonError.addProperty("ErrorCode", "No Internet Connection");
                return jsonError;
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
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

            try{
                input = con.getInputStream();
            } catch (UnknownHostException u){
                JsonObject jsonError =  new JsonObject();
                jsonError.addProperty("ErrorCode", "No Internet Connection");
                return jsonError;
            }
            in = new BufferedReader(new InputStreamReader(input));

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
        SharedPreferences sharedPreferences = context.getSharedPreferences("userData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String username = sharedPreferences.getString("username", "");
        editor.putString("username", "");
        editor.apply();

        if (json == null){
            Toast.makeText(context, "Couldn't Get Grimoire Information", Toast.LENGTH_LONG).show();
            return;
        } else if(json.get("ErrorCode").toString().equals("\"No Internet Connection\"")){
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_LONG).show();
            return;
        } else if (!(json.get("ErrorCode").toString().equals("1"))) {
            Toast.makeText(context, "Incorrect Username or Platform", Toast.LENGTH_LONG).show();
            return;
        }

        json = json.getAsJsonObject("Response").getAsJsonObject("data");
        String score = json.get("score").toString();
        GrimoireContainer.getObject().setUserCardCollection(json.getAsJsonArray("cardCollection"));

        editor.putString("username", username);
        editor.apply();

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
