package in.cwt.ebooksforstudents.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import in.cwt.ebooksforstudents.R;
import in.cwt.ebooksforstudents.adapters.CategoriesAdapter;
import in.cwt.ebooksforstudents.listener.ListItemClickListener;
import in.cwt.ebooksforstudents.model.Cname;
import in.cwt.ebooksforstudents.utilities.AppUtilities;

public class CategoriesActivity extends AppCompatActivity {

    private Activity mActivity;
    private Context mContext;

    private ArrayList<Cname> mcnames;
    private CategoriesAdapter mAdapter = null;
    private RecyclerView mRecycler;

    private RelativeLayout relativeLayout;
    private ImageView gohome;

    // Firebase Database
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_categories );
        mActivity = CategoriesActivity.this;
        mContext = mActivity.getApplicationContext();

        mcnames = new ArrayList<>();

        relativeLayout = findViewById( R.id.category_mainrl );
        gohome = findViewById( R.id.imBack );
        mRecycler = (RecyclerView) findViewById(R.id.rvCategories);
        mRecycler.setLayoutManager(new GridLayoutManager(mActivity, 2, GridLayoutManager.VERTICAL, false));
        mAdapter = new CategoriesAdapter(mContext, mActivity, mcnames);
        mRecycler.setAdapter(mAdapter);
        loadData();



        gohome.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CategoriesActivity.super.onBackPressed();
            }
        } );






    }

    private void loadData(){

        FirebaseApp.initializeApp(this);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("categories");

        mDatabaseReference.orderByChild( "cname" ).addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mcnames.clear();
                for (DataSnapshot contentSnapShot : snapshot.getChildren()) {
                    Cname cname = contentSnapShot.getValue(Cname.class);
                    mcnames.add(cname);
                }
                mRecycler.setVisibility(View.VISIBLE);
                mAdapter.notifyDataSetChanged();

                // recycler list item click listener
                mAdapter.setItemClickListener(new ListItemClickListener() {
                    @Override
                    public void onItemClick(int position, View view) {
                        Cname cname = mcnames.get(position);

                        switch (view.getId()) {
                            case R.id.lyt_container:

                                Intent myIntent = new Intent(CategoriesActivity.this, CategoryWiseBooksActivity.class);
                                myIntent.putExtra( "imgurl", cname.getCpic() );
                                myIntent.putExtra( "cname", cname.getCname() );
                                startActivity( myIntent );

                                break;
                            default:
                                break;
                        }
                    }

                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                AppUtilities.showToast( CategoriesActivity.this, "Error: " + error );
            }
        } );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}