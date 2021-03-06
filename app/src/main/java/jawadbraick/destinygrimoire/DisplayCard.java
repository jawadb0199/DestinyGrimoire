package jawadbraick.destinygrimoire;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

public class DisplayCard extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_display_card);

            JsonObject cardJson = GrimoireContainer.getObject().getCardJson();

//        Set cardImage source and Description Border (Background)
            int cardImageFile = getImage(cardJson.get("cardId").toString());
            ImageView cardImage = findViewById(R.id.cardImage);
            cardImage.setImageResource(cardImageFile);
            ImageView borderBackground = findViewById(R.id.background);
            borderBackground.setImageResource(cardImageFile);

//        Set Card Name Text
            String cardName = cardJson.get("cardName").toString();
            cardName = cardName.substring(1, cardName.length() - 1);
            TextView cardNameText = findViewById(R.id.cardName);
            cardNameText.setText(cardName);


//        Set Card Intro Text and Attribution (if applicable)
            if (cardJson.get("cardIntro") != null) {
                String cardIntro = stripHtml(cardJson.get("cardIntro").toString());
//            String cardIntro = stripHtml(intro);
                cardIntro = cardIntro.substring(1, cardIntro.length() - 1);
                TextView cardIntroText = findViewById(R.id.cardIntro);
                cardIntroText.setText(cardIntro);
                cardIntroText.setVisibility(View.VISIBLE);
            }
            if (cardJson.get("cardIntroAttribution") != null) {
                String cardIntroAttribution = stripHtml(cardJson.get("cardIntroAttribution").toString());
                cardIntroAttribution = cardIntroAttribution.substring(1, cardIntroAttribution.length() - 1);
                TextView cardIntroAttributionText = findViewById(R.id.cardIntroAttribution);
                cardIntroAttributionText.setText(cardIntroAttribution);
                cardIntroAttributionText.setVisibility(View.VISIBLE);

            }

//        Set Card Description Text
            if (cardJson.get("cardDescription") != null) {
                String cardDescription = stripHtml(cardJson.get("cardDescription").toString());
                cardDescription = cardDescription.substring(1, cardDescription.length() - 1);
                TextView cardDescriptionText = findViewById(R.id.cardDescription);
                cardDescriptionText.setText(cardDescription);
            } else {
                findViewById(R.id.cardDescriptionBlock).setVisibility(View.GONE);
            }
        } catch (Exception e){
            Toast.makeText(this, "Error Loading Grimoire Card", Toast.LENGTH_LONG).show();
        }

    }
    @SuppressWarnings("deprecation")
    private String stripHtml(String html) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return String.valueOf(Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY));
        } else {
            return String.valueOf(Html.fromHtml(html));
        }
    }
    private int getImage(String fileName){
        Resources resources = getResources();
        if (Character.isDigit(fileName.charAt(0))){
            fileName = 'c' + fileName;
        }
        return resources.getIdentifier(fileName, "drawable", getPackageName());
    }
}
