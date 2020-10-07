package com.travisit.travisitbusiness.vvm.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.travisit.travisitbusiness.R;
import com.travisit.travisitbusiness.model.Branch;
import com.travisit.travisitbusiness.utils.ChosenAction;

import java.util.ArrayList;
import java.util.List;

public class BranchesAdapter extends RecyclerView.Adapter<BranchesAdapter.BranchViewHolder> {

    private final Context context;
    private List<Branch> items;
    private SelectionPropagator observer;

    public BranchesAdapter(List<Branch> items, Context context, SelectionPropagator observer) {
        this.items = items;
        this.context = context;
        this.observer = observer;
    }

    @Override
    public BranchViewHolder onCreateViewHolder(ViewGroup parent,
                                                 int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_branch, parent, false);
        return new BranchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BranchViewHolder holder, int position) {
        Branch item = items.get(position);
        holder.set(item, context, observer);
    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }

    class BranchViewHolder extends RecyclerView.ViewHolder {
        TextView branchName;
        TextView rating;
        TextView offersCount;
        ImageView options;
        ImageView delete;

        public BranchViewHolder(@NonNull View itemView) {
            super(itemView);
            branchName = itemView.findViewById(R.id.card_branch_tv_name);
            rating = itemView.findViewById(R.id.card_branch_tv_rating);
            offersCount = itemView.findViewById(R.id.card_branch_tv_no_of_offers);
            options = itemView.findViewById(R.id.card_branch_iv_options);
            delete = itemView.findViewById(R.id.card_branch_iv_delete);
        }
        public void set(Branch item, Context context, SelectionPropagator observer) {
            branchName.setText(item.getName());
            //TODO CHANGE RATING AND NUMBER OF OFFERS
            rating.setText("3.5");
            offersCount.setText("9");
            options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMenu(v, item);
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAlertDialog(item);
                }
            });
        }
    }
    private void showAlertDialog(Branch branch){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(R.string.delete_alert);
        builder.setMessage(context.getString(R.string.confirm_delete_message));
        builder.setPositiveButton(context.getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removeItem(branch);
                observer.branchSelected(branch, ChosenAction.DELETE);
            }
        });
        builder.setNegativeButton(context.getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
    private void showMenu(View v, Branch branch) {
        PopupMenu popup = new PopupMenu(context, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.edit_option:
                        observer.branchSelected(branch, ChosenAction.EDIT);
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.inflate(R.menu.menu_branch_options);
        popup.show();
    }
    private void removeItem(Branch branch){
        items.remove(branch);
        notifyDataSetChanged();
    }
    public interface SelectionPropagator{
        void branchSelected(Branch branch, ChosenAction action );
    }
}