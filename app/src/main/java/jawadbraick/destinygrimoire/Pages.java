package jawadbraick.destinygrimoire;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class Pages extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        String themeTag = getIntent().getStringExtra("themeTag");
        int themeLayoutId = getResources().getIdentifier(themeTag, "layout", getPackageName());
        setContentView(themeLayoutId);
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
