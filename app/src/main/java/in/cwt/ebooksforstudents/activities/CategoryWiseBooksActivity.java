package in.cwt.ebooksforstudents.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import in.cwt.ebooksforstudents.R;
import in.cwt.ebooksforstudents.adapters.BooksAdapter;
import in.cwt.ebooksforstudents.listener.ListItemClickListener;
import in.cwt.ebooksforstudents.model.ImgUpload;
import in.cwt.ebooksforstudents.utilities.AppUtilities;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CategoryWiseBooksActivity extends AppCompatActivity {
    private Activity mActivity;
    private Context mContext;
    private ImageView goback;
    private TextView toolbarname;
    private ImageView toolbarimage;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView categorybooks_RV;
    String toolbartitle, toolbarimg;

    private List<ImgUpload> mUploads;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDataBaseRef;
    private BooksAdapter mAdapter;
    private LinearLayout relativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_category_wise_books);

        Intent intent = getIntent();
        if (intent != null){
            toolbartitle = intent.getStringExtra( "cname" );
            toolbarimg = intent.getStringExtra( "imgurl" );
        }

        mActivity = CategoryWiseBooksActivity.this;
        mContext = mActivity.getApplicationContext();

        Uri imageuri = Uri.parse( toolbarimg );
        mUploads = new ArrayList<>();
        goback = findViewById( R.id.imBack );
        toolbarname = findViewById( R.id.TVTitle );
        toolbarimage = findViewById( R.id.category_img );
        swipeRefreshLayout = findViewById( R.id.swipeRefreshLayout );
        relativeLayout =findViewById( R.id.RLnodata);
        categorybooks_RV = findViewById( R.id.rvCategory_books );
        categorybooks_RV.setHasFixedSize( true );
        categorybooks_RV.setLayoutManager( new GridLayoutManager(CategoryWiseBooksActivity.this, 2, RecyclerView.VERTICAL, false ) );

        toolbarname.setText( toolbartitle );

        Glide.with(mContext)
                .load(imageuri)
                .into(toolbarimage);

// Initialize the Audience Network SDK

        LoadData();


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                categorybooks_RV.setVisibility( View.GONE);
                mUploads.clear();
                LoadData();
            }
        });

        goback.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CategoryWiseBooksActivity.super.onBackPressed();
            }
        } );



    }




    private void LoadData(){

        mDataBaseRef = FirebaseDatabase.getInstance().getReference( "Books" );
        mDataBaseRef.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUploads.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    ImgUpload upload = postSnapshot.getValue( ImgUpload.class );


                    assert upload != null;
                    if (upload.getCname().equals(toolbartitle)){

                        mUploads.add(upload);
                    }
                }
                categorybooks_RV.setVisibility(View.VISIBLE);
                Collections.reverse( mUploads );

                if (mUploads.isEmpty()){
                    categorybooks_RV.setVisibility( View.GONE );
                    relativeLayout.setVisibility( View.VISIBLE );
                }
                BooksAdapter mAdapter = new BooksAdapter( CategoryWiseBooksActivity.this, mUploads);

                categorybooks_RV.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                mAdapter.setOnItemClickListener( CategoryWiseBooksActivity.this);

                mAdapter.setItemClickListener( new ListItemClickListener() {
                    @Override
                    public void onItemClick(int position, View view) {

                        ImgUpload imgUpload = mUploads.get( position );
                        final String vu = mUploads.get( position ).getBookLink();
                        final String vn = mUploads.get( position ).getfileName();
                        switch (view.getId()) {

                            case R.id.card_view_top:

                                Intent myIntent = new Intent(CategoryWiseBooksActivity.this, PdfActivity.class);
                                myIntent.putExtra( "book_url", imgUpload.getBookLink() );
                                myIntent.putExtra( "book_name", imgUpload.getfileName() );
                                myIntent.putExtra( "cname", imgUpload.getCname() );
                                startActivity( myIntent );

                                break;

                        }
                    }
                } );


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                AppUtilities.showToast( CategoryWiseBooksActivity.this, "Error: " + error );

            }
        } );

        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}