package jawadbraick.destinygrimoire;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class Pages extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        String themeTag = getIntent().getStringExtra("themeTag");
        int themeLayoutId = getResources().getIdentifier(themeTag, "layout", getPackageName());
        setContentView(themeLayoutId);

        SharedPreferences sharedPreferences = getSharedPreferences("userData", Context.MODE_PRIVATE);

        TextView scoreText = findViewById(R.id.score);
        scoreText.setText(sharedPreferences.getString("score", ""));
    }

    public void openCards(View view){
        String page = view.getTag().toString();

        GrimoireContainer grimoire = GrimoireContainer.getObject();
        grimoire.setPage(page);

        Intent i = new Intent(Pages.this, Cards.class);
        i.putExtra("pageTag", page);
        startActivity(i);
    }

}
