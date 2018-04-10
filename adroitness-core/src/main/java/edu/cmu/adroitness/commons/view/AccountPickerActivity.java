package edu.cmu.adroitness.commons.view;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import edu.cmu.adroitness.comm.calendar.model.CalendarNotificationEvent;
import edu.cmu.adroitness.comm.email.model.GmailManagerEvent;
import edu.cmu.adroitness.comm.generic.control.MessageBroker;
import edu.cmu.adroitness.comm.generic.control.adapters.GooglePlayAdapter;
import edu.cmu.adroitness.comm.generic.model.MiddlewareNotificationEvent;
import edu.cmu.adroitness.client.commons.control.ResourceLocator;
import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.client.commons.control.UtilServiceAPIs;

public class AccountPickerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GooglePlayAdapter adapter = (GooglePlayAdapter) MessageBroker.getExistingInstance(this)
                .getAdapter(Constants.GOOGLE_PLAY);
        adapter.isAccountSelected( this );
    }

    /**
     * Called after AccountPicker and authorization dialog box exits.
     * @param requestCode code indicating which Activity result is incoming.
     * @param resultCode code indicating the result of the incoming Activity result.
     * @param data Intent (containing result data) returned by incoming Activity result.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        boolean finish = false;
        if( requestCode == Constants.REQUEST_ACCOUNT_PICKER ){
            if (resultCode == RESULT_OK && data != null && data.getExtras() != null) {
                String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                if (accountName != null) {
                    UtilServiceAPIs.configAccountName(this, accountName);
                    ResourceLocator.getExistingInstance().setAccount(Constants.GOOGLE_ACCOUNT, accountName);
                    MessageBroker.getExistingInstance(this).send(this,
                            MiddlewareNotificationEvent.build().setSourceName( this.getClass().getName() ) );
                    finish = true;
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Account unspecified.", Toast.LENGTH_LONG).show();
            }
            else if(requestCode == Constants.REQUEST_AUTHORIZATION){
                Log.d("activity result", String.valueOf(requestCode));
                    if (resultCode != RESULT_OK) {
                        //controller.chooseAccount(this);
                    }
                //startActivityForResult(, Constants.REQUEST_AUTHORIZATION);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
        if( finish ){
            finish();
        }
    }

    /**
     * Event Handler. Here you can handle calendar events and error messages
     * @param event
     */
    public void onEventMainThread( final CalendarNotificationEvent event ) {
        if (event.getNotification().equals(Constants.CALENDAR_USER_RECOVERABLE_EXCEPTION)) {
            startActivityForResult((Intent) event.getParams(), Constants.REQUEST_AUTHORIZATION);
        }
    }
    public void onEventMainThread( final GmailManagerEvent event ) {
        if (event.getMessageString().equals(Constants.CALENDAR_USER_RECOVERABLE_EXCEPTION)) {
            startActivityForResult((Intent) event.getParams(), Constants.REQUEST_AUTHORIZATION);
        }
    }
}
