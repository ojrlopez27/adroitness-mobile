package edu.cmu.adroitness.client.services.red5streaming.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import edu.cmu.adroitness.R;
import edu.cmu.adroitness.client.services.red5streaming.model.Red5ConfigVO;

public class SettingsDialogFragment extends DialogFragment {


    private AppState state;
    private OnFragmentInteractionListener mListener;
    private Spinner resolutionPicker ;
    private ArrayAdapter adapter;
    public static int defaultResolution = 0;
    public static int selectedResolution=0;
    public static int bitRate = 56;
    public static boolean isResolutionChanged=false;
    private SharedPreferences preferences;

    public static SettingsDialogFragment newInstance(AppState state) {
        SettingsDialogFragment fragment = new SettingsDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.state = state;
        return fragment;
    }

    /***
     * Set resolution dropdpwn and attach to an adapter
     * @param list
     */
    public void setSpinnerAdapter(ArrayAdapter list) {
        this.adapter=list;
        defaultResolution=-1;
        boolean has352 = false;
        boolean has160 = false;

        int c = adapter.getCount();
        for(int j=0; j < c; j++){
            String rez = String.valueOf(adapter.getItem(j));
            if("352x288".equals(rez)) {
                defaultResolution=j;//not ideal but...
            }
            else  if("160x120".equals(rez)&& defaultResolution<0) {
                //  defaultResolution=j;//not good enough...
            }
            else  if("176x144".equals(rez)&& defaultResolution<0) {
                //  defaultResolution=j;//not good enough...
            }

            if("320x240".equals(rez)) {
                defaultResolution=j;//perfect
                break;
            }

        }

        Log.d("publisher", "setting default resolution "+defaultResolution);

        if(defaultResolution < 0) {
            defaultResolution=0;
            Log.e("publisher","no currently supported resolution");
        }

        if(resolutionPicker!=null) {
            resolutionPicker.setAdapter(adapter);
            resolutionPicker.setSelection(defaultResolution);
            resolutionPicker.setOnItemSelectedListener(getItemSelectedHandlerForResolution());
            if(Red5StreamingActivity.selected_item!=null) {
                resolutionPicker.setSelection(adapter.getPosition(Red5StreamingActivity.selected_item));
            }
        }
    }

    public SettingsDialogFragment() {

    }

    /***
     * Returns the edit text resource requested for
     * @param v
     * @param id
     * @return
     */
    private EditText getField(View v, int id) {
        return (EditText) v.findViewById(id);
    }

    private String getPreferenceValue(int id) {
        return getResources().getString(id);
    }

    /***
     * The Red5 Config settings are saved to Preferences.
     * Updates current instance of Red5ConfigVO.
     * @param v
     */
    private void saveSettings(View v) {
         preferences = getActivity().getApplicationContext()
                 .getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        EditText host = getField(v, R.id.settings_host);
        EditText port = getField(v, R.id.settings_port);
        EditText app = getField(v, R.id.settings_appname);
        EditText name = getField(v, R.id.settings_streamname);

        editor.putString(getPreferenceValue(R.string.preference_host), host.getText().toString());
        editor.putInt(getPreferenceValue(R.string.preference_port),
                Integer.parseInt(port.getText().toString()));
        editor.putString(getPreferenceValue(R.string.preference_app), app.getText().toString());
        editor.putString(getPreferenceValue(R.string.preference_name), name.getText().toString());
        CheckBox cb = (CheckBox)v.findViewById(R.id.settings_audio);
        CheckBox cbv = (CheckBox)v.findViewById(R.id.settings_video);
        if(state == AppState.PUBLISH) {

            EditText br = getField(v, R.id.settings_bitrate);
            bitRate = Integer.valueOf(br.getText().toString().trim());
            editor.putInt(getPreferenceValue(R.string.preference_bitrate),bitRate);
            editor.putBoolean(getPreferenceValue(R.string.preference_audio), cb.isChecked());
            editor.putBoolean(getPreferenceValue(R.string.preference_video), cbv.isChecked());
        }

        editor.commit();
        ((Red5StreamingActivity)getActivity()).getViewHelper().setConfigVO(host.getText().toString(),
        Integer.parseInt(port.getText().toString()),
        app.getText().toString(), name.getText().toString(),
                bitRate, cb.isChecked(), cbv.isChecked());

        Red5StreamingActivity.logTxtView.append(String.format("Red5 Settings Saved!\n"));
    }

    private void showUserSettings(View v) {

        EditText host = getField(v, R.id.settings_host);
        EditText port = getField(v, R.id.settings_port);
        EditText app = getField(v, R.id.settings_appname);
        EditText name = getField(v, R.id.settings_streamname);
        EditText bitrate = getField(v, R.id.settings_bitrate);

        Red5ConfigVO config = ((Red5StreamingActivity)getActivity()).getViewHelper().getConfigVO();
        host.setText( config.getHost());
        name.setText(config.getName());
        app.setText(config.getApp());
        port.setText(String.valueOf(config.getPort()));
        bitrate.setText(String.valueOf(config.getBitrate()));
        CheckBox cb = (CheckBox)v.findViewById(R.id.settings_audio);
        CheckBox cbv = (CheckBox)v.findViewById(R.id.settings_video);
        cb.setChecked(config.getAudio());
        cbv.setChecked(config.getVideo());
        if(isResolutionChanged) {
            resolutionPicker.setSelection(selectedResolution);
        }
        switch (state) {
            case PUBLISH:

        }
    }

    /***
     * Save resolution selected
     * @return
     */
    private AdapterView.OnItemSelectedListener getItemSelectedHandlerForResolution(){
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("publisher"," selected item "+String.valueOf(adapterView.getSelectedItem()
                        +"  i:" +i+"  l :"+l));
                Red5StreamingActivity.selected_item = String.valueOf(adapterView.getSelectedItem());
                Red5StreamingActivity.preferedResolution = adapterView.getSelectedItemPosition();
                selectedResolution = i;
                isResolutionChanged = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View v =
                inflater.inflate(R.layout.services_red5_settings_fragment_dialog, null, false);
        resolutionPicker = (Spinner) v.findViewById(R.id.resolutionPicker);
        if(adapter!=null) {
            resolutionPicker.setAdapter(adapter);

            resolutionPicker.setSelection(defaultResolution);
            resolutionPicker.setOnItemSelectedListener(getItemSelectedHandlerForResolution());
        }

        ViewGroup streamSettings = (ViewGroup) v.findViewById(R.id.subscribe_settings);
        ViewGroup publishSettings = (ViewGroup) v.findViewById(R.id.publishing_settings);

        switch (state) {

        }

        ContextThemeWrapper ctx = new ContextThemeWrapper(getActivity(), R.style.AppTheme );
        AlertDialog dialog =  new AlertDialog.Builder(ctx)
                .setView(v)
                .setPositiveButton("DONE", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        saveSettings(v);
                        mListener.onSettingsDialogClose();
                        dialog.cancel();
                    }

                })
                .create();

        showUserSettings(v);
        return dialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;

            ImageButton settingsButton = (ImageButton) activity.findViewById(R.id.btnSettings);
            if(settingsButton != null) {
                settingsButton.setImageResource(R.drawable.settings);
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        ImageButton settingsButton = (ImageButton) getActivity().findViewById(R.id.btnSettings);
        if(settingsButton != null) {
            settingsButton.setImageResource(R.drawable.settings_grey);
        }
    }

    /***
     * Fragment Interaction Listener
     */
    public interface OnFragmentInteractionListener {
        public void onSettingsDialogClose();
    }

}


