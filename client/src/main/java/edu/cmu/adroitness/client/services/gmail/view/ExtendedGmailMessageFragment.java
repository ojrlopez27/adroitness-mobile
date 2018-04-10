package edu.cmu.adroitness.client.services.gmail.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.cmu.adroitness.client.services.email.model.GmailMessageVO;
import edu.cmu.adroitness.client.services.email.view.GmailMessageFragment;

/**
 * A simple {@link GmailMessageFragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ExtendedGmailMessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExtendedGmailMessageFragment extends GmailMessageFragment {

    public ExtendedGmailMessageFragment() {super();}

    @SuppressLint("ValidFragment")
    public ExtendedGmailMessageFragment(ExtendedGmailActivity gmailActivity, GmailMessageVO param1, String param2)
    {
        this.gmailActivity = gmailActivity;
        if(param1!=null) {
            this.gmailMessageVO = param1;
            selectedMessageID = this.gmailMessageVO.getMessageID();
        }
        this.queryType =param2;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
        // Inflate the layout for this fragment
    }

}
