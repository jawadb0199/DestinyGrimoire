package jawadbraick.destinygrimoire;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder>{
    private LayoutInflater inflater;
    List<RecordInfo> recordInfoList = Collections.emptyList();

    public RecordAdapter(Context context, List<RecordInfo> recordInfoList){
        super();
        inflater = LayoutInflater.from(context);
        this.recordInfoList = recordInfoList;
    }


    @Override
    public RecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = inflater.inflate(R.layout.recyclerview_row, parent, false);
        RecordViewHolder holder = new RecordViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecordViewHolder holder, int position){
        RecordInfo current = recordInfoList.get(position);

        holder.recordIcon.setImageResource(current.getIconId());
        holder.recordText.setText(current.getName());
    }

    @Override
    public int getItemCount(){
        return recordInfoList.size();
    }

    class RecordViewHolder extends RecyclerView.ViewHolder{
        ImageView recordIcon;
        TextView recordText;

        public RecordViewHolder(View itemView){
            super(itemView);
            recordIcon = itemView.findViewById(R.id.recordIcon);
            recordText = itemView.findViewById(R.id.recordText);
        }
    }
}
