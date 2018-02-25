package it.flatwhite.mytube.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.Manifest;
import android.widget.Toast;

import it.flatwhite.mytube.R;


/**
 * Created by Aaron on 30/11/2017.
 * flatwhite.it
 */


public class current_info_frag3 extends Fragment {
    public static current_info_frag3 newInstance() {
        current_info_frag3 fragment = new current_info_frag3();
        return fragment;
    }

    private  static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;
    private Button dial;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set title and Background colour of ActionBar
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.item_3);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4c4c4c")));    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_current_info_frag3, container, false);


        // call button
        dial = (Button) view.findViewById(R.id.dial);

        dial.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                // Number to pass to the phone app and dial
                String phoneNumber = "07447000599";

                // make sure there is a number to call
                if (!TextUtils.isEmpty(phoneNumber)){

                    // check if app has permission to make a call (launch phone app).
                    if (checkPermission(Manifest.permission.CALL_PHONE)){
                        dial.setEnabled(true);

                        final String dial = "tel:" + phoneNumber;

                        /*
                        * Show the user a dialog, informing them that the app is about to
                        * launch the phone app and make a call
                        * */
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Warning! - This is not a FREE call!")
                                .setMessage("\nThe cost of this call will be deducted from your calltime allowance. \n \n Do you wish to proceed with this call?")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int whichButton) {

                                        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                                    }})
                                .setNegativeButton(android.R.string.no, null).show();

                    } else {
                        Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //Toast.makeText(getContext(), "hello", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dial.setEnabled(true);
        if (checkPermission(Manifest.permission.CALL_PHONE)){
            // if CALL permission is granted make button pressable
            dial.setEnabled(true);
        }else{
            // if CALL permission is denied, freeze call button and request permission from the user.
            dial.setEnabled(false);
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CALL_PHONE}, MAKE_CALL_PERMISSION_REQUEST_CODE);
        }
        return view;
    }

    // check if permissions have been granted.
    private boolean checkPermission(String permission)
    {
        return ContextCompat.checkSelfPermission(getActivity(), permission) == PackageManager.PERMISSION_GRANTED;
    }

    // Request permission from the user.
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case MAKE_CALL_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED )) {
                    dial.setEnabled(true);
                   // Toast.makeText(getActivity(), "click button", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }
}