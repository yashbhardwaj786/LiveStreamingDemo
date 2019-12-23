package com.arya.livestreaming;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class VideoDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private String video_name;
    private VideoDetailActivity ctx = this;
    private ProgressDialog d;
    private VideoView videoview;
    private MediaController mediacontroller;
    private int length = 0;
    private boolean status = false;
    private String VideoURL;
    private LinearLayout bottomLayout, headerLL, fullScreen;
    private RelativeLayout upperLayout;
    private ImageView back, share;
    private ScrollView ScrollView;
    private TextView about_episode;


    private LinearLayout mGallery;
    private int[] covers;
    private String[] playBackURL;
    private String[] countArr;
    private String[] nameArr;
    private LayoutInflater mInflater;
    private HorizontalScrollView horizontalScrollView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);

        video_name = getIntent().getStringExtra("video_name");
        VideoURL = getIntent().getStringExtra("video_url");

        mGallery = (LinearLayout) findViewById(R.id.id_gallery);
        back = (ImageView) findViewById(R.id.back);
        share = (ImageView) findViewById(R.id.share);
        videoview = (VideoView) findViewById(R.id.VideoView);
        fullScreen = (LinearLayout) findViewById(R.id.fullScreen);
        bottomLayout = (LinearLayout) findViewById(R.id.bottomLayout);
        ScrollView = (ScrollView) findViewById(R.id.ScreollView);
        headerLL = (LinearLayout) findViewById(R.id.headerLL);
        upperLayout = (RelativeLayout) findViewById(R.id.upperLayout);
        about_episode = (TextView) findViewById(R.id.about_episode);

        mInflater = LayoutInflater.from(this);
        initData();
        initView();


        bufferAndPlayVideo(VideoURL, video_name);

        // click on expandable buuton to change orientation of video play
        fullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ctx.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
                else if(ctx.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }

            }
        });

        // back button click listener
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // share video URL using share intents
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Check Out "+video_name+" on "+getResources().getString(R.string.app_name)+" "+VideoURL);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });


    }

    // Method for Tollbar
    private Toolbar setToolbar(final String name) {
        Toolbar appbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(appbar);

        TextView header = (TextView) findViewById(R.id.header);
        header.setText(name);

        return appbar;
    }

    // as the device orientation is changed
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            landscapeSettings();  // call landscape settings

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){

            portraitSettings();    // call portrait settings
        }
    }

    // method definition for landscape setting
    private void landscapeSettings()
    {
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                0,
                1.0f
        );
        upperLayout.setLayoutParams(param);
        bottomLayout.setVisibility(View.GONE);
        ScrollView.setVisibility(View.GONE);
        headerLL.setVisibility(View.GONE);
    }

     // method definition for portrait setting
    private void portraitSettings()
    {
        LinearLayout.LayoutParams param_top = new LinearLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                0,
                0.4f
        );
        upperLayout.setLayoutParams(param_top);

        bottomLayout.setVisibility(View.VISIBLE);
        ScrollView.setVisibility(View.VISIBLE);
        headerLL.setVisibility(View.VISIBLE);

        LinearLayout.LayoutParams param_bootom = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                0.6f
        );
        ScrollView.setLayoutParams(param_bootom);
    }

    // show loading code.
    public static ProgressDialog showLoading(Activity activity) {
        ProgressDialog mProgressDialog = new ProgressDialog(activity);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        if (!activity.isFinishing() && !mProgressDialog.isShowing())
            mProgressDialog.show();
        return mProgressDialog;
    }

    // method definition for video buffer and play
    private void bufferAndPlayVideo(String url, String name)
    {
        setToolbar(name);
        // Create a progressbar
        d = showLoading(ctx);
        d.setCanceledOnTouchOutside(false);
        d.setCancelable(false);

        about_episode.setText(name + " "+getResources().getString(R.string.about_episode));
        try {
            // Start the MediaController
            mediacontroller = new MediaController(ctx);
            mediacontroller.setAnchorView(videoview);
            // Get the URL from String VideoURL
            Uri video = Uri.parse(url);
            videoview.setMediaController(mediacontroller);
            videoview.setVideoURI(video);

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        videoview.requestFocus();

        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                d.dismiss();
                videoview.start();
            }
        });

        //finish after playing
        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                length = 0;
                videoview.seekTo(length);
                status = true;

            }
        });
    }

    // horizontal scrool view data initialization
    private void initData()
    {
        covers = new int[]{ R.drawable.album1, R.drawable.album2, R.drawable.album3, R.drawable.album4, R.drawable.album5};

        playBackURL = new String[]
                {
                        "https://archive.org/download/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4",
                        "http://43.224.1.48:8080/hari_radhe/media/mmm.mp4",
                        "http://www.sample-videos.com/video/mp4/720/big_buck_bunny_720p_1mb.mp4",
                        "http://techslides.com/demos/sample-videos/small.mp4",
                        "https://archive.org/download/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4"
                };

        countArr  = new String[]{"13","8","11","12","14"};

        nameArr  = new String[]{"True Romance","Xscpae","Maroon 5","Born to Die","Honeymoon"};


    }

    // horizontal scroll view for view initialization and  set data in each item

    private void initView()
    {
        mGallery = (LinearLayout) findViewById(R.id.id_gallery);

        for (int i = 0; i < covers.length; i++)
        {

            View view = mInflater.inflate(R.layout.activity_gallery_item,
                    mGallery, false);
            ImageView img = (ImageView) view
                    .findViewById(R.id.albumImage);
            img.setImageResource(covers[i]);
            TextView title = (TextView) view .findViewById(R.id.title);
            title.setText(nameArr[i]);
            TextView count = (TextView) view .findViewById(R.id.count);
            count.setText(countArr[i]+" Songs");
            LinearLayout rootLL = (LinearLayout)view.findViewById(R.id.rootLL);
            rootLL.setTag(R.string.url, playBackURL[i]);
            rootLL.setTag(R.string.name, nameArr[i]);
            rootLL.setOnClickListener((VideoDetailActivity) ctx);
            mGallery.addView(view);
        }
    }

    // handle on click for horizontal scroll view item
    @Override
    public void onClick(View v) {
        if (videoview != null) {
            videoview.pause();
            videoview.seekTo(0);
            status = true;
        }
        String url = (String)v.getTag(R.string.url);
        String name = (String)v.getTag(R.string.name);
        System.out.println("hh yasal "+url);
        bufferAndPlayVideo(url, name);
    }

    // as activity go on onpause (press home button or device) the video will be pause
    @Override
    protected void onPause() {
        super.onPause();

        videoview.pause();
        length = videoview.getCurrentPosition();
        System.out.println("hh yashal length on pause "+length);

    }

    // as activity go on onResume (after going home again launch in app) the video will be pause
    @Override
    protected void onResume() {
        super.onResume();

        if (length > 0)
        {
            d = showLoading(ctx);
            d.setCanceledOnTouchOutside(false);
            d.setCancelable(false);
            System.out.println("hh yashal length on resume "+length);
            videoview.resume();
            videoview.seekTo(length);
            if (videoview.isPlaying())
            {
                d.dismiss();
            }
        }


    }
}
