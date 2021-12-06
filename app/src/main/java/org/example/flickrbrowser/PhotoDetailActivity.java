package org.example.flickrbrowser;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.squareup.picasso.Picasso;

public class PhotoDetailActivity extends BaseActivity {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        activateToolbar(true);

        Intent intent=getIntent();
        Photo photo=(Photo) intent.getSerializableExtra(PHOTO_TRANSFER); if(photo!=null){
            TextView photoTitle=(TextView) findViewById(R.id.photo_title);

            Resources resources=getResources();
            String text=resources.getString(R.string.photo_title_text,photo.getTitle());  photoTitle.setText(text);

            TextView photoTags=(TextView) findViewById(R.id.photo_tags);
            photoTags.setText(resources.getString(R.string.photo_title_tags,photo.getTags()));

            TextView photoAuthor=(TextView) findViewById(R.id.photo_author);
            photoAuthor.setText(photo.getAuthor());

            ImageView photoImage=(ImageView) findViewById(R.id.photo_image);
            Picasso.with(this).load(photo.getLink())
                    .error(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .into(photoImage); // .into is where we store the downloaded image.
        }
    }
}