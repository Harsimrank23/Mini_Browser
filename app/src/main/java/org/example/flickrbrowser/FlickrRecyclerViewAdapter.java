package org.example.flickrbrowser;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

class FlickrRecyclerViewAdapter extends RecyclerView.Adapter<FlickrRecyclerViewAdapter.FlickrImageViewHolder> {
    private static final String TAG = "FlickrRecyclerViewAdapt";
    private List<Photo> mPhotoList; // list of photos
    private Context mContext;

    // make constructors
    public FlickrRecyclerViewAdapter(List<Photo> photoList, Context context) {
        mPhotoList = photoList;
        mContext = context;
    }

    // make override methods

    @NonNull
    @Override
    public FlickrImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       Log.d(TAG, "onCreateViewHolder: new view requested");
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.browse,parent,false);

        return new FlickrImageViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull FlickrImageViewHolder holder, int position) {
         if(mPhotoList==null || (mPhotoList.size())==0)
        {
            holder.thumbnail.setImageResource(R.drawable.placeholder);
            holder.title.setText("No photos match your search.\n\n Use the search icon to search for photos");
        }
        else
        {
            Photo photoItem=mPhotoList.get(position); // retrieved the current photo object from list, recycler view helps us here because it tells us the position of the data we need in the position parameter so then consequently we can just retrieve the exact photo from our arrayList that we have already saved in this object.
            Log.d(TAG, "onBindViewHolder: "+photoItem.getTitle()+" --> "+position);
            Picasso.with(mContext).load(photoItem.getImage())
                    .error(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .into(holder.thumbnail);

            holder.title.setText(photoItem.getTitle()); // put title into text view
        }
       }

    @Override
    public int getItemCount() {
        // returns no of photos in the list
        Log.d(TAG, "getItemCount: called");
        return ((mPhotoList!=null) && (mPhotoList.size()!=0) ? mPhotoList.size() :1);
    }

   void loadNewData(List<Photo> newPhotos){
        mPhotoList=newPhotos;
        notifyDataSetChanged();
    }

   public Photo getPhoto(int position){
       return ((mPhotoList!=null) && (mPhotoList.size()!=0) ? mPhotoList.get(position) :null);
    }


   static class FlickrImageViewHolder extends RecyclerView.ViewHolder{
        private static final String TAG = "FlickrImageViewHolder";
        ImageView thumbnail = null;
        TextView title=null;

        public FlickrImageViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.d(TAG, "FlickrImageViewHolder: starts");
            this.thumbnail=(ImageView) itemView.findViewById(R.id.thumbnail);
            this.title=(TextView) itemView.findViewById(R.id.titles);

        }
    }
}
