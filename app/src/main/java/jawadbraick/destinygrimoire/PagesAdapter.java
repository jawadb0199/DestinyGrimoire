package jawadbraick.destinygrimoire;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class PagesAdapter extends BaseAdapter{
    private List<String> pageNameList;
    private Activity activity;

    public PagesAdapter(Activity activity, List<String> pageNameList) {
        this.pageNameList = pageNameList;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return pageNameList.size();
    }

    @Override
    public String getItem(int position) {
        return pageNameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public class ViewHolder{
        public ImageButton imageButtonPage;
        public TextView pageText;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PagesAdapter.ViewHolder view;
        LayoutInflater inflator = activity.getLayoutInflater();

        if(convertView==null) {
            view = new PagesAdapter.ViewHolder();
            convertView = inflator.inflate(R.layout.cards_gridview, null);

            view.pageText = (TextView) convertView.findViewById(R.id.textView);
            view.imageButtonPage = (ImageButton) convertView.findViewById(R.id.imageButton);

            convertView.setTag(view);
        } else {
            view = (PagesAdapter.ViewHolder) convertView.getTag();
        }

        String name = pageNameList.get(position);
        view.pageText.setText(name);
        int imgID = getImage(name);

        view.imageButtonPage.setImageBitmap(reduceImage(imgID));
        view.imageButtonPage.setTag(name);

        Log.d("GRID SOURCE", view.imageButtonPage.getDrawable().toString());

        return convertView;
    }

    private Bitmap reduceImage(int id){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = 3;
        return BitmapFactory.decodeResource(activity.getResources(), id, options);
    }

    private int getImage(String name){
        String fileName = name.replaceAll(" & ", "_").replaceAll("[^a-zA-Z0-9_ ]", "").replaceAll(" ", "_").toLowerCase();
        return activity.getResources().getIdentifier(fileName, "drawable", activity.getPackageName());
    }
}
