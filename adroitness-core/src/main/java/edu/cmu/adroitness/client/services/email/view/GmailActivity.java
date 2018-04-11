package edu.cmu.adroitness.client.services.email.view;

import java.util.ArrayList;
import java.util.List;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import edu.cmu.adroitness.R;
import edu.cmu.adroitness.comm.email.model.GmailManagerEvent;
import edu.cmu.adroitness.comm.generic.control.MessageBroker;
import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.client.commons.control.Util;
import edu.cmu.adroitness.client.services.email.control.GmailController;
import edu.cmu.adroitness.client.services.email.control.GmailMessageAdapter;
import edu.cmu.adroitness.client.services.email.model.GmailMessageVO;
import edu.cmu.adroitness.client.services.generic.view.ServicesActivity;

public class GmailActivity extends ServicesActivity {
    public static GmailController viewHelper;
    GmailMessageFragment fragment;
    Toolbar decoration_toolbar;
    protected Context mContext;
    Activity gmailActivity;
    public ListView listView;
    public static boolean isItemClicked=false;
    protected static String query ="";
    protected static List<GmailMessageVO> gmailMessageVOList;
    GmailMessageVO selectedGmailMessage;

    protected Class<? extends GmailMessageFragment> eventCreationFragmentClass = GmailMessageFragment.class;
    Boolean goBack = true;
    public static GmailMessageAdapter gmailMessageAdapter;
    Button viewBtn, replyBtn, forwardBtn, composeBtn, replyToAllEmail;

    public void setSelectedGmailMessage(GmailMessageVO selectedGmailMessage) {
        this.selectedGmailMessage = selectedGmailMessage;
    }

    public List<GmailMessageVO> getGmailMessageVOList() {
        return gmailMessageVOList;
    }

    public GmailController getViewHelper() {
        return viewHelper;
    }

    public static void setViewHelper(GmailController viewHelper) {
        GmailActivity.viewHelper = viewHelper;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.services_gmail_activity);
        decoration_toolbar = (Toolbar) findViewById(R.id.decoration_toolbar);
        listView = (ListView) findViewById(R.id.emailsListView);
        viewBtn = (Button) findViewById(R.id.viewEmail);
        replyBtn = (Button) findViewById(R.id.replyEmail);
        forwardBtn = (Button) findViewById(R.id.forwardEmail);
        composeBtn = (Button) findViewById(R.id.composeEmailButton);
        replyToAllEmail = (Button) findViewById(R.id.replyToAllEmail);
        replyBtn.setEnabled(false);
        composeBtn.setEnabled(false);
        forwardBtn.setEnabled(false);
        viewBtn.setEnabled(false);
        replyToAllEmail.setEnabled(false);
        mContext = this;
        gmailActivity = this;
        MessageBroker.getInstance(getApplicationContext()).subscribe(this);
        viewHelper = GmailController.getInstance(this);
        gmailMessageVOList = new ArrayList<>();
        selectedGmailMessage = new GmailMessageVO();
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        initialize();

        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query= "view";
                createEmailFragment(v);
            }
        });
        composeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query= "compose";
                createEmailFragment(v);
            }
        });
        replyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query = "reply";
                createEmailFragment(v);
            }
        });
        forwardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query="forward";
                createEmailFragment(v);
            }
        });
        replyToAllEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query = "replyall";
                createEmailFragment(v);
            }
        });


    }

    protected void initialize()
    {
        gmailMessageVOList = new ArrayList<>();
        viewHelper.filterMessagesByQuery(null, new Boolean(true));
        gmailMessageAdapter = new GmailMessageAdapter(mContext,
                R.layout.services_adapter_gmail_messages, gmailMessageVOList);
        gmailMessageAdapter.setGmailActivity((GmailActivity)gmailActivity);
        listView.setAdapter(gmailMessageAdapter);
        decoration_toolbar.setTitle("Adroitness");
        //listView.setAdapter(gmailMessageVOList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MessageBroker.getInstance(getApplicationContext()).subscribe(this);
        viewHelper.filterMessagesByQuery(null, new Boolean(true));

    }

    public void filterQuery()
    {
        viewHelper.filterMessagesByQuery(null, new Boolean(true));
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

    public void onBackPressed() {
        goBack= false;
        if(!query.isEmpty())
        {
            query ="";
            selectedGmailMessage = null;
        }
        if ( fm.getBackStackEntryCount() == 0 ) {
            super.onBackPressed();
        } else {
            fm.popBackStack();
            LinearLayout mainLayout = (LinearLayout) findViewById(R.id.gmailLayout);
            mainLayout.setVisibility(View.VISIBLE);
        }
    }

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
                case Constants.GMAIL_GET_MESSAGE_VO:
                    if (!event.getGmailMessageVO().getJsonString().equals(""))
                        Log.d("GET_GMAILVO", event.getGmailMessageVO().getJsonString());
                    break;
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

    /**
     * Called when an mActivity launched here (specifically, AccountPicker
     * and authorization) exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     * @param requestCode code indicating which mActivity result is incoming.
     * @param resultCode code indicating the result of the incoming
     *     mActivity result.
     * @param data Intent (containing result data) returned by incoming
     *     mActivity result.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null && data.getExtras() != null) {
                    String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        viewHelper.configAccountName(this, accountName);
                        viewHelper.getDataFromGmail();
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "Account unspecified.", Toast.LENGTH_LONG).show();
                }
                break;
            case Constants.REQUEST_AUTHORIZATION:
                if (resultCode != RESULT_OK) {
                    viewHelper.getDataFromGmail();
                }
                break;
            case Constants.REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    viewHelper.checkGooglePlayServicesAvailable();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void refreshMessages( List<GmailMessageVO> emails)
    {
        /*Util.execute(new Runnable() {
            @Override
            public void run() {*/


                gmailMessageVOList = new ArrayList<>(emails);
                if (gmailMessageVOList == null) {
                    gmailMessageVOList = new ArrayList<>();
                }
                gmailMessageAdapter.clear();
                gmailMessageAdapter.addAll(gmailMessageVOList);
                //istView.setVisibility(View.VISIBLE);
                listView.setAdapter(gmailMessageAdapter);
                gmailMessageAdapter.notifyDataSetChanged();
            /*}
        });*/
    }

    public void createEmailFragment(View v) {
        fragment = Util.createInstance(eventCreationFragmentClass, this,
                selectedGmailMessage==null?new GmailMessageVO():selectedGmailMessage, query );
        //fragment = GmailMessageFragment.newInstance( (GmailActivity) gmailActivity,
                //gmailMessageVO, action);
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.gmailLayout);
        mainLayout.setVisibility(View.INVISIBLE);
        ft = fm.beginTransaction();
        ft.replace(R.id.gmailMain, fragment);
        ft.addToBackStack(null);
        ft.commit();
        goBack = true;
    }

    public void enableButtonsOnListItemSelected(Boolean isMailSelected)
    {
        if(isMailSelected) {
            viewBtn.setEnabled(true);
            forwardBtn.setEnabled(true);
            replyBtn.setEnabled(true);
            replyToAllEmail.setEnabled(true);
            composeBtn.setEnabled(true);
        }
        else
        {
            composeBtn.setEnabled(true);
            viewBtn.setEnabled(false);
            forwardBtn.setEnabled(false);
            replyBtn.setEnabled(false);
            replyToAllEmail.setEnabled(false);
        }
    }

    public void setEventCreationFragmentClass(Class<? extends GmailMessageFragment> eventCreationFragmentClass) {
        this.eventCreationFragmentClass = eventCreationFragmentClass;
    }

}
