package it.flatwhite.mytube.Adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import it.flatwhite.mytube.Model.Station;
import it.flatwhite.mytube.R;

/**
 * Created by Aaron on 06/12/2017.
 * flatwhite.it
 */

public class stationAdapter extends RecyclerView.Adapter<stationAdapter.StationViewHolder> {

    private List<Station> stationList;

    public stationAdapter(List<Station> stationList) {
        this.stationList = stationList;
    }

    @Override
    public StationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.station_list_row, parent, false);

        return new StationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(stationAdapter.StationViewHolder holder, int position) {

        holder.station.setText(stationList.get(position).getStation());

        if(position %2 == 1)
        {
            holder.station.setBackgroundColor(Color.parseColor("#4c4c4c"));
        }
        else
        {
            holder.station.setBackgroundColor(Color.parseColor("#003688"));
        }
    }

    @Override
    public int getItemCount() {
        return stationList.size();
    }

    public class StationViewHolder extends RecyclerView.ViewHolder implements View.OnContextClickListener {
        public TextView station;

        public StationViewHolder(View view) {
            super(view);
            station = (TextView) view.findViewById(R.id.station);
        }

        @Override
        public boolean onContextClick(View view) {

            return false;
        }
    }
}
