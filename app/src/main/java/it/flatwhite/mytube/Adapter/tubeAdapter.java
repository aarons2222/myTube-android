package it.flatwhite.mytube.Adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import it.flatwhite.mytube.Fragments.current_info_frag1;
import it.flatwhite.mytube.Model.dbModel;
import it.flatwhite.mytube.R;


/**
 * Created by Aaron on 30/11/2017.
 * flatwhite.it
 */
public class tubeAdapter extends RecyclerView.Adapter<tubeAdapter.ViewHolder> {

    private String e;

    static   List<dbModel> dbList;
    static current_info_frag1 context;
    public tubeAdapter(current_info_frag1 context, List<dbModel> dbList){
        this.dbList = new ArrayList<dbModel>();
        this.context = context;
        this.dbList = dbList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.tube_list_row, null);

        // create ViewHolder
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.title.setText(dbList.get(position).getLineName());
        holder.rvLineStatus.setText(dbList.get(position).getLineStatus());
        // array containing HEX codes for recycler row backgrounds
        Integer[] colors = {Color.rgb(179, 99, 5),Color.rgb(227, 32, 23),Color.rgb(255, 211, 0),Color.rgb(0, 120, 42),Color.rgb(0, 164, 167),Color.rgb(243, 169, 187),Color.rgb(160, 165, 169),Color.rgb(238, 124, 14),Color.rgb(155, 0, 86),Color.rgb(0, 0, 0),Color.rgb(0, 54, 136),Color.rgb(113, 86, 165),Color.rgb(0, 152, 212),Color.rgb(149, 205, 186)};
        holder.title.setBackgroundColor(colors[position]);
    }

    @Override
    // get size of lit
    public int getItemCount() {
        return dbList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView rvLineStatus;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            title = (TextView) itemLayoutView
                    .findViewById(R.id.tvTubeName);

            rvLineStatus = (TextView) itemLayoutView
                    .findViewById(R.id.tvTubeStatus);

        }
    }
}
