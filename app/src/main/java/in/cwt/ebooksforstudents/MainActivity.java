package in.cwt.ebooksforstudents;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import in.cwt.ebooksforstudents.activities.CategoriesActivity;
import in.cwt.ebooksforstudents.activities.WebView;
import in.cwt.ebooksforstudents.fragments.TwoFragment;
import in.cwt.ebooksforstudents.model.Slider;
import in.cwt.ebooksforstudents.model.Update;
import in.cwt.ebooksforstudents.utilities.AppUtilities;

import com.github.javiersantos.appupdater.AppUpdater;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.animations.DescriptionAnimation;
import com.glide.slider.library.slidertypes.BaseSliderView;
import com.glide.slider.library.slidertypes.TextSliderView;
import com.glide.slider.library.tricks.ViewPagerEx;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ViewPagerEx.OnPageChangeListener {

    private static final String TAG = "MyTab";
    TabLayout mTabLayout;
    ViewPager mViewPager;
    Adapter mPagerAdapter;
    List<String> propertyAddressList = new ArrayList<String>();
    List<String> tabItems = new ArrayList<>();
    private DatabaseReference mDatabaseRef, myRef, scrollingRef, mDatabasesliderRef;
    private SharedPreferences.Editor editor;
    private SharedPreferences preferences;
    private String MY_PREFS_NAME = "MyPrefsFile";
    private List<Update> mUpdate;
    private TextView scrollingtext;
    String ScrollingText;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDataBaseRef;
    String showslider = "no", sliderclick, sliderimage, slidername;
    RelativeLayout relativeLayout;
    private DatabaseReference sliderref;
    private SliderLayout mDemoSlider;
    CardView cardView;
    private static final String ARG_PARAM1 = "yes";
    private static final String ARG_PARAM2 = "no";

    List<String> PropertyAddressList;
    List<String> thumbAddressList;
    List<String> clickAddressList;

    List<Slider> mSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.app_name);
        FirebaseApp.initializeApp(this);
        mDatabase = FirebaseDatabase.getInstance();
        mDatabasesliderRef = mDatabase.getReference();
        mUpdate = new ArrayList<>();
        mSlider = new ArrayList<>();
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mDemoSlider = findViewById(R.id.slider);
        mDemoSlider.setVisibility(View.GONE);
        cardView = findViewById(R.id.slidercard);
        cardView.setVisibility(View.GONE);
        preferences = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        editor = preferences.edit();
        makeList();
        scrollingtext = findViewById(R.id.tvFlash);
        scrollingtext.setSelected(true);


        Scrolling();
        loadslider();


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


    }

    private void makeList() {
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("categories");
        mDatabaseRef.orderByChild("cname").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                propertyAddressList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String propertyAddress = ds.child("cname").getValue(String.class);
                    propertyAddressList.add(propertyAddress);
                    if (propertyAddressList.size() == dataSnapshot.getChildrenCount()) {
                        settingViewPager(mViewPager, propertyAddressList);
                        mTabLayout.setupWithViewPager(mViewPager);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                AppUtilities.showToast(MainActivity.this, "Error: " + error);
            }
        });

    }

    private void settingViewPager(ViewPager mViewPager, List<String> propertyAddressList) {

        Adapter adapter = new Adapter(getSupportFragmentManager());
        TwoFragment fragment = new TwoFragment();
        for (int i = 0; i < propertyAddressList.size(); i++) {
            Bundle bundle = new Bundle();
            bundle.putString("title", propertyAddressList.get(i));
            fragment.setArguments(bundle);
            adapter.addFragment(fragment, propertyAddressList.get(i));
            fragment = new TwoFragment();
        }
        mViewPager.setAdapter(adapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_categories) {

            Intent category = new Intent(MainActivity.this, CategoriesActivity.class);
            startActivity(category);

        } else if (id == R.id.nav_privacy) {

            String privacyurl = "https://www.udemy.com/course/google-drive-2020-complete-guide-from-beginner-to-expert";

            Intent intent = new Intent(MainActivity.this, WebView.class);
            intent.putExtra("URL", privacyurl);
            intent.putExtra("Title", "Privacy Policy");
            startActivity(intent);

        } else if (id == R.id.nav_send) {

            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", "girijaalladi@gmail.com", null));
            startActivity(Intent.createChooser(intent, "Choose an Email App :"));


        } else if (id == R.id.nav_update) {

            AppUpdater appUpdater = new AppUpdater(MainActivity.this)
                    .setContentOnUpdateAvailable("Check out the latest version available to get the latest features and bug fixes")
                    .setCancelable(false)
                    .setButtonDoNotShowAgain(null)
                    .setButtonUpdate("Update now")
                    .setButtonDismiss("later")
                    .showAppUpdated(true)
                    .setTitleOnUpdateNotAvailable("Update not available")
                    .setContentOnUpdateNotAvailable("No update available. Check for updates again later!");
            appUpdater.start();
            AppUtilities.showToast(MainActivity.this, "Wait to check update");
        } else if (id == R.id.nav_share) {

            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
            String sAux = "Hey Check Our App Telugu Daily NewsPapers App \n\n";
            sAux = sAux + "http://play.google.com/store/apps/details?id=" + getPackageName();
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "Share With"));

        } else if (id == R.id.nav_rate) {

            Uri uri = Uri.parse("market://details?id=" + getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
            }


        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class Adapter extends FragmentPagerAdapter {

        List<String> list = new ArrayList<>();
        List<Fragment> fragmentList = new ArrayList<>();

        public void addFragment(Fragment fragment, String title) {
            list.add(title);
            fragmentList.add(fragment);
        }

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return list.get(position);
        }
    }


    private void UpdatePopup() {

        myRef = FirebaseDatabase.getInstance().getReference("Update");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                mUpdate.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Update update = postSnapshot.getValue(Update.class);

                    mUpdate.add(update);
                    String version_name = BuildConfig.VERSION_NAME;
                    assert update != null;
                    if (update.getUpdateversion().equals(version_name)) {

                        Log.d(TAG, version_name);
                    } else {
                        final Uri uri = Uri.parse(update.getUpdatelink());
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle(update.getUpdatetitle())
                                .setMessage(update.getUpdatetext())
                                .setCancelable(false)
                                .setPositiveButton(update.getPositive(), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        startActivity(new Intent(Intent.ACTION_VIEW,
                                                uri));

                                    }
                                }).create().show();


                    }


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                AppUtilities.showToast(MainActivity.this, "Error: " + error);
            }
        });


    }

    private void Scrolling() {

        scrollingRef = FirebaseDatabase.getInstance().getReference("Scrolling Text");

        scrollingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ScrollingText = snapshot.getValue(String.class);
                assert ScrollingText != null;
                if (!ScrollingText.equals("null")) {
                    scrollingtext.setVisibility(View.VISIBLE);
                    scrollingtext.setText(ScrollingText);
                } else {
                    scrollingtext.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                AppUtilities.showToast(MainActivity.this, "Error: " + error);

            }
        });

    }


    private void loadslider() {

        mDatabasesliderRef.child("showslider").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                showslider = dataSnapshot.getValue(String.class);

                if (showslider != null && showslider.equals(ARG_PARAM1)) {
                    mDemoSlider.setVisibility(View.VISIBLE);
                    cardView.setVisibility(View.VISIBLE);
                }

                if (showslider != null && showslider.equals(ARG_PARAM2)) {
                    mDemoSlider.setVisibility(View.GONE);
                    cardView.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {

            }
        });


        sliderref = FirebaseDatabase.getInstance().getReference("slider");

        sliderref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mDemoSlider.removeAllSliders();
                mSlider.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Slider nslider = ds.getValue(Slider.class);

                    String Show = "yes";
                    assert nslider != null;
                    if (nslider.getShow() != null && nslider.getShow().equals(Show)) {
                        mSlider.add(nslider);

                        RequestOptions requestOptions = new RequestOptions();
                        requestOptions.centerCrop().diskCacheStrategy(DiskCacheStrategy.RESOURCE);

                        TextSliderView sliderView = new TextSliderView(MainActivity.this);
                        // if you want show image only / without description text use DefaultSliderView instead

                        // initialize SliderLayout
                        sliderView.image(nslider.getImglink())
                                .description(nslider.getSname())
                                .setRequestOption(requestOptions)
                                .setProgressBarVisible(true)
                                .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                    @Override
                                    public void onSliderClick(BaseSliderView slider) {

                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        intent.setData(Uri.parse(nslider.getClick()));
                                        startActivity(intent);

                                    }
                                });

                        //add your extra information
                        sliderView.bundle(new Bundle());
                        sliderView.getBundle().putString("extra", nslider.getSname());
                        mDemoSlider.addSlider(sliderView);

                        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
                        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
                        mDemoSlider.setDuration(5000);
                        mDemoSlider.addOnPageChangeListener(MainActivity.this);
                        mDemoSlider.stopCyclingWhenTouch(false);
                    } else if (nslider.getShow().equals(ARG_PARAM2)) {

                        mSlider.remove(nslider);
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                AppUtilities.showToast(MainActivity.this, "Error: " + error.getMessage());

            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        UpdatePopup();
    }

}