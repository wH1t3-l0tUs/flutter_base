package io.driverdoc.testapp.ui.detectdocument;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.jaeger.library.StatusBarUtil;
import com.pixelnetica.imagesdk.MetaImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Observable;

import io.driverdoc.testapp.R;
import io.driverdoc.testapp.common.MVVMApplication;
import io.driverdoc.testapp.ui.customview.TextViewLatoRegularBoldNormal;
import io.driverdoc.testapp.ui.detectdocument.camera.CameraActivity;
import io.driverdoc.testapp.ui.detectdocument.util.Action;
import io.driverdoc.testapp.ui.detectdocument.util.RuntimePermissions;
import io.driverdoc.testapp.ui.utils.LoadDataBinding;
import io.driverdoc.testapp.ui.utils.permision.SharedPfPermissionUtils;

public class DocumentActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PICK_IMAGE = 100;
    private static final int TAKE_PHOTO = 101;
    private static final int SHOW_SETTINGS = 102;
    private Context context;
    // Main Identity
    MainIdentity mIdentity;

    // Display and size
    CropImageView mImageView;
    ProgressBar mProgressWait;
    private int count = 0;
    private boolean isClickDone = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_detect_document);
        StatusBarUtil.setTransparent(this);

        try {
            final Observer<Integer> favsObserver = new Observer<Integer>() {
                @Override
                public void onChanged(Integer integer) {
                    if (integer == 9) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                MVVMApplication.Companion.getLiveData().postValue(-1);
                                onBackPressed();
                            }
                        }, 0);
                    }
                }
            };
            MVVMApplication.Companion.getLiveData().observe(this, favsObserver);
        } catch (Exception e) {

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        // Create a new Identity
        mIdentity = ViewModelProviders.of(this).get(MainIdentity.class);
        mIdentity.executeList.observe(this, actions -> {
            if (actions != null) {
                for (Action<DocumentActivity> action : actions) {
                    action.run(this);
                }
                actions.clear();
            }
        });


        // Setup display
        mImageView = findViewById(R.id.image_holder);
        mImageView.setSdkFactory(mIdentity.SdkFactory);
        mProgressWait = findViewById(R.id.progress_wait);
        mIdentity.setSaveFormat(SaveImageTask.SAVE_JPEG);
        mIdentity.setProcessingProfile(ProcessImageTask.NoBinarization);
        onTakePhoto();
        initView();

        MVVMApplication.Companion.isDoneDetext().observe(this, isDone -> {
            if (isDone && isClickDone) {
                if (MVVMApplication.Companion.getListTextCheck().size() == MVVMApplication.Companion.getListText().size()) {
                    isClickDone = false;
                    mProgressWait.setVisibility(View.INVISIBLE);
                    MVVMApplication.Companion.isDoneDetext().postValue(false);
                    if (SharedPfPermissionUtils.getIncomplete(this)) {
                        UpdateDocument.Companion.upDocument(SharedPfPermissionUtils.getNameDocument(this), this, String.valueOf(SharedPfPermissionUtils.getIDTrip(this)), UpdateDocument.Companion.getListUrl(this), SharedPfPermissionUtils.getToken(this));
                    } else {
                        MVVMApplication.Companion.isDone().postValue(true);
                        onBackPressed();
                    }
                }
            }
        });
    }

    private void initView() {
        findViewById(R.id.btn_sum).setOnClickListener(this);
        findViewById(R.id.btn_done).setOnClickListener(this);
        findViewById(R.id.btn_close).setOnClickListener(this);
        findViewById(R.id.btn_retake).setOnClickListener(this);
        ((AppCompatImageButton) findViewById(R.id.btn_close)).setColorFilter(getResources().getColor(R.color.black));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sum:
                //true
                MVVMApplication.Companion.getListTextCheck().add(true);
                onTakePhoto();
                break;
            case R.id.btn_close:
                // remove all
                MVVMApplication.Companion.getListTextCheck().clear();
                onBackPressed();
                break;
            case R.id.btn_retake:
                // false
                MVVMApplication.Companion.getListTextCheck().add(false);
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                ) {
                    File file = new File(getApplicationContext().getExternalCacheDir(), String.valueOf(SharedPfPermissionUtils.getIDTrip(getApplicationContext())) + "/" + SharedPfPermissionUtils.getRecentFileName(getApplicationContext()) + ".jpg");
                    if (file.isFile()) {
                        LoadDataBinding.INSTANCE.deteteItem(file);
                        onTakePhoto();
                    }
                }
                break;
            case R.id.btn_done:
                //true
                MVVMApplication.Companion.getListTextCheck().add(true);
                isClickDone = true;
                if (MVVMApplication.Companion.getListTextCheck().size() == MVVMApplication.Companion.getListText().size()) {
                    isClickDone = false;
                    mProgressWait.setVisibility(View.INVISIBLE);
                    MVVMApplication.Companion.isDoneDetext().postValue(false);
                    if (SharedPfPermissionUtils.getIncomplete(this)) {
                        UpdateDocument.Companion.upDocument(SharedPfPermissionUtils.getNameDocument(this), this, String.valueOf(SharedPfPermissionUtils.getIDTrip(this)), UpdateDocument.Companion.getListUrl(this), SharedPfPermissionUtils.getToken(this));
                    } else {
                        MVVMApplication.Companion.isDone().postValue(true);
                        onBackPressed();
                    }
                }else {
                    mProgressWait.setVisibility(View.VISIBLE);
                }
                break;


        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        RuntimePermissions.instance().handleRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    void onTakePhoto() {
        final File fileSink = //getExternalCacheDir();
                new File(Environment.getExternalStorageDirectory(), "DI-SDK");
        // Query permissions and create directories
        RuntimePermissions.instance().runWithPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                R.string.permission_query_write_storage,
                new RuntimePermissions.Callback() {
                    @Override
                    public void permissionRun(String permission, boolean granted) {
                        if (granted && (fileSink.exists() || fileSink.mkdirs())) {
                            // Common routine to start camera
                            Intent intent = CameraActivity.newIntent(
                                    DocumentActivity.this,
                                    mIdentity.SdkFactory,
                                    fileSink.getAbsolutePath(),
                                    "camera-prefs",
                                    true);
                            startActivityForResult(intent, TAKE_PHOTO);
                        }
                    }
                });
    }

    void onSaveImage() {
        mIdentity.setSaveFormat(SaveImageTask.SAVE_JPEG);
        RuntimePermissions.instance().runWithPermission(DocumentActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                R.string.permission_query_write_storage, new RuntimePermissions.Callback() {
                    @Override
                    public void permissionRun(String permission, boolean granted) {
                        if (granted) {
                            // NOTE: all dialog params is gone through mIdentity
                            File file = new File(context.getExternalCacheDir(), String.valueOf(SharedPfPermissionUtils.getIDTrip(context)));
                            if (!file.isDirectory()) {
                                file.mkdirs();
                            }
                            mIdentity.saveImage(getApplicationContext(), file);
                        }
                    }
                });

    }

    void onSaveComplete(@NonNull List<SaveImageTask.ImageFile> files) {
        File dir = new File(getExternalCacheDir(), String.valueOf(SharedPfPermissionUtils.getIDTrip(this)));
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
//                //extract text from image using Firebase ML kit on-device or cloud api
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Bitmap bitmap = ImageTextReader.getUprightImage(files.get(0).filePath);
                    ImageTextReader.readTextFromImage(bitmap, getApplicationContext());
                }
            }).start();
            ((TextViewLatoRegularBoldNormal) findViewById(R.id.tv1)).setText("Page " + children.length);
            return;
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        OutputStream fos = null;
        Uri uri;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentResolver resolver = getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, new Date().toString() + ".jpg");
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
            Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            uri = imageUri;
            try {
                fos = resolver.openOutputStream(Objects.requireNonNull(imageUri));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            inImage.compress(Bitmap.CompressFormat.JPEG, 60, fos);

            try {
                Objects.requireNonNull(fos).close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            inImage.compress(Bitmap.CompressFormat.JPEG, 60, bytes);
            String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
            uri = Uri.parse(path);
        }
        return uri;
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PICK_IMAGE:
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    ((ImageButton) findViewById(R.id.btn_done)).setClickable(false);
                    ((ImageButton) findViewById(R.id.btn_sum)).setClickable(false);
                    ((ImageButton) findViewById(R.id.btn_retake)).setClickable(false);
                    mProgressWait.setVisibility(View.VISIBLE);
                    count = 0;
                    context = this;
                    Uri selectedImage = data.getData();
                    mIdentity.reset();
                    new AsyncTask<Bitmap, Void, Bitmap>() {
                        @Override
                        protected Bitmap doInBackground(Bitmap... bitmaps) {
                            try {
                                return MediaStore.Images.Media.getBitmap(context.getContentResolver(), selectedImage);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Bitmap bitmap) {
                            try {
                                mIdentity.openImage(getImageUri(context, reCreateBitmap(bitmap)));
                            } catch (Exception e) {

                            }
                            super.onPostExecute(bitmap);
                        }
                    }.execute();
                }
                break;
        }
    }

    void updateUI() {
        updateWaitState();
        setDisplayImage();
    }

    private Bitmap reCreateBitmap(Bitmap img) {
        Matrix matrix = new Matrix();
        if (img.getWidth() > img.getHeight()) {
            matrix.postRotate(90);
        }
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        return rotatedImg;
    }

    public Bitmap resizeImage(Bitmap bitmap, Context ctx) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) ctx).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels * 2;
        int height = displayMetrics.widthPixels * 2;

        return Bitmap.createScaledBitmap(reCreateBitmap(bitmap), width, height, true);
    }

    public void setDisplayImage() {
        if (mIdentity.hasDisplayImage()) {
            mImageView.setCropImage(mIdentity.getDisplayBitmap(), mIdentity.getImageMatrix(), mIdentity.getCropData());

        } else {
            mImageView.setCropImage(null, null, null);
        }
        mIdentity.recycleImage();
    }

    public void updateWaitState() {
        if (mIdentity.isWaitState()) {
            // TODO: setup a better color filter
            mImageView.setEnabled(false);
            mImageView.setColorFilter(Color.rgb(128, 128, 128), PorterDuff.Mode.LIGHTEN);
            ((ImageButton) findViewById(R.id.btn_done)).setClickable(false);
            ((ImageButton) findViewById(R.id.btn_sum)).setClickable(false);
            ((ImageButton) findViewById(R.id.btn_retake)).setClickable(false);


        } else {
            mImageView.setEnabled(true);
            mImageView.setColorFilter(0, PorterDuff.Mode.DST);
            mProgressWait.setVisibility(View.INVISIBLE);
            ((ImageButton) findViewById(R.id.btn_done)).setClickable(true);
            ((ImageButton) findViewById(R.id.btn_sum)).setClickable(true);
            ((ImageButton) findViewById(R.id.btn_retake)).setClickable(true);
            count++;
            if (count == 2) {
                onSaveImage();
                count = 0;
            }
        }
    }

    private static String displayUriPath(ContentResolver cr, Uri uri) {
        if (cr == null || uri == null) {
            return "";
        }
        String path = MetaImage.getRealPathFromURI(cr, uri);
        if (path == null) {
            path = uri.toString();
        }
        return path;
    }

    public void showProcessingError(@TaskResult.TaskError int error, Uri uri) {
        updateUI();
        // Show error toast
        switch (error) {
            case TaskResult.NOERROR:
                Toast.makeText(getApplicationContext(), R.string.msg_processing_complete, Toast.LENGTH_SHORT).show();
                break;
            case TaskResult.PROCESSING:
                Toast.makeText(getApplicationContext(), R.string.msg_processing_error, Toast.LENGTH_LONG).show();
                break;
            case TaskResult.OUTOFMEMORY:
                Toast.makeText(getApplicationContext(), R.string.msg_out_of_memory, Toast.LENGTH_LONG).show();
                break;
            case TaskResult.NODOCCORNERS:
                Toast.makeText(getApplicationContext(), R.string.msg_no_doc_corners, Toast.LENGTH_LONG).show();
                break;
            case TaskResult.INVALIDCORNERS:
                Toast.makeText(getApplicationContext(), R.string.msg_invalid_corners, Toast.LENGTH_LONG).show();
                break;
            case TaskResult.INVALIDFILE:
                Toast.makeText(this, String.format(getString(R.string.msg_cannot_open_file),
                        displayUriPath(getContentResolver(), uri)), Toast.LENGTH_LONG).show();
                break;
            case TaskResult.CANTSAVEFILE:
                Toast.makeText(this, R.string.msg_cannot_write_image_file, Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(this,
                        String.format(getString(R.string.msg_unknown_error), error), Toast.LENGTH_LONG).show();
                break;
        }
    }


}
