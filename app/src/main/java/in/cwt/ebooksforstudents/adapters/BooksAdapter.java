package in.cwt.ebooksforstudents.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import in.cwt.ebooksforstudents.R;
import in.cwt.ebooksforstudents.listener.ListItemClickListener;
import in.cwt.ebooksforstudents.model.ImgUpload;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.ImageViewHolder> {
    private Context mContext;
    private List<ImgUpload> mUploads;
    private OnItemClickListener mListener;
    Bitmap bmImg = null;
    private ListItemClickListener mItemClickListener;
    private Activity BooksActivity;
    private static final String TAG = "Link";


    public BooksAdapter(Context context, List<ImgUpload> Images ) {
        mContext = context;
        mUploads = Images;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate( R.layout.books_item, parent, false);
        return new ImageViewHolder(v,mItemClickListener);
    }

    public void setItemClickListener(ListItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public void onBindViewHolder(final ImageViewHolder holder, final int position) {

        final ImgUpload imgUpload = mUploads.get( position );
        String imgeurl = imgUpload.getfileName();
        String bookUrl = imgUpload.getBookLink();
        holder.textViewName.setText(imgeurl);
        Uri name = Uri.parse( bookUrl );


        // create a ProgressDrawable object which we will show as placeholder
        CircularProgressDrawable drawable = new CircularProgressDrawable(mContext);
        drawable.setColorSchemeColors(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent);
        drawable.setCenterRadius(30f);
        drawable.setStrokeWidth(5f);
        // set all other properties as you would see fit and start it
        drawable.start();

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(drawable);
        requestOptions.error(R.drawable.ic_eye_24);
        String thumbnail = imgUpload.getThumbnail();
        if (thumbnail != null && !thumbnail.isEmpty()) {
        Glide.with( mContext )
                .load( thumbnail )
                .apply(requestOptions)
               .into( holder.imageView );

        }
        Log.d( TAG, "KEY = " + thumbnail );

//Use "https://lh3.googleusercontent.com/d/fileid=w320?authuser=0"
        //use https://drive.google.com/thumbnail?authuser=0&sz=w320&id=fileid
    }
    // tried to get filename from drive url
    public String getFilenameFromURL(String url) {
        try {
            return new File(new URL(url).getPath()).getName()+".pdf";
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return System.currentTimeMillis() + ".pdf";
        }
    }


    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public void setOnItemClickListener(FragmentActivity activity) {
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        public CardView cardView;
        public ImageView imageView;
        public Button button;
        public AppCompatTextView textViewName;
        private ListItemClickListener itemClickListener;

        public ImageViewHolder(View itemView , ListItemClickListener itemClickListener) {
            super(itemView);

            this.itemClickListener = itemClickListener;

            cardView = itemView.findViewById( R.id.card_view_top );
            textViewName = itemView.findViewById( R.id.title_text);
            imageView = itemView.findViewById( R.id.post_img);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
            cardView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(getLayoutPosition(), v);
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {

                }
            }
            return false;
        }
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}
