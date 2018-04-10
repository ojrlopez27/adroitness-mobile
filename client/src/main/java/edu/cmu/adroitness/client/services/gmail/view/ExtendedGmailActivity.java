package edu.cmu.adroitness.client.services.gmail.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.client.services.email.view.GmailActivity;
import edu.cmu.adroitness.comm.email.model.GmailManagerEvent;

public class ExtendedGmailActivity extends GmailActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Override
    public void onEventMainThread(final GmailManagerEvent event)
    {
        if(!event.getMessageString().equals(""))
        {
            ((Activity)mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, event.getMessageString(), Toast.LENGTH_LONG).show();
                }
            });
        }
        if(event.getNotification()!=null) {
            switch (event.getNotification()) {
                case Constants.GMAIL_AFTER_FILTER_QUERY:
                    if( event.getEmails()!=null)
                    {
                        refreshMessages(event.getEmails());
                        /*listView.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1,
                                event.getMessageAdapterList()));*/
                    }
                    break;
                case Constants.GMAIL_AFTER_SEND_EMAIL:
                    Log.d("EmailSent", event.getMessageString());
                    break;
                case Constants.GMAIL_AFTER_FORWARD_EMAIL:
                    Log.d("EmailSent", event.getMessageString());
                    break;
                case Constants.GMAIL_AFTER_REPLY_EMAIL:
                    Log.d("EmailSent", event.getMessageString());
                    break;
                case Constants.CALENDAR_USER_RECOVERABLE_EXCEPTION:
                    startActivityForResult((Intent) event.getParams(),
                            Constants.REQUEST_AUTHORIZATION);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void initialize()
    {
        super.initialize();
        filterQuery();
        setEventCreationFragmentClass(ExtendedGmailMessageFragment.class);
    }
}
