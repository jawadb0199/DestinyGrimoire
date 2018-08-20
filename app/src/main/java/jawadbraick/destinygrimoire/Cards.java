package jawadbraick.destinygrimoire;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Cards extends AppCompatActivity{
    private GridViewAdapter mAdapter;
    private ArrayList<String> cardIdList;
    private ArrayList<String> cardNameList;
    private ArrayList<String> userCardIdList;
    private ArrayList<String> userCardNameList;
    private HeaderGridView gridView;
    private GrimoireContainer grimoire;
    private JsonArray cardCollection;
    private JsonArray userCardCollection;
    private int userCardCollectionStartIndex;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);

        grimoire = GrimoireContainer.getObject();
        cardCollection = grimoire.getCardCollection();
        userCardCollection = grimoire.getUserCardCollection();

        int firstCardId = Integer.parseInt(cardCollection.get(0).getAsJsonObject().get("cardId").toString());
         userCardCollectionStartIndex = getStartIndex(firstCardId,0, userCardCollection.size()-1);

        createLists();

        mAdapter = new GridViewAdapter(this, getApplicationContext(), userCardIdList, userCardNameList);

        // Set custom adapter to gridview
        gridView = (HeaderGridView) findViewById(R.id.gridview);

        String pageName = getIntent().getStringExtra("pageTag").toLowerCase();
//        String pageName = "ExaltedHive";
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
        cardCollection = grimoire.getCardCollection();

        userCardIdList = new ArrayList<>();
        userCardNameList = new ArrayList<>();

//        int grimoireIndex = 0;
//        int userIndex = userCardCollectionStartIndex;
//        int lockedCards = 0;
//        while (grimoireIndex < cardCollection.size()) {
//            // code for creating lists
//            JsonObject grimoireCard = cardCollection.get(grimoireIndex).getAsJsonObject();
//            JsonObject userCard = userCardCollection.get(userIndex).getAsJsonObject();
//
//            String grimoireCardId = grimoireCard.get("cardId").getAsString();
//            String userCardId = userCard.get("cardId").getAsString();
//
//            if (userCardId.equals(grimoireCardId)) {
//                userCardIdList.add(grimoireCardId);
//                userCardNameList.add(grimoireCard.get("cardName").getAsString());
//                userIndex++;
//            } else {
//                lockedCards++;
//            }
//            grimoireIndex++;
//        }
//
//        while (lockedCards > 0) {
//            userCardIdList.add("grimoire_cover");
//            userCardNameList.add("");
//            lockedCards--;
//        }
        for (int i = 0; i < cardCollection.size(); i++) {
            JsonObject grimoireCard = cardCollection.get(i).getAsJsonObject();
            String grimoireCardId = grimoireCard.get("cardId").getAsString();

            for (int j = userCardCollectionStartIndex; j < userCardCollection.size(); j++) {
                JsonObject userCard = userCardCollection.get(j).getAsJsonObject();
                String userCardId = userCard.get("cardId").getAsString();

                if (userCardId.equals(grimoireCardId)){
                    userCardIdList.add(grimoireCardId);
                    userCardNameList.add(grimoireCard.get("cardName").getAsString());
                    break;
                }
            }
        }
        for (int i = userCardIdList.size(); i < cardCollection.size(); i++) {
            userCardIdList.add("grimoire_cover");
            userCardNameList.add("");

        }


        new Thread(new Runnable(){
            @Override
            public void run(){
                cardIdList = new ArrayList<>();
                cardNameList = new ArrayList<>();

                for (JsonElement e: cardCollection) {
                    JsonObject j = e.getAsJsonObject();
                    cardIdList.add(j.get("cardId").getAsString());
                    cardNameList.add(j.get("cardName").getAsString());
                }
            }
        }).start();
    }

    public void toggleLockedCards(View view){
        CheckBox checkBox = (CheckBox) view;
        if (checkBox.isChecked()){
            mAdapter = new GridViewAdapter(this, getApplicationContext(), cardIdList, cardNameList);
            gridView.setAdapter(mAdapter);
        } else {
            mAdapter = new GridViewAdapter(this, getApplicationContext(), userCardIdList, userCardNameList);
            gridView.setAdapter(mAdapter);
        }
    }

    public void showCard(View view){
        String cardId = view.getTag().toString();
        if (cardId.isEmpty()){
            Toast.makeText(Cards.this, "You have not unlocked this card yet", Toast.LENGTH_SHORT).show();
            return;
        }
        grimoire.setCard(cardId);

        Log.d("CARDJSON", grimoire.getCardJson().toString());
        Intent i = new Intent(Cards.this, DisplayCard.class);
        startActivity(i);
    }
//    Binary Search
    private int getStartIndex(int cardId, int start, int end){
        if (start <= end) {
            int middle = end+(start-end)/2;
            int card = Integer.parseInt(userCardCollection.get(middle).getAsJsonObject().get("cardId").toString());

            if (card == cardId) {
                return middle;
            } else if (cardId < card){
                return getStartIndex(cardId, start, middle-1);
            } else {
                return getStartIndex(cardId, middle+1, end);
            }
        } else {
            return 0;
        }
    }


}
