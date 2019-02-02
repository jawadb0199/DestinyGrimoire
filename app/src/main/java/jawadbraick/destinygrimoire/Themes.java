package jawadbraick.destinygrimoire;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonArray;

public class Themes extends AppCompatActivity{
    private Thread parseGrimoireThread;
    private JsonArray themeCollection;
    private GrimoireContainer grimoire;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_themes);

        SharedPreferences sharedPreferences = getSharedPreferences("userData", Context.MODE_PRIVATE );

        TextView scoreText = findViewById(R.id.score);
        scoreText.setText(sharedPreferences.getString("score", ""));

    }

    public void openPages(View view){
        String theme = capitalize(view.getTag().toString());

        GrimoireContainer grimoire = GrimoireContainer.getObject();
        while (grimoire.getThemeCollection() == null){
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            grimoire = GrimoireContainer.getObject();
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

}
