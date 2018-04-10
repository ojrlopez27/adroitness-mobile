package edu.cmu.adroitness.client.services.email.control;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import edu.cmu.adroitness.R;
import edu.cmu.adroitness.client.services.email.model.GmailMessageVO;
import edu.cmu.adroitness.client.services.email.view.GmailActivity;

/**
 * Created by sakoju on 3/8/17.
 */

public class GmailMessageAdapter extends ArrayAdapter<GmailMessageVO> {
    Context context;
    private int layoutId;
    GmailActivity gmailActivity;
    List<GmailMessageVO> gmailMessageVOList;
    List<View> viewList = new ArrayList<>();
    RadioButton mSelectedRadioButton;
    int selectedPosition;


    // this improves the performance and reuse resources
    class ViewHolder {
        public TextView text;
        public RadioButton action;
        public int position;
    }

    public GmailMessageAdapter(Context context, int resource, List<GmailMessageVO> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layoutId = resource;
        gmailMessageVOList = new ArrayList<>(objects);
    }

    @Override
    public void addAll(Collection<? extends GmailMessageVO> collection) {
        super.addAll(collection);
        gmailMessageVOList = (List<GmailMessageVO>)collection;
    }

    public void updateLatestEmails(List<GmailMessageVO> emails)
    {
        this.gmailMessageVOList = emails;
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View messageRow = convertView;
        if(messageRow==null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) ((Activity)context)
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            messageRow = layoutInflater.inflate(layoutId, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.text = (TextView) messageRow.findViewById(R.id.messageListElement);
            viewHolder.action = (RadioButton) messageRow.findViewById(R.id.actionRadiobox);
            viewHolder.position = position;
            messageRow.setTag(viewHolder);
        }
        //gmailMessageVOList = gmailActivity.getGmailMessageVOList();
        synchronized (gmailMessageVOList)
        {
            if(gmailMessageVOList!=null  && position< gmailMessageVOList.size())
            {
                final ViewHolder viewHolder = (ViewHolder) messageRow.getTag();
                GmailMessageVO gmailMessageVO = gmailMessageVOList.get(viewHolder.position);
                viewHolder.text.setVisibility(View.VISIBLE);
                viewHolder.action.setVisibility(View.VISIBLE);

                viewHolder.text.setText(gmailMessageVO.getJsonString());

                /*if(!viewHolder.action.hasOnClickListeners())
                {
                    viewHolder.action.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(viewHolder.action.isChecked())
                            {
                                gmailActivity.enableButtonsOnListItemSelected(new Boolean(true));
                                gmailActivity.setSelectedGmailMessage(gmailMessageVOList.get(viewHolder.position));
                            }
                            else
                            {
                                gmailActivity.enableButtonsOnListItemSelected(new Boolean(false));
                            }
                        }
                    });
                }*/
                viewHolder.action.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked)
                        {
                            if(position!=selectedPosition && mSelectedRadioButton!=null) {
                                mSelectedRadioButton.setChecked(false);
                                selectedPosition = position;
                                mSelectedRadioButton = viewHolder.action;
                                viewHolder.action.setChecked(true);
                            }
                            else
                            {
                                selectedPosition = position;
                                mSelectedRadioButton = viewHolder.action;
                                viewHolder.action.setChecked(true);
                            }
                            gmailActivity.enableButtonsOnListItemSelected(new Boolean(true));
                            gmailActivity.setSelectedGmailMessage(gmailMessageVOList.get(viewHolder.position));
                        }
                        else
                        {
                            gmailActivity.enableButtonsOnListItemSelected(new Boolean(false));
                            viewHolder.action.setChecked(false);
                            mSelectedRadioButton=null;
                            selectedPosition=0;
                        }
                    }
                });

                if(!viewHolder.text.hasOnClickListeners())
                {
                    viewHolder.text.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            viewHolder.action.setChecked(true);
                            gmailActivity.setSelectedGmailMessage(gmailMessageVOList.get(viewHolder.position));
                            gmailActivity.createEmailFragment(v);
                        }
                    });
                }
            }
        }


        return messageRow;
    }

    public void setGmailActivity(GmailActivity gmailActivity) {
        this.gmailActivity = gmailActivity;
    }
}
