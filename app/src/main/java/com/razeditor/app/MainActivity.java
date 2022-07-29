package com.razeditor.app;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.transition.ChangeBounds;
import android.support.transition.TransitionManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.razeditor.app.base.BaseActivity;
import com.razeditor.app.filters.FilterListener;
import com.razeditor.app.filters.FilterViewAdapter;
import com.razeditor.app.tools.EditingToolsAdapter;
import com.razeditor.app.tools.ToolType;

import java.io.File;
import java.io.IOException;

import ja.burhanrashid52.photoeditor.OnPhotoEditorListener;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;
import ja.burhanrashid52.photoeditor.SaveSettings;
import ja.burhanrashid52.photoeditor.ViewType;
import ja.burhanrashid52.photoeditor.PhotoFilter;

public class MainActivity extends BaseActivity implements OnPhotoEditorListener,
        View.OnClickListener,
        PropertiesBSFragment.Properties,
        EmojiBSFragment.EmojiListener,
        StickerBSFragment.StickerListener,LogoBSFragment.LogoListener,
        HairBSFragment.HairListener,MaksBSFragment.MaksListener, GraphicsBSFragment.GraphicsListener, ActorsBSFragment.ActorsListener, SunglassBSFragment.SunglassListener,
        FrameBSFragment.FrameListener,
        EditingToolsAdapter.OnItemSelected, FilterListener {

    InterstitialAd interstitial;
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String EXTRA_IMAGE_PATHS = "extra_image_paths";
    private static final int CAMERA_REQUEST = 52;
    private static final int PICK_REQUEST = 53;
    private PhotoEditor mPhotoEditor;
    private PhotoEditorView mPhotoEditorView;
    private PropertiesBSFragment mPropertiesBSFragment;
    private EmojiBSFragment mEmojiBSFragment;
    private StickerBSFragment mStickerBSFragment;
    private LogoBSFragment mLogoBSFragment;
    private HairBSFragment mHairBSFragment;
    private MaksBSFragment mMaksBSFragment;
    private FrameBSFragment mFrameBSFragment;
    private SunglassBSFragment mSunglassBSFragment;
    private ActorsBSFragment mActorsBSFragment;
    private GraphicsBSFragment mGraphicsBSFragment;
    private TextView mTxtCurrentTool;
    private Typeface mWonderFont;
    private RecyclerView mRvTools, mRvFilters;
    private EditingToolsAdapter mEditingToolsAdapter = new EditingToolsAdapter(this);
    private FilterViewAdapter mFilterViewAdapter = new FilterViewAdapter(this);
    private ConstraintLayout mRootView;
    private ConstraintSet mConstraintSet = new ConstraintSet();
    private boolean mIsFilterVisible;
    private LinearLayout brush, text, erser,filter, emoji,
            flare,frame,frame_load,flare_load,emoji_load,logo,logo_load,
            hair,hair_load,maks,maks_load,sunglass,sunglass_load,
            actors,actors_load,graphics, graphics_load;

    private TextView time_count,Loading_text;

    ConnectionChecker connectionChecker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeFullScreen();
        setContentView(R.layout.activity_main);

        connectionChecker = new ConnectionChecker(this);

        if (connectionChecker.isConnected()) {





                    // Initialize the Mobile Ads SDK
                    MobileAds.initialize(MainActivity.this, "ca-app-pub-3940256099942544~3347511713");
                    AdRequest adIRequest = new AdRequest.Builder().build();

                    // Prepare the Interstitial Ad Activity
                    interstitial = new InterstitialAd(MainActivity.this);

                    // Insert the Ad Unit ID
                    interstitial.setAdUnitId("ca-app-pub-3940256099942544/6300978111");

                    // Interstitial Ad load Request
                    interstitial.loadAd(adIRequest);

                    // Prepare an Interstitial Ad Listener
                    interstitial.setAdListener(new AdListener()
                    {
                        public void onAdLoaded()
                        {
                            // Call displayInterstitial() function when the Ad loads
                            displayInterstitial();
                        }
                    });


        }
        //Internet checker
        /*else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            // Setting Alert Dialog Title
            alertDialogBuilder.setTitle("Warning!");
            // Icon Of Alert Dialog
            // Setting Alert Dialog Message
            alertDialogBuilder.setMessage("Please check your internet connection");
            alertDialogBuilder.setCancelable(false);

            alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    finish();
                }
            });



            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }*/



        initViews();

        mWonderFont = Typeface.createFromAsset(getAssets(), "beyond_wonderland.ttf");

        mPropertiesBSFragment = new PropertiesBSFragment();
        mEmojiBSFragment = new EmojiBSFragment();
        mStickerBSFragment = new StickerBSFragment();
        mStickerBSFragment.setStickerListener(this);

        mLogoBSFragment = new LogoBSFragment();
        mLogoBSFragment.setLogoListener(this);

        mHairBSFragment = new HairBSFragment();
        mHairBSFragment.setHairListener(this);

        mMaksBSFragment = new MaksBSFragment();
        mMaksBSFragment.setMaksListener(this);

        mFrameBSFragment = new FrameBSFragment();
        mFrameBSFragment.setFrameListener(this);

        mActorsBSFragment = new ActorsBSFragment();
        mActorsBSFragment.setActorsListener(this);

        mGraphicsBSFragment = new GraphicsBSFragment();
        mGraphicsBSFragment.setGraphicsListener(this);

        mSunglassBSFragment = new SunglassBSFragment();
        mSunglassBSFragment.setSunglassListener(this);

        mEmojiBSFragment.setEmojiListener(this);
        mPropertiesBSFragment.setPropertiesChangeListener(this);

        LinearLayoutManager llmTools = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRvTools.setLayoutManager(llmTools);
        mRvTools.setAdapter(mEditingToolsAdapter);

        LinearLayoutManager llmFilters = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRvFilters.setLayoutManager(llmFilters);
        mRvFilters.setAdapter(mFilterViewAdapter);


        //Typeface mTextRobotoTf = ResourcesCompat.getFont(this, R.font.roboto_medium);
        //Typeface mEmojiTypeFace = Typeface.createFromAsset(getAssets(), "emojione-android.ttf");

        mPhotoEditor = new PhotoEditor.Builder(this, mPhotoEditorView)
                .setPinchTextScalable(true) // set flag to make text scalable when pinch
                //.setDefaultTextTypeface(mTextRobotoTf)
                //.setDefaultEmojiTypeface(mEmojiTypeFace)
                .build(); // build photo editor sdk

        mPhotoEditor.setOnPhotoEditorListener(this);

        //Set Image Dynamically
        // mPhotoEditorView.getSource().setImageResource(R.drawable.color_palette);

        time_count = findViewById(R.id.time);
        brush = findViewById(R.id.brush);
        text = findViewById(R.id.text);
        erser = findViewById(R.id.erser);
        filter = findViewById(R.id.filter);
        flare = findViewById(R.id.flare);
        frame = findViewById(R.id.frame);
        frame_load = findViewById(R.id.load_frame);
        emoji = findViewById(R.id.emoji);
        flare_load = findViewById(R.id.flare_load);
        emoji_load = findViewById(R.id.emoji_load);
        Loading_text = findViewById(R.id.txtCurrentTool_load);
        logo = findViewById(R.id.logo);
        logo_load = findViewById(R.id.logo_load);
        actors = findViewById(R.id.actors);
        actors_load = findViewById(R.id.actors_load);
        hair = findViewById(R.id.hair);
        hair_load = findViewById(R.id.hair_load);
        maks = findViewById(R.id.mask);
        maks_load = findViewById(R.id.mask_load);
        graphics = findViewById(R.id.graphics);
        graphics_load = findViewById(R.id.graphics_load);
        sunglass = findViewById(R.id.sunglass);
        sunglass_load = findViewById(R.id.sunglass_load);


        sunglass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSunglassBSFragment.show(getSupportFragmentManager(), mSunglassBSFragment.getTag());
                mTxtCurrentTool.setVisibility(View.GONE);
                sunglass.setVisibility(View.GONE);
                Loading_text.setVisibility(View.VISIBLE);
                sunglass_load.setVisibility(View.VISIBLE);


                new CountDownTimer(5*1000, 1000){
                    @Override
                    public void onTick(long l) {
                        time_count.setText(String.valueOf(l/1000+"s"));
                    }

                    @Override
                    public void onFinish() {

                        mTxtCurrentTool.setVisibility(View.VISIBLE);
                        Loading_text.setVisibility(View.GONE);
                        sunglass.setVisibility(View.VISIBLE);
                        sunglass_load.setVisibility(View.GONE);
                    }
                }.start();
            }
        });

        graphics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGraphicsBSFragment.show(getSupportFragmentManager(), mGraphicsBSFragment.getTag());
                mTxtCurrentTool.setVisibility(View.GONE);
                graphics.setVisibility(View.GONE);
                Loading_text.setVisibility(View.VISIBLE);
                graphics_load.setVisibility(View.VISIBLE);


                new CountDownTimer(5*1000, 1000){
                    @Override
                    public void onTick(long l) {
                        time_count.setText(String.valueOf(l/1000+"s"));
                    }

                    @Override
                    public void onFinish() {

                        mTxtCurrentTool.setVisibility(View.VISIBLE);
                        Loading_text.setVisibility(View.GONE);
                        graphics.setVisibility(View.VISIBLE);
                        graphics_load.setVisibility(View.GONE);
                    }
                }.start();
            }
        });
        actors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActorsBSFragment.show(getSupportFragmentManager(), mActorsBSFragment.getTag());
                mTxtCurrentTool.setVisibility(View.GONE);
                actors.setVisibility(View.GONE);
                Loading_text.setVisibility(View.VISIBLE);
                actors_load.setVisibility(View.VISIBLE);


                new CountDownTimer(5*1000, 1000){
                    @Override
                    public void onTick(long l) {
                        time_count.setText(String.valueOf(l/1000+"s"));
                    }

                    @Override
                    public void onFinish() {

                        mTxtCurrentTool.setVisibility(View.VISIBLE);
                        Loading_text.setVisibility(View.GONE);
                        actors.setVisibility(View.VISIBLE);
                        actors_load.setVisibility(View.GONE);
                    }
                }.start();
            }
        });

        hair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHairBSFragment.show(getSupportFragmentManager(), mHairBSFragment.getTag());
                mTxtCurrentTool.setVisibility(View.GONE);
                hair.setVisibility(View.GONE);
                Loading_text.setVisibility(View.VISIBLE);
                hair_load.setVisibility(View.VISIBLE);


                new CountDownTimer(5*1000, 1000){
                    @Override
                    public void onTick(long l) {
                        time_count.setText(String.valueOf(l/1000+"s"));
                    }

                    @Override
                    public void onFinish() {

                        mTxtCurrentTool.setVisibility(View.VISIBLE);
                        Loading_text.setVisibility(View.GONE);
                        hair.setVisibility(View.VISIBLE);
                        hair_load.setVisibility(View.GONE);
                    }
                }.start();
            }
        });


        maks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaksBSFragment.show(getSupportFragmentManager(), mMaksBSFragment.getTag());
                mTxtCurrentTool.setVisibility(View.GONE);
                maks.setVisibility(View.GONE);
                Loading_text.setVisibility(View.VISIBLE);
                maks_load.setVisibility(View.VISIBLE);


                new CountDownTimer(5*1000, 1000){
                    @Override
                    public void onTick(long l) {
                        time_count.setText(String.valueOf(l/1000+"s"));
                    }

                    @Override
                    public void onFinish() {

                        mTxtCurrentTool.setVisibility(View.VISIBLE);
                        Loading_text.setVisibility(View.GONE);
                        maks.setVisibility(View.VISIBLE);
                        maks_load.setVisibility(View.GONE);
                    }
                }.start();
            }
        });

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mLogoBSFragment.show(getSupportFragmentManager(), mLogoBSFragment.getTag());
                mTxtCurrentTool.setVisibility(View.GONE);
                logo.setVisibility(View.GONE);
                Loading_text.setVisibility(View.VISIBLE);
                logo_load.setVisibility(View.VISIBLE);


                new CountDownTimer(5*1000, 1000){
                    @Override
                    public void onTick(long l) {
                        time_count.setText(String.valueOf(l/1000+"s"));
                    }

                    @Override
                    public void onFinish() {

                        mTxtCurrentTool.setVisibility(View.VISIBLE);
                        Loading_text.setVisibility(View.GONE);
                        logo.setVisibility(View.VISIBLE);
                        logo_load.setVisibility(View.GONE);
                    }
                }.start();


            }
        });
        emoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mEmojiBSFragment.show(getSupportFragmentManager(), mEmojiBSFragment.getTag());
                mTxtCurrentTool.setVisibility(View.GONE);
                Loading_text.setVisibility(View.VISIBLE);
                emoji.setVisibility(View.GONE);
                emoji_load.setVisibility(View.VISIBLE);



                new CountDownTimer(5*1000, 1000){
                    @Override
                    public void onTick(long l) {
                        time_count.setText(String.valueOf(l/1000+"s"));
                    }

                    @Override
                    public void onFinish() {

                        mTxtCurrentTool.setVisibility(View.VISIBLE);
                        Loading_text.setVisibility(View.GONE);
                        emoji.setVisibility(View.VISIBLE);
                        emoji_load.setVisibility(View.GONE);
                    }
                }.start();



            }
        });
        brush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhotoEditor.setBrushDrawingMode(true);
                mTxtCurrentTool.setText(R.string.label_brush);
                mPropertiesBSFragment.show(getSupportFragmentManager(), mPropertiesBSFragment.getTag());
            }
        });
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextEditorDialogFragment textEditorDialogFragment = TextEditorDialogFragment.show(MainActivity.this);
                textEditorDialogFragment.setOnTextEditorListener(new TextEditorDialogFragment.TextEditor() {
                    @Override
                    public void onDone(String inputText, int colorCode) {
                        mPhotoEditor.addText(inputText, colorCode);
                        mTxtCurrentTool.setText(R.string.label_text);
                    }
                });
            }
        });
        erser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhotoEditor.brushEraser();
                mTxtCurrentTool.setText(R.string.label_eraser);
            }
        });
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTxtCurrentTool.setText(R.string.label_filter);
                showFilter(true);
            }
        });
        flare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mStickerBSFragment.show(getSupportFragmentManager(), mStickerBSFragment.getTag());
                mTxtCurrentTool.setVisibility(View.GONE);
                flare.setVisibility(View.GONE);
                Loading_text.setVisibility(View.VISIBLE);
                flare_load.setVisibility(View.VISIBLE);


                new CountDownTimer(5*1000, 1000){
                    @Override
                    public void onTick(long l) {
                        time_count.setText(String.valueOf(l/1000+"s"));
                    }

                    @Override
                    public void onFinish() {

                        mTxtCurrentTool.setVisibility(View.VISIBLE);
                        Loading_text.setVisibility(View.GONE);
                        flare.setVisibility(View.VISIBLE);
                        flare_load.setVisibility(View.GONE);
                    }
                }.start();
            }
        });
        frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFrameBSFragment.show(getSupportFragmentManager(), mFrameBSFragment.getTag());
                mTxtCurrentTool.setVisibility(View.GONE);
                frame.setVisibility(View.GONE);
                Loading_text.setVisibility(View.VISIBLE);
                frame_load.setVisibility(View.VISIBLE);


                new CountDownTimer(5*1000, 1000){
                    @Override
                    public void onTick(long l) {
                        time_count.setText(String.valueOf(l/1000+"s"));
                    }

                    @Override
                    public void onFinish() {

                        mTxtCurrentTool.setVisibility(View.VISIBLE);
                        Loading_text.setVisibility(View.GONE);
                        frame.setVisibility(View.VISIBLE);
                        frame_load.setVisibility(View.GONE);
                    }
                }.start();
            }
        });



        final View adContiner = findViewById(R.id.banner);



                //Admob Banner
                AdView mAdView = new AdView(MainActivity.this);
                mAdView.setAdSize(AdSize.SMART_BANNER);
                mAdView.setAdUnitId("ca-app-pub-8269466310713489/8102801208");
                ((RelativeLayout)adContiner).addView(mAdView);
                AdRequest adRequest = new AdRequest.Builder().build();
                mAdView.loadAd(adRequest);







    }

    private void initViews() {
        ImageView imgUndo;
        ImageView imgRedo;
        ImageView imgCamera;
        ImageView imgGallery;
        ImageView imgSave;
        ImageView imgClose;

        mPhotoEditorView = findViewById(R.id.photoEditorView);
        mRvTools = findViewById(R.id.re_hide);
        mTxtCurrentTool = findViewById(R.id.txtCurrentTool_show);
        mRvFilters = findViewById(R.id.rvFilterView);
        mRootView = findViewById(R.id.rootView);

        imgUndo = findViewById(R.id.imgUndo);
        imgUndo.setOnClickListener(this);

        imgRedo = findViewById(R.id.imgRedo);
        imgRedo.setOnClickListener(this);

        imgCamera = findViewById(R.id.imgCamera);
        imgCamera.setOnClickListener(this);

        imgGallery = findViewById(R.id.imgGallery);
        imgGallery.setOnClickListener(this);

        imgSave = findViewById(R.id.imgSave);
        imgSave.setOnClickListener(this);

        imgClose = findViewById(R.id.imgClose);
        imgClose.setOnClickListener(this);






    }



    @Override
    public void onEditTextChangeListener(final View rootView, String text, int colorCode) {
        TextEditorDialogFragment textEditorDialogFragment =
                TextEditorDialogFragment.show(this, text, colorCode);
        textEditorDialogFragment.setOnTextEditorListener(new TextEditorDialogFragment.TextEditor() {
            @Override
            public void onDone(String inputText, int colorCode) {
                mPhotoEditor.editText(rootView, inputText, colorCode);
                mTxtCurrentTool.setText(R.string.label_text);
            }
        });
    }

    @Override
    public void onAddViewListener(ViewType viewType, int numberOfAddedViews) {
        Log.d(TAG, "onAddViewListener() called with: viewType = [" + viewType + "], numberOfAddedViews = [" + numberOfAddedViews + "]");
    }

    @Override
    public void onRemoveViewListener(int numberOfAddedViews) {
        Log.d(TAG, "onRemoveViewListener() called with: numberOfAddedViews = [" + numberOfAddedViews + "]");
    }

    @Override
    public void onRemoveViewListener(ViewType viewType, int numberOfAddedViews) {
        Log.d(TAG, "onRemoveViewListener() called with: viewType = [" + viewType + "], numberOfAddedViews = [" + numberOfAddedViews + "]");
    }

    @Override
    public void onStartViewChangeListener(ViewType viewType) {
        Log.d(TAG, "onStartViewChangeListener() called with: viewType = [" + viewType + "]");
    }

    @Override
    public void onStopViewChangeListener(ViewType viewType) {
        Log.d(TAG, "onStopViewChangeListener() called with: viewType = [" + viewType + "]");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.imgUndo:
                mPhotoEditor.undo();
                break;

            case R.id.imgRedo:
                mPhotoEditor.redo();
                break;

            case R.id.imgSave:
                saveImage();
                break;

            case R.id.imgClose:
                onBackPressed();
                break;

            case R.id.imgCamera:
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                break;

            case R.id.imgGallery:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_REQUEST);
                break;
        }
    }

    @SuppressLint("MissingPermission")
    private void saveImage() {
        if (requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            showLoading("Saving...");
            File file = new File(Environment.getExternalStorageDirectory()
                    + File.separator + ""
                    + System.currentTimeMillis() + ".png");
            try {
                file.createNewFile();

                SaveSettings saveSettings = new SaveSettings.Builder()
                        .setClearViewsEnabled(true)
                        .setTransparencyEnabled(true)
                        .build();

                mPhotoEditor.saveAsFile(file.getAbsolutePath(), saveSettings, new PhotoEditor.OnSaveListener() {
                    @Override
                    public void onSuccess(@NonNull String imagePath) {
                        hideLoading();
                        showSnackbar("Image Saved Successfully");
                        mPhotoEditorView.getSource().setImageURI(Uri.fromFile(new File(imagePath)));
                    }

                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        hideLoading();
                        showSnackbar("Failed to save Image");
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                hideLoading();
                showSnackbar(e.getMessage());
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CAMERA_REQUEST:
                    mPhotoEditor.clearAllViews();
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    mPhotoEditorView.getSource().setImageBitmap(photo);
                    break;
                case PICK_REQUEST:
                    try {
                        mPhotoEditor.clearAllViews();
                        Uri uri = data.getData();
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        mPhotoEditorView.getSource().setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    @Override
    public void onColorChanged(int colorCode) {
        mPhotoEditor.setBrushColor(colorCode);
        mTxtCurrentTool.setText(R.string.label_brush);
    }

    @Override
    public void onOpacityChanged(int opacity) {
        mPhotoEditor.setOpacity(opacity);
        mTxtCurrentTool.setText(R.string.label_brush);
    }

    @Override
    public void onBrushSizeChanged(int brushSize) {
        mPhotoEditor.setBrushSize(brushSize);
        mTxtCurrentTool.setText(R.string.label_brush);
    }

    @Override
    public void onEmojiClick(String emojiUnicode) {
        mPhotoEditor.addEmoji(emojiUnicode);
        mTxtCurrentTool.setText(R.string.label_emoji);

    }

    @Override
    public void onStickerClick(Bitmap bitmap) {
        mPhotoEditor.addImage(bitmap);
        mTxtCurrentTool.setText(R.string.label_sticker);
    }

    @Override
    public void isPermissionGranted(boolean isGranted, String permission) {
        if (isGranted) {
            saveImage();
        }
    }

    private void showSaveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you want to exit without saving image ?");
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveImage();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNeutralButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.create().show();

    }

    @Override
    public void onFilterSelected(PhotoFilter photoFilter) {
        mPhotoEditor.setFilterEffect(photoFilter);
    }



    @Override
    public void onToolSelected(ToolType toolType) {
        switch (toolType) {
            case BRUSH:
                mPhotoEditor.setBrushDrawingMode(true);
                mTxtCurrentTool.setText(R.string.label_brush);
                mPropertiesBSFragment.show(getSupportFragmentManager(), mPropertiesBSFragment.getTag());
                break;
            case TEXT:
                TextEditorDialogFragment textEditorDialogFragment = TextEditorDialogFragment.show(this);
                textEditorDialogFragment.setOnTextEditorListener(new TextEditorDialogFragment.TextEditor() {
                    @Override
                    public void onDone(String inputText, int colorCode) {
                        mPhotoEditor.addText(inputText, colorCode);
                        mTxtCurrentTool.setText(R.string.label_text);
                    }
                });
                break;
            case ERASER:
                mPhotoEditor.brushEraser();
                mTxtCurrentTool.setText(R.string.label_eraser);
                break;
            case FILTER:
                mTxtCurrentTool.setText(R.string.label_filter);
                showFilter(true);
                break;
            case EMOJI:
                mEmojiBSFragment.show(getSupportFragmentManager(), mEmojiBSFragment.getTag());
                break;
            case STICKER:
                mStickerBSFragment.show(getSupportFragmentManager(), mStickerBSFragment.getTag());
                break;
        }
    }


    void showFilter(boolean isVisible) {
        mIsFilterVisible = isVisible;
        mConstraintSet.clone(mRootView);

        if (isVisible) {
            mConstraintSet.clear(mRvFilters.getId(), ConstraintSet.START);
            mConstraintSet.connect(mRvFilters.getId(), ConstraintSet.START,
                    ConstraintSet.PARENT_ID, ConstraintSet.START);
            mConstraintSet.connect(mRvFilters.getId(), ConstraintSet.END,
                    ConstraintSet.PARENT_ID, ConstraintSet.END);
        } else {
            mConstraintSet.connect(mRvFilters.getId(), ConstraintSet.START,
                    ConstraintSet.PARENT_ID, ConstraintSet.END);
            mConstraintSet.clear(mRvFilters.getId(), ConstraintSet.END);
        }

        ChangeBounds changeBounds = new ChangeBounds();
        changeBounds.setDuration(350);
        changeBounds.setInterpolator(new AnticipateOvershootInterpolator(1.0f));
        TransitionManager.beginDelayedTransition(mRootView, changeBounds);

        mConstraintSet.applyTo(mRootView);
    }

    @Override
    public void onBackPressed() {
        if (mIsFilterVisible) {
            showFilter(false);
            mTxtCurrentTool.setText(R.string.app_name);
        } else if (!mPhotoEditor.isCacheEmpty()) {
            showSaveDialog();
        } else {
            Intent intent = new Intent(MainActivity.this, FinishActivity.class);
            finish();
            startActivity(intent);
        }
    }
    public void displayInterstitial() {
        // If Interstitial Ads are loaded then show else show nothing.
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }
}
