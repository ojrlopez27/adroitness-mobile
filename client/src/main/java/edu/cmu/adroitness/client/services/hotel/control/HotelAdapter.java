package edu.cmu.adroitness.client.services.hotel.control;

/**
 * Created by oscarr on 6/27/16.
 */

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import edu.cmu.adroitness.client.R;
import edu.cmu.adroitness.client.services.booking.model.HotelVO;

import java.util.ArrayList;

/**
 * ADAPTER
 */
public class HotelAdapter extends ArrayAdapter<HotelVO> {
    private final Context context;
    private int layoutId;
    private int[] imgIds = new int[]{ R.id.im1, R.id.im2, R.id.im3, R.id.im4, R.id.im5};
    private ArrayList<HotelVO> hotels;

    // this improves the performance and reuse resources
    class ViewHolder {
        TextView url;
        TextView recommendation;
        TextView amenities;
        TextView price;
        TextView pricePerNight;
        ImageView image;
        int position;
    }

    public HotelAdapter(Context context, int layoutId, ArrayList<HotelVO> hotelResults ) {
        super(context, 0, hotelResults);
        this.context = context;
        this.layoutId = layoutId;
        this.hotels = hotelResults;
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
            viewHolder.url = (TextView) rowView.findViewById(R.id.url);
            viewHolder.recommendation = (TextView) rowView.findViewById(R.id.recommendation);
            viewHolder.amenities = (TextView) rowView.findViewById(R.id.amenities);
            viewHolder.price = (TextView) rowView.findViewById(R.id.price);
            viewHolder.pricePerNight = (TextView) rowView.findViewById(R.id.perNight);
            viewHolder.position = position;
            rowView.setTag(viewHolder);
        }

        // fill data
        synchronized ( hotels ) {
            final ViewHolder holder = (ViewHolder) rowView.getTag();
            HotelVO hotel = hotels.get(holder.position); //getItem(holder.position);
            int numberOfStars = Integer.parseInt(hotel.getStarRating().substring(0, 1));
            holder.image = (ImageView) rowView.findViewById(imgIds[numberOfStars - 1]);
            for( int i = 0; i < numberOfStars; i++ ){
                ((ImageView) rowView.findViewById(imgIds[i])).
                        setImageResource(R.drawable.starafter);
            }
            if (hotel.getStarRating().substring(2).equals("5")) {
                holder.image = (ImageView) rowView.findViewById(imgIds[numberOfStars]);
                holder.image.setImageResource(R.drawable.halfstar);
            }
            String val = "<a href=\"" + hotel.getUrl() + "\">Hotel's Info</a>";
            holder.url.setMovementMethod(LinkMovementMethod.getInstance());
            holder.url.setText(Html.fromHtml(val));

            holder.recommendation.setText( hotel.getRecommendationPercentage() == null? "N/A"
                    : hotel.getRecommendationPercentage() + " %");
            holder.amenities.setText("Amenities: " + hotel.getAmenityCodes().toString());
            holder.price.setText("Total Price: " + hotel.getTotalPrice());
            holder.pricePerNight.setText("Price Per Night: " + hotel.getPricePerNight());
        }
        return rowView;
    }
}
