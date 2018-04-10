package edu.cmu.adroitness.client.services.email.view;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import edu.cmu.adroitness.R;
import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.client.services.email.model.GmailMessageVO;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link GmailMessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GmailMessageFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    protected View fragmentView;
    protected EditText to;
    protected EditText cc;
    protected EditText subject;
    protected String filePath;
    protected EditText messageContent;
    protected Button buttonSend, buttonAttach;
    protected GmailMessageVO gmailMessageVO;
    protected TextView titleTextView ;
    protected File file;
    protected String selectedMessageID ="";

    // TODO: Rename and change types of parameters
    protected String queryType;
    protected GmailActivity gmailActivity;

    public GmailMessageFragment() {
        // Required empty public constructor
    }


    @SuppressLint("ValidFragment")
    public GmailMessageFragment(GmailActivity gmailActivity, GmailMessageVO param1, String param2)
    {
        this.gmailActivity = gmailActivity;
        if(param1!=null) {
            this.gmailMessageVO = param1;
            selectedMessageID = this.gmailMessageVO.getMessageID();
        }
        this.queryType =param2;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GmailMessageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GmailMessageFragment newInstance(GmailActivity gmailActivity, GmailMessageVO param1, String param2) {
        GmailMessageFragment fragment = new GmailMessageFragment(gmailActivity, param1, param2);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.services_gmail_details_fragment, container, false);
        buttonAttach = (Button) fragmentView.findViewById(R.id.attachFile);
        buttonSend = (Button) fragmentView.findViewById(R.id.done);
        titleTextView = (TextView)fragmentView.findViewById(R.id.titleTextView);
        messageContent = (EditText) fragmentView.findViewById(R.id.emailContent);
        to = (EditText) fragmentView.findViewById(R.id.toEmail);
        cc = (EditText) fragmentView.findViewById(R.id.ccEmail);
        subject = (EditText) fragmentView.findViewById(R.id.subjectEmail);
        subject.setMovementMethod(new ScrollingMovementMethod());
        messageContent.setMovementMethod(new ScrollingMovementMethod());
        if(queryType.equals("new"))
        {
            /*String afterDate="2016/12/13";
            String beforeDate="2016/12/18";
            String query="";
                query += "{ folderName: INBOX, ";
                query += " labelName: unread, ";
                query += " fromName: inmind, ";
                query += " subject: merge, ";
                query += " afterDate: "+ afterDate ;
                query += " beforeDate: "+ beforeDate;
                query+=" hasAtttachment: true,";
                query+= " fileName: test.doc}";
            messageString.setText(query);
            messageString.setMovementMethod(new ScrollingMovementMethod());
            buttonSend.setText("Send Query Request");
            buttonSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GmailFilterQueryInputVO gmailFilterQueryInputVO =
                            Util.fromJson(messageString.getText().toString(), GmailFilterQueryInputVO.class);
                    GmailActivity.viewHelper.filterMessagesByQuery(gmailFilterQueryInputVO, new Boolean(true));
                }
            });*/
            //send new email or modify email
            to.setVisibility(View.VISIBLE);
            cc.setVisibility(View.VISIBLE);
            to.setEnabled(true);
            cc.setEnabled(true);
            titleTextView.setText("Compose email");
            subject.setVisibility(View.VISIBLE);

        }
       else if(queryType.equals("reply"))
        {
            selectedMessageID = gmailMessageVO.getMessageID();
            titleTextView.setText("Reply to: "+gmailMessageVO.getSubject());
            subject.setVisibility(View.INVISIBLE);
            to.setText("");
            to.setVisibility(View.VISIBLE);
            buttonAttach.setEnabled(false);
            buttonAttach.setVisibility(View.INVISIBLE);
            cc.setVisibility(View.INVISIBLE);
        }
        else if(queryType.equals("replyall"))
        {
            selectedMessageID = gmailMessageVO.getMessageID();
            titleTextView.setText("Reply all : "+gmailMessageVO.getSubject());
            subject.setVisibility(View.INVISIBLE);
            to.setVisibility(View.VISIBLE);
            //to.setHint("Add additional recipients" );
            cc.setEnabled(false);
            cc.setVisibility(View.INVISIBLE);
            buttonAttach.setEnabled(false);
            buttonAttach.setVisibility(View.INVISIBLE);
        }
        else if(queryType.equals("forward"))
        {
            selectedMessageID = gmailMessageVO.getMessageID();
            titleTextView.setText("Forward email: "+gmailMessageVO.getSubject());
            subject.setVisibility(View.INVISIBLE);
            messageContent.setVisibility(View.INVISIBLE);
            to.setVisibility(View.VISIBLE);
            to.setText("");
            cc.setVisibility(View.VISIBLE);
            cc.setEnabled(true);
            cc.setText("");
            buttonAttach.setEnabled(false);
            buttonAttach.setVisibility(View.INVISIBLE);
        }
        else if(queryType.equals("view"))
        {
            selectedMessageID = gmailMessageVO.getMessageID();
            titleTextView.setText("View email details");
            to.setVisibility(View.VISIBLE);
            cc.setVisibility(View.VISIBLE);
            subject.setVisibility(View.VISIBLE);
            if (!gmailMessageVO.getFromEmail().equals("")) {
                to.setText(gmailMessageVO.getFromEmail());
                to.setHint("From:");
            }

            if (!gmailMessageVO.getSubject().equals("")) {
                subject.setText(gmailMessageVO.getSubject());
            }

            if (!gmailMessageVO.getMessageContent().equals("")) {
                messageContent.setText(gmailMessageVO.getMessageContent());
                messageContent.setMovementMethod(new ScrollingMovementMethod());
            }
            if (!gmailMessageVO.getCcEmail().equals("")) {
                cc.setText(gmailMessageVO.getCcEmail());
                cc.setHint("CC:");
            }
            else
            {
                cc.setText("");
                cc.setEnabled(false);
            }
            buttonSend.setText("Done");
        }

        buttonAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select File"), Constants.CAPTION_GENERATION_SELECT_IMAGE);
            }
        });

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toEmail = to.getText().toString();
                String content = messageContent.getText().toString();
                gmailMessageVO = new GmailMessageVO();
                gmailMessageVO.setToEmail(toEmail);
                gmailMessageVO.setMessageContent(content);

                if(queryType.equals("reply"))
                {
                    gmailMessageVO.setReplyAllFlag(false);
                    gmailMessageVO.setMessageID(selectedMessageID);
                    gmailActivity.getViewHelper().replyToEmail(gmailMessageVO);
                }
                else if(queryType.equals("replyall"))
                {
                    gmailMessageVO.setReplyAllFlag(true);
                    gmailMessageVO.setMessageID(selectedMessageID);
                    gmailActivity.getViewHelper().replyToEmail(gmailMessageVO);
                }
                else if(queryType.equals("forward"))
                {
                    gmailMessageVO.setMessageID(selectedMessageID);
                    gmailActivity.getViewHelper().forwardEmail(gmailMessageVO);
                }
                else if(queryType.equals("compose"))
                {
                    String subjectEmail = subject.getText().toString();
                    gmailMessageVO.setSubject(subjectEmail);
                    if(file!=null)
                    {
                        gmailMessageVO.setAttachedFile(file);
                        gmailMessageVO.setHasAttachment(true);
                        gmailMessageVO.setMessageID(selectedMessageID);
                        gmailActivity.getViewHelper().sendMessageWithAttachment(gmailMessageVO);
                    }
                    else
                    {
                        gmailMessageVO.setMessageID(selectedMessageID);
                        gmailActivity.getViewHelper().sendMessage(gmailMessageVO);
                    }
                }

                if(queryType.equals("view"))
                {
                    to.setText("");
                    cc.setText("");
                    subject.setText("");
                    messageContent.setText("");
                    ((GmailActivity)v.getParent()).onBackPressed();
                }
            }
        });
        return fragmentView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    filePath = data.getData().getPath();
                    System.out.println(filePath);
                    file = new File(filePath);
                }
            }
        }
    }

}
