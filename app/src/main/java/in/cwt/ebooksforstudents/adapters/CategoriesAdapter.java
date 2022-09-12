package in.cwt.ebooksforstudents.adapters;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import in.cwt.ebooksforstudents.R;
import in.cwt.ebooksforstudents.listener.ListItemClickListener;
import in.cwt.ebooksforstudents.model.Cname;

import java.util.ArrayList;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder>{
    private Context mContext;
    private Activity mActivity;
    private ArrayList<Cname> mcnames;
    private ListItemClickListener mItemClickListener;


    public CategoriesAdapter(Context mContext, Activity mActivity, ArrayList<Cname> mCategoryList) {
        this.mContext = mContext;
        this.mActivity = mActivity;
        this.mcnames = mCategoryList;
    }

    public void setItemClickListener(ListItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate( R.layout.item_category_recycler, parent, false);
        return new ViewHolder(view, viewType, mItemClickListener);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView categoryImg;
        private TextView categoryTitle;
        private RelativeLayout lytContainer;
        private ListItemClickListener itemClickListener;


        public ViewHolder(View itemView, int viewType, ListItemClickListener itemClickListener) {
            super(itemView);

            this.itemClickListener = itemClickListener;

            categoryImg = (ImageView) itemView.findViewById(R.id.category_img);
            categoryTitle = (TextView) itemView.findViewById(R.id.category_name);
            lytContainer = (RelativeLayout) itemView.findViewById(R.id.lyt_container);
            lytContainer.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(getLayoutPosition(), view);
            }
        }
    }

    @Override
    public int getItemCount() {
        return (null != mcnames ? mcnames.size() : 0);
    }
    @Override
    public void onBindViewHolder(ViewHolder mainHolder, int position) {
        final Cname model = mcnames.get(position);

        // setting data over views
        String imgUrl = model.getCpic();
        if (imgUrl != null && !imgUrl.isEmpty()) {
            Glide.with(mContext)
                    .load(imgUrl)
                    .into(mainHolder.categoryImg);
        }
        mainHolder.categoryTitle.setText( Html.fromHtml(model.getCname()));
    }

}
