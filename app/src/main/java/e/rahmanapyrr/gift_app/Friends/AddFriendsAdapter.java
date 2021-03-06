package e.rahmanapyrr.gift_app.Friends;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import e.rahmanapyrr.gift_app.R;
import e.rahmanapyrr.gift_app.models.User;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;
import static com.parse.Parse.getApplicationContext;

public class AddFriendsAdapter extends RecyclerView.Adapter<AddFriendsAdapter.ViewHolder> {

    private List<User> Users;
    ArrayList<ParseUser> friends;

    Context context;

    public AddFriendsAdapter(List<User> users) {
        this.Users = users;
    }

    @Override
    public AddFriendsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View friendView = inflater.inflate(R.layout.item_friend_option, parent, false);

        return new AddFriendsAdapter.ViewHolder(friendView);
    }

    @Override
    public void onBindViewHolder(AddFriendsAdapter.ViewHolder holder, int position) {
        // get data
        ParseUser user = Users.get(position);
//        holder.Friend.setText(user.getUsername());
        holder.Friend_name.setText(user.get("firstname") + " " + user.get("lastname"));

        //String myTime = TimeFormat.getTimeDifference(user.getCreatedAt().toString());
        //            holder.time.setText(myTime);
        //holder.tvDate.setText(getRelativeTimeAgo(post.getCreatedAt().toString()));

//        holder.Friend.setText(user.getUsername());
        final ParseFile pic = user.getParseFile("ProfilePic");
        if(pic != null){
            Glide.with(context).load(pic.getUrl())
                    .apply(bitmapTransform(new CircleCrop()))
                    .into(holder.profilePic);
        }
    }

    @Override
    public int getItemCount() {
        return Users.size();
    }

    public void AddFriendFunc(View view) {}
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView Friend;
        public TextView Friend_name;
        public Button AddFriendbtn;
        public ImageView profilePic;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            Friend_name = (TextView) itemView.findViewById(R.id.friendNameOption);
            AddFriendbtn = (Button) itemView.findViewById(R.id.addFriendbtn);
            profilePic = itemView.findViewById(R.id.profile2);

            ///add friends///-----------------------------------
            AddFriendbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final ParseUser user = Users.get(getAdapterPosition());
                    ParseUser currentUserClass = ParseUser.getCurrentUser();

                    ParseRelation<ParseUser> friends = currentUserClass.getRelation("FriendRelation");
                    friends.add(user);
                    currentUserClass.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Log.d("Friend", "New friend added: " + user.getUsername());
                                } else {
                                Log.d("Friend", "Error: " + e.getMessage());
                                }

                                }
                                });

                    }
                    ///button to add friends------------------------------------------
            });

        //when the user clicks on a row, show MovieDetailsActivity for the selected movie

        }
    }

    // Clean all elements of the recycler
    public void clear() {
        Users.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<User> list) {
        Users.addAll(list);
        notifyDataSetChanged();
    }

    public void sendNotification(String body) {
        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notifiBuilder = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Firebase Cloud Messaging")
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(notificationSound);

        Notification notification= notifiBuilder.build();
        NotificationManager manager = (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0/*ID of notify:*/, notification);
    }

}


