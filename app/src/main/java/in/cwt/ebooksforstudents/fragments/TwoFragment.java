package in.cwt.ebooksforstudents.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import in.cwt.ebooksforstudents.R;
import in.cwt.ebooksforstudents.activities.PdfActivity;
import in.cwt.ebooksforstudents.activities.PdfActivitytest;
import in.cwt.ebooksforstudents.adapters.BooksAdapter;
import in.cwt.ebooksforstudents.listener.ListItemClickListener;
import in.cwt.ebooksforstudents.model.ImgUpload;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TwoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TwoFragment extends Fragment {
    private Activity activity;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String Title = "title";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String mtitle;
    private TextView textView;
    private RecyclerView mRecyclerView;
    private BooksAdapter mAdapter;
    List<String> tabtitles = new ArrayList<>();
    private List<ImgUpload> mUploads;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDataBaseRef;
    private FragmentManager fragmentManager;
    private static final String TAG = "MyFrag";
    private String tabName;
    private LinearLayout relativeLayout;

    public TwoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = getActivity();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TwoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TwoFragment newInstance(String param1, String param2, String title) {
        TwoFragment fragment = new TwoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putCharSequence(Title, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mtitle = getArguments().getString(Title);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_two, container, false);
        final String sTitle = getArguments().getString("title");
        mUploads = new ArrayList<>();
        relativeLayout = view.findViewById( R.id.RLnodata );
        mRecyclerView = view.findViewById( R.id.recycler_fragment );
        mRecyclerView.setHasFixedSize( true );
        mRecyclerView.setLayoutManager( new GridLayoutManager( getActivity(), 2, RecyclerView.VERTICAL, false ) );

        mDataBaseRef = FirebaseDatabase.getInstance().getReference( "Books" );
        mDataBaseRef.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUploads.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ImgUpload upload = postSnapshot.getValue( ImgUpload.class );

                    Fragment fragment  = new TwoFragment();
                    //getFragmentManager().findFragmentById(R.id.Fragment_main);

                    assert upload != null;
                    if (upload.getCname().equals(sTitle)){
                        mUploads.add(upload);
                    }
                }
                Collections.reverse( mUploads );

                if (mUploads.isEmpty()){
                    mRecyclerView.setVisibility( View.GONE );
                    relativeLayout.setVisibility( View.VISIBLE );
                }
                BooksAdapter mAdapter = new BooksAdapter( getActivity(), mUploads);

                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                mAdapter.setOnItemClickListener( getActivity() );
                mAdapter.setItemClickListener( new ListItemClickListener() {
                    @Override
                    public void onItemClick(int position, View view) {

                        ImgUpload imgUpload = mUploads.get( position );
                        final String vu = mUploads.get( position ).getBookLink();
                        final String vn = mUploads.get( position ).getfileName();
                        switch (view.getId()) {

                            case R.id.card_view_top:
                                Intent myIntent = new Intent(getActivity(), PdfActivity.class);
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
                Toast.makeText( getActivity(), error.getMessage(), Toast.LENGTH_SHORT ).show();
                Toast.makeText( getActivity(), "Please Check Your Internet Connection", Toast.LENGTH_LONG ).show();
            }
        } );
        return view;
    }

}