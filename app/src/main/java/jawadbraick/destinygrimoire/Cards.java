package jawadbraick.destinygrimoire;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class Cards extends AppCompatActivity{
    private GridViewAdapter mAdapter;
    private ArrayList<String> CardIdList;
    private ArrayList<String> CardNameList;
    private HeaderGridView gridView;
    private GrimoireContainer grimoire;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);

        createLists();

        mAdapter = new GridViewAdapter(this, getApplicationContext(), CardIdList, CardNameList);

        // Set custom adapter to gridview
        gridView = (HeaderGridView) findViewById(R.id.gridview);

//        String pageName = getIntent().getStringExtra("pageId").toLowerCase();
        String pageName = "ExaltedHive";
        pageName = pageName.charAt(0) + pageName.substring(1).replace("[A-Z]", " ");
        gridView.addHeaderView(createTextViewHeader(pageName));
        gridView.setAdapter(mAdapter);

    }

    private void createLists(){
        grimoire = GrimoireContainer.getObject();
        JsonArray cardCollection = grimoire.getCardCollection();

        CardIdList = new ArrayList<String>();
        CardNameList = new ArrayList<String>();

        for (JsonElement e: cardCollection) {
            JsonObject j = e.getAsJsonObject();
            CardIdList.add(j.get("cardId").getAsString());
            CardNameList.add(j.get("cardName").getAsString());
        }
    }

    private TextView createTextViewHeader(String text){
        TextView header = new TextView(this);
        header.setText(text);
        header.setTextSize(35);
        header.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        header.setTextColor(getResources().getColor(R.color.lightText));
        header.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return header;
    }

    public void showCard(View view){
        String cardId = view.getTag().toString();
        grimoire.setCard(cardId);

        Log.d("CARDJSON", grimoire.getCardJson().toString());
        Intent i = new Intent(Cards.this, DisplayCard.class);
        startActivity(i);
    }

}
