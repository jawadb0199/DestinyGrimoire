package jawadbraick.destinygrimoire;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GrimoireActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grimoire);

    }

    public void viewGrimoire(View view){
        SharedPreferences sharedPreferences = getSharedPreferences("userData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("score", "");
        editor.commit();


        GrimoireContainer.getObject().setUserCardCollection(null);

        startActivity(new Intent(GrimoireActivity.this, Themes.class));
    }

    public void login(View view){
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

        EditText usernameText = (EditText) findViewById(R.id.username);
        String username = usernameText.getText().toString();
        usernameText.getText().clear();

        SharedPreferences.Editor editor = getSharedPreferences("userData", Context.MODE_PRIVATE).edit();
        editor.putString("username", username);
        editor.apply();

        if (view.getId() == R.id.psButton){
            new LoginTask(this).execute(new LoginTaskWrapper(username, 2));
            editor.putInt("platform", 2);
            editor.apply();
        } else {
            new LoginTask(this).execute(new LoginTaskWrapper(username, 1));
            editor.putInt("platform", 1);
            editor.apply();
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

            JsonObject json = new JsonParser().parse(sb.toString()).getAsJsonObject();
            JsonArray themeCollection = json.getAsJsonObject("Response").getAsJsonArray("themeCollection");

            GrimoireContainer.getObject().setThemeCollection(themeCollection);
        }
    }
}
