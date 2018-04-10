package edu.cmu.adroitness.client.services.googlespeechrecognition.view;

import java.util.ArrayList;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import edu.cmu.adroitness.comm.generic.control.MessageBroker;
import edu.cmu.adroitness.R;
import edu.cmu.adroitness.client.services.googlespeechrecognition.control.GoogleSpeechRecognitionController;

public class GoogleSpeechRecognitionActivity extends AppCompatActivity implements MessageSpeechFragment.Listener {
    private static GoogleSpeechRecognitionController googleSpeechRecognitionController;
    protected Context mContext;
    Activity googleSpeechActivity;
    private static final String FRAGMENT_MESSAGE_DIALOG = "message_dialog";

    private static final String STATE_RESULTS = "results";

        private static final int REQUEST_RECORD_AUDIO_PERMISSION = 1;


    public static GoogleSpeechRecognitionController getGoogleSpeechRecognitionController() {
        return googleSpeechRecognitionController;
    }

    public static void setGoogleSpeechRecognitionController(
            GoogleSpeechRecognitionController googleSpeechRecognitionController) {
        GoogleSpeechRecognitionActivity.googleSpeechRecognitionController
                = googleSpeechRecognitionController;
    }


    public int getmColorHearing() {
        return mColorHearing;
    }

    public int getmColorNotHearing() {
        return mColorNotHearing;
    }

    // Resource caches
    private int mColorHearing;
    private int mColorNotHearing;

    public TextView getmStatus() {
        return mStatus;
    }

    public void setmStatus(TextView mStatus) {
        this.mStatus = mStatus;
    }

    public TextView getmText() {
        return mText;
    }

    public void setmText(TextView mText) {
        this.mText = mText;
    }

    public ResultAdapter getmAdapter() {
        return mAdapter;
    }

    public void setmAdapter(ResultAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    public RecyclerView getmRecyclerView() {
        return mRecyclerView;
    }

    public void setmRecyclerView(RecyclerView mRecyclerView) {
        this.mRecyclerView = mRecyclerView;
    }

    // View references
    private TextView mStatus;
    private TextView mText;
    private ResultAdapter mAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.services_google_speech_recognition_activity);
        final Resources resources = getResources();
        final Resources.Theme theme = getTheme();
        mColorHearing = ResourcesCompat.getColor(resources, R.color.status_hearing, theme);
        mColorNotHearing = ResourcesCompat.getColor(resources, R.color.status_not_hearing, theme);

       // setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        mStatus = (TextView) findViewById(R.id.status);
        mText = (TextView) findViewById(R.id.text);

        mContext = this;
        googleSpeechActivity = this;
        MessageBroker.getInstance(getApplicationContext()).subscribe(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        final ArrayList<String> results = savedInstanceState == null ? null :
                savedInstanceState.getStringArrayList(STATE_RESULTS);
        mAdapter = new ResultAdapter(results);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setVisibility(View.INVISIBLE);
        googleSpeechRecognitionController = GoogleSpeechRecognitionController.getInstance(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Prepare Cloud Speech API

        // Start listening to voices
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {
            googleSpeechRecognitionController.startVoiceRecorder();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.RECORD_AUDIO)) {
            showPermissionMessageDialog();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_RECORD_AUDIO_PERMISSION);
        }

       // MessageBroker.getInstance(getApplicationContext()).subscribe(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MessageBroker.getInstance(getApplicationContext()).subscribe(this);
    }

    @Override
    protected void onPause() {
        MessageBroker.getInstance(getApplicationContext()).unsubscribe(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        MessageBroker.getInstance(getApplicationContext()).unsubscribe(this);
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        // Stop listening to voice
        googleSpeechRecognitionController.stopVoiceRecorder();

        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mAdapter != null) {
            outState.putStringArrayList(STATE_RESULTS, mAdapter.getResults());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (permissions.length == 1 && grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                googleSpeechRecognitionController.startVoiceRecorder();
            } else {
                showPermissionMessageDialog();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    private void showPermissionMessageDialog() {
        MessageSpeechFragment
                .newInstance(getString(R.string.permission_message))
                .show(getSupportFragmentManager(), FRAGMENT_MESSAGE_DIALOG);
    }

    @Override
    public void onMessageDialogDismissed() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},
                REQUEST_RECORD_AUDIO_PERMISSION);
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        TextView text;

        ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.speech_item_result, parent, false));
            text = (TextView) itemView.findViewById(R.id.text);
        }

    }

    public static class ResultAdapter extends RecyclerView.Adapter<ViewHolder> {

        private final ArrayList<String> mResults = new ArrayList<>();

        ResultAdapter(ArrayList<String> results) {
            if (results != null) {
                mResults.addAll(results);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.text.setText(mResults.get(position));
        }

        @Override
        public int getItemCount() {
            return mResults.size();
        }

        public void addResult(String result) {
            mResults.add(0, result);
            notifyItemInserted(0);
        }

        public ArrayList<String> getResults() {
            return mResults;
        }

    }

}
