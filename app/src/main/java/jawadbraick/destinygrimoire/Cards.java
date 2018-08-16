package jawadbraick.destinygrimoire;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.w3c.dom.Text;

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

        LayoutInflater inflater = this.getLayoutInflater();
        LinearLayout header = (LinearLayout) inflater.inflate(R.layout.gridview_header, null);

        TextView label = (TextView) header.getChildAt(1);
        label.setText(pageName);

        TextView score = (TextView) ((ViewGroup) header.getChildAt(2)).getChildAt(0);
        SharedPreferences sharedPreferences = getSharedPreferences("userData", Context.MODE_PRIVATE);
        score.setText(sharedPreferences.getString("score", ""));

        gridView.addHeaderView(header);
        gridView.setAdapter(mAdapter);

    }

    private void createLists(){
        grimoire = GrimoireContainer.getObject();
        JsonArray cardCollection = grimoire.getCardCollection();

        CardIdList = new ArrayList<>();
        CardNameList = new ArrayList<>();

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
