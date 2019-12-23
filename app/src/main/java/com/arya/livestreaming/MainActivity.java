package com.arya.livestreaming;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private AlbumsAdapter adapter;
    private List<Album> albumList;
    private ConnectionDetector cd;
    private CoordinatorLayout main_content;
    private ConnectionReceiver myBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initCollapsingToolbar();

        cd = new ConnectionDetector(getApplicationContext());
        myBroadcastReceiver = new ConnectionReceiver();
        enableReceiver();
        Intent intent = new Intent();
        sendBroadcast(intent);

        main_content = (CoordinatorLayout) findViewById(R.id.main_content);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        albumList = new ArrayList<>();
        adapter = new AlbumsAdapter(this, albumList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareAlbums();

        try {
            Glide.with(this).load(R.drawable.cover).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    /**
     * Adding few albums for testing
     */
    private void prepareAlbums() {
        int[] covers = new int[]{
                R.drawable.album1,
                R.drawable.album2,
                R.drawable.album3,
                R.drawable.album4,
                R.drawable.album5,
                R.drawable.album6,
                R.drawable.album7,
                R.drawable.album8,
                R.drawable.album9,
                R.drawable.album10,
                R.drawable.album11};

        String[] playBackURL = new String[]
                {
                        "http://www.androidbegin.com/tutorial/AndroidCommercial.3gp",
                        "https://archive.org/download/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4",
                        "http://43.224.1.48:8080/hari_radhe/media/mmm.mp4",
                        "http://www.sample-videos.com/video/mp4/720/big_buck_bunny_720p_1mb.mp4",
                        "http://techslides.com/demos/sample-videos/small.mp4",
                        "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4",
                        "https://archive.org/download/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4",
                        "http://43.224.1.48:8080/hari_radhe/media/mmm.mp4",
                        "http://www.sample-videos.com/video/mp4/720/big_buck_bunny_720p_1mb.mp4",
                        "http://techslides.com/demos/sample-videos/small.mp4",
                        "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"
                };

        Album a = new Album(playBackURL[0], "True Romance", 13, covers[0]);
        albumList.add(a);

        a = new Album(playBackURL[1], "Xscpae", 8, covers[1]);
        albumList.add(a);

        a = new Album(playBackURL[2], "Maroon 5", 11, covers[2]);
        albumList.add(a);

        a = new Album(playBackURL[3], "Born to Die", 12, covers[3]);
        albumList.add(a);

        a = new Album(playBackURL[4], "Honeymoon", 14, covers[4]);
        albumList.add(a);

        a = new Album(playBackURL[5], "I Need a Doctor", 1, covers[5]);
        albumList.add(a);

        a = new Album(playBackURL[6], "Loud", 11, covers[6]);
        albumList.add(a);

        a = new Album(playBackURL[7], "Legend", 14, covers[7]);
        albumList.add(a);

        a = new Album(playBackURL[8], "Hello", 11, covers[8]);
        albumList.add(a);

        a = new Album(playBackURL[9], "Greatest Hits", 17, covers[9]);
        albumList.add(a);

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {

        int position = (int) v.getTag(R.string.pos);
        int track = (int) v.getTag(R.string.track);
        int img = (int) v.getTag(R.string.img);
        String name = (String) v.getTag(R.string.name);
        String url = (String) v.getTag(R.string.url);

        System.out.println("hh yashal position is " + position);
        System.out.println("hh yashal track is " + track);
        System.out.println("hh yashal img is " + img);
        System.out.println("hh yashal name is " + name);
        System.out.println("hh yashal url is " + url);

        if (cd.getNetworkInfo() != null) {
            Intent intent = new Intent(MainActivity.this, VideoDetailActivity.class);
            intent.putExtra("video_name", name);
            intent.putExtra("video_url", url);
            startActivity(intent);
        } else {
            showSnackBar(main_content, "No internet connection available");
        }


    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    // Show snack bar method
    public void showSnackBar(View rootlayout, String message) {
        Snackbar snackbar = Snackbar
                .make(rootlayout, message, Snackbar.LENGTH_LONG)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });

        // Changing message text color
        snackbar.setActionTextColor(Color.BLACK);

        // Changing action button text color
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.white));
        TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.RED);

        snackbar.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disableReceiver();
        unregisterReceiver(myBroadcastReceiver);
    }


    public void enableReceiver() {

        IntentFilter filter1 = new IntentFilter();
        filter1.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(myBroadcastReceiver, filter1);

        ComponentName receiver = new ComponentName(this, ConnectionReceiver.class);
        PackageManager pm = this.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
//        Toast.makeText(this, "Enabled broadcast receiver", Toast.LENGTH_SHORT).show();
    }

    public void disableReceiver() {
        ComponentName receiver = new ComponentName(this, ConnectionReceiver.class);
        PackageManager pm = this.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
//        Toast.makeText(this, "Disabled broadcst receiver", Toast.LENGTH_SHORT).show();
    }
}
