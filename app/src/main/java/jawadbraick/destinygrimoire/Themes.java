package jawadbraick.destinygrimoire;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Themes extends AppCompatActivity{
    private Thread parseGrimoireThread;
    private JsonArray themeCollection;
    private GrimoireContainer grimoire;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        parseGrimoireThread = new Thread(new parseGrimoire());
        parseGrimoireThread.start();

        setContentView(R.layout.activity_themes);

        SharedPreferences sharedPreferences = getSharedPreferences("userData", Context.MODE_PRIVATE );

        TextView scoreText = findViewById(R.id.score);
        scoreText.setText(sharedPreferences.getString("score", ""));

    }

    public void openPages(View view){
        String theme = capitalize(view.getTag().toString());

        try {
            parseGrimoireThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        grimoire.setTheme(theme);

        Intent i = new Intent(Themes.this, Pages.class);
        String themeTag = view.getTag().toString();
        i.putExtra("themeTag",themeTag);

        startActivity(i);
    }

    private String capitalize(String str){
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private class parseGrimoire implements Runnable{

        @Override
        public void run(){
            String s = "";
            BufferedReader br;
            try {
                Resources res = getResources();
                InputStreamReader input = new InputStreamReader(res.openRawResource(R.raw.grimoire));
                br = new BufferedReader(input);
                String temp;
                while ((temp = br.readLine()) != null) {
                    s += temp;
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("GRIMOIRE JSON PARSE", "Pre-Parse");

            JsonObject json = new JsonParser().parse(s).getAsJsonObject();
            themeCollection = json.getAsJsonObject("Response").getAsJsonArray("themeCollection");
            Log.d("GRIMOIRE JSON PARSE", "Parce Successful");

            grimoire = GrimoireContainer.getObject();
            grimoire.setThemeCollection(themeCollection);

            Log.d("GRIMOIRE JSON PARSE", "Waiting for theme selection . . .");
        }
    }

}
