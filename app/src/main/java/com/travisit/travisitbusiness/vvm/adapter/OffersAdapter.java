package com.travisit.travisitbusiness.vvm.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.travisit.travisitbusiness.R;
import com.travisit.travisitbusiness.data.Const;
import com.travisit.travisitbusiness.model.Offer;

import java.util.ArrayList;
import java.util.List;

public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.OfferViewHolder> implements Filterable {
    private final Context context;
    private List<Offer> items;
    private List<Offer> itemsToDisplay;
    private SelectionPropagator observer;

    public OffersAdapter(List<Offer> items, Context context, SelectionPropagator observer) {
        this.items = items;
        this.itemsToDisplay = new ArrayList<>();
        this.context = context;
        this.observer = observer;
    }

    @Override
    public OfferViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_offer, parent, false);
        return new OfferViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OfferViewHolder holder, int position) {
        Offer item = itemsToDisplay.get(position);
        holder.set(item, context, observer);
    }

    class OfferViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView offerImage;
        TextView offerName;
        TextView rating;
        TextView endDate;
        ConstraintLayout container;
        public OfferViewHolder(@NonNull View itemView) {
            super(itemView);
            offerImage = itemView.findViewById(R.id.card_offer_sdv_image);
            offerName = itemView.findViewById(R.id.card_offer_tv_name);
            rating = itemView.findViewById(R.id.card_offer_tv_rating);
            endDate = itemView.findViewById(R.id.card_offer_tv_end_date);
            container = itemView.findViewById(R.id.card_offer_container);
        }
        public void set(Offer item, Context context, SelectionPropagator observer) {
            String offerPhotoPath = Const.IMAGES_SERVER_ADDRESS + item.getFirstImage();
            offerImage.setImageURI(Uri.parse(offerPhotoPath));

            offerName.setText(item.getTitle());
            //TODO CHANGE RATING Add Price
            rating.setText("3.5");
            endDate.setText(context.getString(R.string.ends_on) + " " + item.getEndDate());
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    observer.offerSelected(item);
                }
            });
        }
    }
    @Override
    public int getItemCount() {
        if (itemsToDisplay == null) {
            return 0;
        }
        return itemsToDisplay.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                String searchString = constraint.toString();
                if(searchString.isEmpty()){
                    itemsToDisplay = items;
                } else {
                    for(Offer offer : items){
                        if(offer.getTitle().toLowerCase().contains(searchString.toLowerCase()) ||
                                offer.getDescription().toLowerCase().contains(searchString.toLowerCase()) ){
                            itemsToDisplay.add(offer);
                        }
                    }
                    if(itemsToDisplay.isEmpty()){
                        Toast.makeText(context, "Couldn't Find: "+searchString+" !", Toast.LENGTH_SHORT);
                    }
                }
                results.values = itemsToDisplay;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

            }
        };
    }
    private void removeItem(Offer offer){
        items.remove(offer);
        notifyDataSetChanged();
    }
    public interface SelectionPropagator{
        void offerSelected(Offer offer);
    }
}