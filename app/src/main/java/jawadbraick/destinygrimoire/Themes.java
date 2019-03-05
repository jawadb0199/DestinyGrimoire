package jawadbraick.destinygrimoire;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Themes extends AppCompatActivity{
    private GrimoireContainer grimoire;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_themes);

        String score = getSharedPreferences("userData", Context.MODE_PRIVATE ).getString("score", "");

        if(!score.isEmpty()){
            TextView scoreText = findViewById(R.id.score);
            scoreText.setText(score);
            findViewById(R.id.buffer).setVisibility(View.GONE);
            findViewById(R.id.signOutButton).setVisibility(View.VISIBLE);
        }

    }

    public void openPages(View view){
        try {
            String theme = capitalize(view.getTag().toString());

            grimoire = GrimoireContainer.getObject();
            while (grimoire.getThemeCollection() == null) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                grimoire = GrimoireContainer.getObject();
            }
            grimoire.setTheme(theme);

            Intent i = new Intent(Themes.this, Pages.class);
            String themeTag = view.getTag().toString();
            i.putExtra("themeTag", themeTag);

            startActivity(i);
        } catch (Exception e){
            Toast.makeText(this, "Error Opening Grimoire Theme", Context.MODE_PRIVATE).show();
        }
    }

    public void signOut(View view){
        SharedPreferences.Editor editor = getSharedPreferences("userData", Context.MODE_PRIVATE ).edit();
        editor.putInt("platform", -1);
        editor.putString("username", "");
        editor.putString("score", "");
        editor.apply();

        Intent i = new Intent(Themes.this, GrimoireActivity.class);
        Themes.this.finish();
        startActivity(i);
    }

    private String capitalize(String str){
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

}
