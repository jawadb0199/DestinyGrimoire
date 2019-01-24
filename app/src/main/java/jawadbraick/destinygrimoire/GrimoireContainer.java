package jawadbraick.destinygrimoire;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class GrimoireContainer{
    private static volatile GrimoireContainer object;
    private JsonArray themeCollection;
    private JsonArray pageCollection;
    private JsonArray cardCollection;
    private JsonObject cardJson;
    private JsonArray userCardCollection;
    private String theme;
    private String page;
    private String card;


    private GrimoireContainer(){}

    public static GrimoireContainer getObject(){
        if (object == null){
            synchronized (GrimoireContainer.class) {
                if (object == null) {
                    object = new GrimoireContainer();
                }
            }
        }
        return object;
    }

//    Getters
    public JsonArray getThemeCollection(){
        return themeCollection;
    }

    public JsonArray getPageCollection(){
        return pageCollection;
    }

    public JsonArray getCardCollection(){
        return cardCollection;
    }

    public JsonObject getCardJson(){
        return cardJson;
    }
    public JsonArray getUserCardCollection(){
        return userCardCollection;
    }

//    Setters
    public void setTheme(String theme){
        this.theme = theme;
        setPageCollection();
    }
    public void setPage(String page){
        this.page = page;
        setCardCollection();
    }
    public void setCard(String card){
        this.card = card;
        setCardJson();
    }

    public void setThemeCollection(JsonArray themeCollection){
        if (this.themeCollection == null){
            this.themeCollection = themeCollection;
        }
    }
    private void setPageCollection(){
        pageCollection = getJsonObjectFromArray(themeCollection, "themeId", theme).getAsJsonArray("pageCollection");
    }
    private void setCardCollection(){
        cardCollection = getJsonObjectFromArray(pageCollection, "pageName", page).getAsJsonArray("cardCollection");
    }
    private void setCardJson(){
        cardJson = getJsonObjectFromArray(cardCollection, "cardId", card);
    }
    public void setUserCardCollection(JsonArray userCardCollection){
        this.userCardCollection = userCardCollection;
    }


    private JsonObject getJsonObjectFromArray(JsonArray array, String key, String value){
        Log.d("JSONARRAY", array.toString());
        for (JsonElement e: array) {
            JsonObject json = e.getAsJsonObject();
            if (json.get(key).getAsString().equals(value)){
                return json;
            }
        }
        return null;
    }
}
