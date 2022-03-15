package jawadbraick.destinygrimoire;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder>{
    private LayoutInflater inflater;
    List<RecordInfo> recordData;
    private FragmentManager fm;

    public RecordAdapter(Context context, List<RecordInfo> recordData, FragmentManager fm){
        super();
        this.inflater = LayoutInflater.from(context);
        this.recordData = recordData;
        this.fm = fm;
    }


    @Override
    public RecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = inflater.inflate(R.layout.record_row, parent, false);
        RecordViewHolder holder = new RecordViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecordViewHolder holder, int position){
        RecordInfo current = recordData.get(position);

        holder.recordIcon.setImageResource(current.getIconId());
        holder.recordText.setText(current.getRecordName());
    }

    @Override
    public int getItemCount(){
        return recordData.size();
    }

    class RecordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView recordIcon;
        TextView recordText;


        public RecordViewHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            recordIcon = itemView.findViewById(R.id.recordIcon);
            recordText = itemView.findViewById(R.id.recordText);
        }

        @Override
        public void onClick(View view){
            RecordInfo info = recordData.get(getAdapterPosition());

            LoreFragment frag = new LoreFragment();
            frag.setInfo(info);
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.add(R.id.loreHomeActivity, frag, "LoreFragment").addToBackStack(null).commit();
        }
    }
}


