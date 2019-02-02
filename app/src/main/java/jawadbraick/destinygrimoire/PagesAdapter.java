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
    private final int spacing = 40;

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

        int width =  (parent.getWidth()-spacing)/3;
        view.imageButtonPage.setImageBitmap(reduceImage(imgID, width));
        view.imageButtonPage.setTag(name);

        Log.d("GRID SOURCE", view.imageButtonPage.getDrawable().toString());

        return convertView;
    }

    private Bitmap reduceImage(int id, int reqWidth){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(activity.getResources(), id, options);

        boolean tabletSize = activity.getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            options.inSampleSize = calculateInSampleSize(options, reqWidth);
        } else {
            options.inSampleSize = 5;
        }

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(activity.getResources(), id, options);
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth){
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (width > reqWidth) {
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((width / inSampleSize) >= reqWidth) {
                inSampleSize++;
            }
        }

        return inSampleSize-1;
    }

    private int getImage(String name){
        String fileName = name.replaceAll(" & ", "_").replaceAll("[^a-zA-Z0-9_ ]", "").replaceAll(" ", "_").toLowerCase();
        return activity.getResources().getIdentifier(fileName, "drawable", activity.getPackageName());
    }
}
