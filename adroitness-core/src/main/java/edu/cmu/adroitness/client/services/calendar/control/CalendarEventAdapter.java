package edu.cmu.adroitness.client.services.calendar.control;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import edu.cmu.adroitness.R;
import edu.cmu.adroitness.client.services.calendar.model.CalendarEventVO;
import edu.cmu.adroitness.client.services.calendar.view.CalendarDayDetailActivity;

public class CalendarEventAdapter extends ArrayAdapter<CalendarEventVO> {
    private final Context context;
    private int layoutId;
    private CalendarDayDetailActivity calendarDayDetailActivity;
    private List<CalendarEventVO> events;


    // this improves the performance and reuse resources
    class ViewHolder {
        public TextView text;
        public CheckBox delete;
        public int position;
    }

    public CalendarEventAdapter(Context context, int layoutId, List<CalendarEventVO> elements) {
        super(context, layoutId, elements);
        this.context = context;
        this.layoutId = layoutId;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        // reuse views to improve performance
        if (rowView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            rowView = inflater.inflate(layoutId, parent, false);

            // configure view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.text = (TextView) rowView.findViewById(R.id.textListElement);
            viewHolder.delete = (CheckBox) rowView.findViewById(R.id.deleteCheckBox);
            viewHolder.position = position;
            rowView.setTag(viewHolder);
        }


        // fill data
        events = calendarDayDetailActivity.getEvents();
        synchronized ( events ) {
            if ( events != null && position < events.size()) {
                final ViewHolder holder = (ViewHolder) rowView.getTag();
                CalendarEventVO event = events.get(holder.position);
                holder.delete.setVisibility(View.VISIBLE);
                holder.text.setVisibility(View.VISIBLE);

                if( calendarDayDetailActivity.isRearrangeElements() ) {
                    holder.delete.setChecked(false);
                    holder.position = position;
                    if (holder.position == events.size() - 1) {
                        calendarDayDetailActivity.setIsRearrangeElements( false );
                    }
                }

                holder.text.setText("Summary " + position + ": " + event.getSummary()
                        + "  start date: " + event.getFormattedStartDate() + ":" + event.getFormattedStartTime()
                        + "  end date: " + event.getFormattedEndDate() + ":" + event.getFormattedEndTime());
                if (!holder.text.hasOnClickListeners()) {
                    holder.text.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            calendarDayDetailActivity.setIsModifyEvent(true);
                            calendarDayDetailActivity.setEventToModify(events.get(holder.position));
                            calendarDayDetailActivity.createEventFragment( v );
                        }
                    });
                }

                if (!holder.delete.hasOnClickListeners()) {
                    holder.delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CalendarEventVO event = events.get(holder.position);
                            if (holder.delete.isChecked()) {
                                calendarDayDetailActivity.getDeleteEventsList().add(event);
                            } else {
                                calendarDayDetailActivity.getDeleteEventsList().remove(event);
                            }
                        }
                    });
                }
            }
            return rowView;
        }
    }

    public void setCalendarDayDetailActivity(CalendarDayDetailActivity calendarDayDetailActivity) {
        this.calendarDayDetailActivity = calendarDayDetailActivity;
    }
}
