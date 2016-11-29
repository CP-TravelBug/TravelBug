package codepath.travelbug.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import codepath.travelbug.R;
import codepath.travelbug.models.User;

/**
 * Created by ocarty on 11/22/2016.
 */

public class ShareAdapter extends
        RecyclerView.Adapter<ShareAdapter.ViewHolder> {

    private List<User> friendList;
    // Store the context for easy access
    private Context mContext;

    // Pass in the contact array into the constructor
    public ShareAdapter(Context context, List<User> friends) {
        friendList = friends;
        mContext = context;
    }
    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }
    @Override
    public ShareAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_share, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ShareAdapter.ViewHolder holder, int position) {
        User friend = friendList.get(position);

        // Set item views based on your views and data model
        TextView textView = holder.nameTextView;
        textView.setText(friend.getFullName());

    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public ImageView ivProfileImage;
        public CheckBox cbShareButton;

        public ViewHolder(View itemView) {
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.friendName);
        }
    }
}
