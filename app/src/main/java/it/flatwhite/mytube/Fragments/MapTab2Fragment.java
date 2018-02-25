package it.flatwhite.mytube.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import it.flatwhite.mytube.R;
// Library to add 'pinch-to-zoom' on images
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Aaron on 13/12/2017.
 * flatwhite.it
 */

public class MapTab2Fragment extends Fragment {

    ImageView imageView;
    PhotoViewAttacher miaFoto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.maptab2_fragment,container,false);

        imageView = (ImageView) view.findViewById(R.id.nightTubeMap);
        miaFoto = new PhotoViewAttacher(imageView);

        return view;
    }
}
