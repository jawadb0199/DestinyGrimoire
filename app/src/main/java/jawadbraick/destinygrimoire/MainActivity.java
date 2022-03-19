package jawadbraick.destinygrimoire;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(new parseGrimoire()).start();
    }

    public void openGrimoire(View view){
        SharedPreferences sharedPreferences = getSharedPreferences("userData", Context.MODE_PRIVATE);

        String username = sharedPreferences.getString("username", "");
        int platform = sharedPreferences.getInt("platform", -1);

        if((!username.isEmpty()) && platform != -1){
            new LoginTask(this).execute(new LoginTaskWrapper(username, platform));
        } else {
            startActivity(new Intent(MainActivity.this, GrimoireActivity.class));
        }
    }

    public void viewRecords(View view){
        startActivity(new Intent(MainActivity.this, LoreHomeActivity.class));
    }

    public void viewItems(View view){
        startActivity(new Intent(MainActivity.this, ItemsLoreActivity.class));
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

            JsonObject json = new JsonParser().parse(sb.toString()).getAsJsonObject();
            JsonArray themeCollection = json.getAsJsonObject("Response").getAsJsonArray("themeCollection");

            GrimoireContainer.getObject().setThemeCollection(themeCollection);
        }
    }

}
