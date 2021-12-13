package jawadbraick.destinygrimoire;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class GridViewAdapter extends BaseAdapter{
    private List<String> cardIdList;
    private List<String> cardNameList;
    private Activity activity;
    private final int spacing = 40;

    public GridViewAdapter(Activity activity, List<String> cardIdList, List<String> cardNameList) {
        super();
        this.cardIdList = cardIdList;
        this.cardNameList = cardNameList;
        this.activity = activity;
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
        Resources resources = activity.getResources();
        if (Character.isDigit(fileName.charAt(0))){
            fileName = 'c' + fileName;
        }
        return resources.getIdentifier(fileName, "drawable", activity.getPackageName());
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
        int imgID = getImage(cardIdList.get(position));



        if (cardIdList.get(position).equals("blank")){
            view.imageButtonPage.setTag("");
            view.imageButtonPage.setImageResource(imgID);
        } else {

            int width =  (parent.getWidth()-spacing)/3;
            view.imageButtonPage.setImageBitmap(reduceImage(imgID, width));
            view.imageButtonPage.setTag(cardIdList.get(position));
        }

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
            options.inSampleSize = 4;
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
}
