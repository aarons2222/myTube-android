package it.flatwhite.mytube.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
// project imports
import it.flatwhite.mytube.Model.dbModel;
import it.flatwhite.mytube.R;
import it.flatwhite.mytube.Story;

/**
 * Created by Aaron on 30/11/2017.
 * flatwhite.it
 */

public class newsAdapter extends RecyclerView.Adapter<newsAdapter.ViewHolder> {

    static List<dbModel> dbList;
    static Context context;
    public newsAdapter(Context context, List<dbModel> dbList){
        this.dbList = new ArrayList<dbModel>();
        this.context = context;
        this.dbList = dbList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.articles_list_row, null);

        // create ViewHolder
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.title.setText(dbList.get(position).getTitle());
        holder.desc.setText(dbList.get(position).getDesc());

        //Alternates Row Colour on recycler view
        if(position %2 == 1)
        {
            holder.title.setBackgroundColor(Color.parseColor("#4c4c4c"));
        }
        else
        {
            holder.title.setBackgroundColor(Color.parseColor("#003688"));
        }
    }

    @Override
    public int getItemCount() {
        return dbList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

        public TextView title;
        public TextView desc;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            title = (TextView) itemLayoutView
                    .findViewById(R.id.tvTubeName);

            desc = (TextView) itemLayoutView
                    .findViewById(R.id.rvDesc);

            itemLayoutView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context,Story.class);

            Bundle extras = new Bundle();
            extras.putInt("position",getAdapterPosition());
            intent.putExtras(extras);

            context.startActivity(intent);
        }
    }
}

