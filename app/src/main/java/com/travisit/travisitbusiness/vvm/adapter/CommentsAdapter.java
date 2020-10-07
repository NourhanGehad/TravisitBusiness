package com.travisit.travisitbusiness.vvm.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.travisit.travisitbusiness.R;
import com.travisit.travisitbusiness.data.Const;
import com.travisit.travisitbusiness.model.OfferComment;
import com.travisit.travisitbusiness.utils.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;
    private ArrayList<OfferComment> mCommentItems;
    public CommentsAdapter(ArrayList<OfferComment> postItems) {
        this.mCommentItems = postItems;
    }
    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.card_comment, parent, false));
            case VIEW_TYPE_LOADING:
                return new ProgressHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_layout, parent, false));
            default:
                return null;
        }
    }
    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }
    @Override
    public int getItemViewType(int position) {
        if (isLoaderVisible) {
            return position == mCommentItems.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }
    @Override
    public int getItemCount() {
        return mCommentItems == null ? 0 : mCommentItems.size();
    }
    public void addItems(ArrayList<OfferComment> postItems) {
        mCommentItems.addAll(postItems);
        notifyDataSetChanged();
    }
    public void addLoading() {
        isLoaderVisible = true;
        mCommentItems.add(new OfferComment());
        notifyItemInserted(mCommentItems.size() - 1);
    }
    public void removeLoading() {
        isLoaderVisible = false;
        int position = mCommentItems.size() - 1;
        OfferComment item = getItem(position);
        if (item != null) {
            mCommentItems.remove(position);
            notifyItemRemoved(position);
        }
    }
    public void clear() {
        mCommentItems.clear();
        notifyDataSetChanged();
    }
    OfferComment getItem(int position) {
        return mCommentItems.get(position);
    }
    public void appendComments(ArrayList<OfferComment> comments){
        this.mCommentItems.addAll(comments);
        notifyItemRangeInserted(this.mCommentItems.size(), comments.size() - 1);
    }
    public class ViewHolder extends BaseViewHolder {
        SimpleDraweeView userImage;
        TextView userName;
        TextView commentText;
        ViewHolder(View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.card_comment_user_picture);
            userName = itemView.findViewById(R.id.card_comment_user_name);
            commentText = itemView.findViewById(R.id.card_comment_text);
        }
        protected void clear() {
        }
        public void onBind(int position) {
            super.onBind(position);
            OfferComment item = mCommentItems.get(position);
            //TODO ADD LATER
            /*String photoPath = Const.IMAGES_SERVER_ADDRESS + item.g;*/
            /*userImage.setImageURI(Uri.parse(userPhotoPath));
            userName.setText(item.getTitle());*/
            commentText.setText(item.getComment());
        }
    }
    public class ProgressHolder extends BaseViewHolder {
        ProgressBar progressBar;
        ProgressHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
        @Override
        protected void clear() {
        }
    }

}