package jawadbraick.destinygrimoire;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class Pages extends AppCompatActivity{
    List<String> adapterList;
    GrimoireContainer grimoire = GrimoireContainer.getObject();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pages);

        Thread t = new Thread(new Runnable(){
            @Override
            public void run(){
                adapterList = createList(GrimoireContainer.getObject().getPageCollection());
            }
        });
        t.start();

        HeaderGridView gridView = findViewById(R.id.gridview);

        LinearLayout header = (LinearLayout) getLayoutInflater().inflate(R.layout.pages_header, null);

        TextView label = (TextView) header.getChildAt(1);
        String theme = getIntent().getStringExtra("themeTag");
        theme = theme.substring(0,1).toUpperCase() + theme.substring(1);
        label.setText(theme);

        TextView scoreText = (TextView) header.findViewById(R.id.score);
        String score = getSharedPreferences("userData", Context.MODE_PRIVATE).getString("score", "");
        scoreText.setText(score);

        gridView.addHeaderView(header);
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        PagesAdapter pagesAdapter = new PagesAdapter(this, adapterList);
        gridView.setAdapter(pagesAdapter);

    }

    private List<String> createList(JsonArray pageCollection){
        ArrayList<String> pageNameList = new ArrayList<>();

        for(int i = 0; i < pageCollection.size(); i++){
            JsonObject page = (JsonObject) pageCollection.get(i);

            String name = page.get("pageName").getAsString();
            pageNameList.add(name);
        }

        return pageNameList;
    }

    public void showCard(View view){
        String page = view.getTag().toString();

        grimoire.setPage(page);

        Intent i = new Intent(Pages.this, Cards.class);
        i.putExtra("pageTag", page);
        startActivity(i);
    }

}
