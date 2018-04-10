package edu.cmu.adroitness.client.services.calendar.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import edu.cmu.adroitness.client.R;
import edu.cmu.adroitness.client.commons.control.ViewHelper;

/**
 * Created by oscarr on 4/12/16.
 */
public class DialogCalendar extends DialogFragment {
    private ViewHelper helper;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.prompt_type_calendar)
                .setCancelable(false)
                .setItems(R.array.type_calendar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                helper.startActivity( CalendarActivity.class );
                                break;
                            case 1:
                                helper.startActivity( ExtendedCalendarActivity.class );
                                break;
                            case 2:
                                helper.startActivity( CustomizedCalendarActivity.class );
                                break;
                            default:
                                break;
                        }
                    }
                });
        return builder.create();
    }

    public void setHelper(ViewHelper helper){
        this.helper = helper;
    }
}
