package jawadbraick.destinygrimoire;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class GridViewAdapter extends BaseAdapter{
    private ArrayList<String> cardIdList;
    private ArrayList<String> cardNameList;
    private Activity activity;
    private Context context;

    public GridViewAdapter(Activity activity, Context context, ArrayList<String> cardIdList, ArrayList<String> cardNameList) {
        super();
        this.cardIdList = cardIdList;
        this.cardNameList = cardNameList;
        this.activity = activity;
        this.context = context;
    }

    @Override
    public int getCount() {
        return cardIdList.size();
    }

    @Override
    public String getItem(int position) {
        return cardIdList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    private int getImage(String fileName){
        Resources resources = context.getResources();
        if (Character.isDigit(fileName.charAt(0))){
            fileName = 'c' + fileName;
        }
        return resources.getIdentifier(fileName, "drawable", context.getPackageName());
    }

    public class ViewHolder{
        public ImageButton imageButtonPage;
        public TextView pageText;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GridViewAdapter.ViewHolder view;
        LayoutInflater inflator = activity.getLayoutInflater();

        if(convertView==null) {
            view = new GridViewAdapter.ViewHolder();
            convertView = inflator.inflate(R.layout.cards_gridview, null);

            view.pageText = (TextView) convertView.findViewById(R.id.textView);
            view.imageButtonPage = (ImageButton) convertView.findViewById(R.id.imageButton);

            convertView.setTag(view);
        } else {
            view = (GridViewAdapter.ViewHolder) convertView.getTag();
        }

        view.pageText.setText(cardNameList.get(position));
        view.imageButtonPage.setImageResource(getImage(cardIdList.get(position)));
        if (cardIdList.get(position).equals("grimoire_cover")){
            view.imageButtonPage.setTag("");
        } else {
            view.imageButtonPage.setTag(cardIdList.get(position));
        }
        Log.d("GRID SOURCE", view.imageButtonPage.getDrawable().toString());

        return convertView;
    }

}
