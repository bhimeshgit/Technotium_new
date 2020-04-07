package com.technotium.technotiumapp.expenses.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.technotium.technotiumapp.BuildConfig;
import com.technotium.technotiumapp.R;
import com.technotium.technotiumapp.config.ImageProcessing;
import com.technotium.technotiumapp.config.JsonParserVolley;
import com.technotium.technotiumapp.config.SessionManager;
import com.technotium.technotiumapp.config.WebUrl;
import com.technotium.technotiumapp.payment.activity.AddPaymentActivity;
import com.technotium.technotiumapp.payment.activity.PaymentHistoryActivity;
import com.technotium.technotiumapp.workorder.model.WorkOrderPojo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddExpense extends AppCompatActivity {

    Button btnBrowse_AttachDocument,btnCapture_AttachDocument;
    ImageView imgPreview_AttachDocument ;
    Button btnRotate_AttachDocument ,btnRotate1_AttachDocument,btnCrop_AttachDocument,btnSave_AttachDocument ;
   AddExpense currentActivity;
    public static final int BROWSE_IMAGE_REQUEST_CODE=101,CAMERA_CAPTURE_IMAGE_REQUEST_CODE=102,MEDIA_TYPE_IMAGE = 1,CROP_IMAGE_REQUEST_CODE = 4;;
    private static Uri fileUri;
    static String filePath = "",filename = "",IMAGE_DIRECTORY_NAME = "Technotium",encodedPhotoString="";
    Bitmap bitmap;
    WorkOrderPojo workOrderPojo;
    EditText txtAmout,txtComment;
    ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        if(getIntent().getSerializableExtra("orderData") != null) {
            workOrderPojo =(WorkOrderPojo) getIntent().getSerializableExtra("orderData");
            init();
        }
    }
    public void init(){
        currentActivity=AddExpense.this;
        btnBrowse_AttachDocument=findViewById(R.id.btnBrowse_AttachDocument);
        btnCapture_AttachDocument=findViewById(R.id.btnCapture_AttachDocument);
        imgPreview_AttachDocument=findViewById(R.id.imgPreview_AttachDocument);
        btnRotate_AttachDocument=findViewById(R.id.btnRotate_AttachDocument) ;
        btnRotate1_AttachDocument=findViewById(R.id.btnRotate1_AttachDocument)  ;
        btnCrop_AttachDocument=findViewById(R.id.btnCrop_AttachDocument) ;
        btnSave_AttachDocument=findViewById(R.id.btnSave_AttachDocument);
        txtAmout=findViewById(R.id.txtAmout);
        txtComment=findViewById(R.id.txtComment);
        btnBrowse_AttachDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                browseImage(currentActivity);
            }
        });
        btnCapture_AttachDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage(currentActivity);
            }
        });
        btnSave_AttachDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDetail();
            }
        });

        pDialog = new ProgressDialog(currentActivity);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(true);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        //***************** DIR For Edited Image *********************\
        File myDir = new File(Environment.getExternalStorageDirectory() + "/Loksuvidha/Images");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        String fname = "Image.jpg";
        final File file = new File(myDir, fname);
        if((requestCode == CROP_IMAGE_REQUEST_CODE) && resultCode != 0){
            if (file.exists())
                file.delete();
        }

        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE || requestCode == BROWSE_IMAGE_REQUEST_CODE) {
            btnRotate_AttachDocument.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    Bitmap rotatedbitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                            matrix, true);
                    imgPreview_AttachDocument.setImageBitmap(rotatedbitmap);
                    bitmap = rotatedbitmap;
                    try {
                        FileOutputStream out = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                        out.flush();
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    compressImage();
                }
            });

            btnRotate1_AttachDocument.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Matrix matrix = new Matrix();
                    matrix.postRotate(-90);
                    Bitmap rotatedbitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                            matrix, true);
                    imgPreview_AttachDocument.setImageBitmap(rotatedbitmap);
                    bitmap = rotatedbitmap;
                    try {
                        FileOutputStream out = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                        out.flush();
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    compressImage();
                }
            });

            btnCrop_AttachDocument.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    try{
//						cropCapturedImage(Uri.fromFile(file));

                        cropCapturedImage(FileProvider.getUriForFile(currentActivity, BuildConfig.APPLICATION_ID + ".provider",file));

                    }
                    catch(ActivityNotFoundException aNFE){
                        //display an error message if user device doesn't support
                        String errorMessage = "Sorry - your device doesn't support the crop action!";
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }

        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Log.d("stest","camera");
                filePath = fileUri.getPath();
                if (filePath != null) {
                    //BitmapFactory.Options options = new BitmapFactory.Options();
                    //options.inSampleSize = 8;
                    //bitmap = BitmapFactory.decodeFile(filePath, options);
                    bitmap = ImageProcessing.decodeSampledBitmapFromFile(filePath, 1024,768);
                    filename = "0" + "_" + SessionManager.getMyInstance(currentActivity).getEmpid()+ "_" +"1" + ".jpg";
                    Matrix matrix = new Matrix();
                    Bitmap newbitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    bitmap = newbitmap;
                    imgPreview_AttachDocument.setImageBitmap(bitmap);
                    try {
                        FileOutputStream out = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                        out.flush();
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        filePath = file.getCanonicalPath();
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"Sorry, file path is missing!", Toast.LENGTH_LONG).show();

                }
            }
            else if (resultCode == Activity.RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),"User cancelled image capture", Toast.LENGTH_SHORT).show();

            }
            else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),"Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();

            }
        }


        if (requestCode == BROWSE_IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                Uri uri = data.getData();
                filePath = getRealPathFromURI(getApplicationContext(), uri);

                if (filePath != null) {
                    //BitmapFactory.Options options = new BitmapFactory.Options();
                    //options.inSampleSize = 8;
                    //bitmap = BitmapFactory.decodeFile(filePath, options);
                    bitmap = ImageProcessing.decodeSampledBitmapFromFile(filePath, 1024,768);
                    filename = "0" + "_" + SessionManager.getMyInstance(currentActivity).getEmpid()+ "_" +"1" + ".jpg";

                    Matrix matrix = new Matrix();
                    Bitmap newbitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    bitmap = newbitmap;

                    imgPreview_AttachDocument.setImageBitmap(bitmap);

                    try {
                        FileOutputStream out = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                        out.flush();
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        filePath = file.getCanonicalPath();
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"Sorry, file path is missing!", Toast.LENGTH_LONG).show();
                }

            }
            else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(),"User cancelled image browsing", Toast.LENGTH_SHORT).show();
            }
            else {
                // failed to record video
                Toast.makeText(getApplicationContext(),"Sorry! Failed to browse image", Toast.LENGTH_SHORT).show();
            }
        }

        if(requestCode == CROP_IMAGE_REQUEST_CODE & resultCode == -1){

            Bitmap croppedbitmap = null;
            filePath = fileUri.getPath();
            if (filePath != null) {
                croppedbitmap = BitmapFactory.decodeFile(filePath);
                imgPreview_AttachDocument.setImageBitmap(croppedbitmap);
            }
            bitmap = croppedbitmap;

            try {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(requestCode == CROP_IMAGE_REQUEST_CODE & resultCode == 0){
            imgPreview_AttachDocument.setImageBitmap(bitmap);
            //Toast.makeText(getApplicationContext(), String.valueOf(requestCode)+"  -  "+String.valueOf(resultCode), Toast.LENGTH_SHORT).show();
        }
        compressImage();
    }
    public void compressImage(){
        if (bitmap != null) {
            int maxHeight=700;
            int maxWidth=450;
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();

            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > ratioBitmap) {
                finalWidth = (int) ((float)maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float)maxWidth / ratioBitmap);
            }


            float scaleWidth = ((float)finalWidth ) / width;
            float scaleHeight = ((float) finalHeight) / height;
            // CREATE A MATRIX FOR THE MANIPULATION
            Matrix matrix = new Matrix();
            // RESIZE THE BIT MAP
            matrix.postScale(scaleWidth, scaleHeight);

            // "RECREATE" THE NEW BITMAP
            Bitmap resizedBitmap = Bitmap.createBitmap(
                    bitmap, 0, 0, width, height, matrix, false);


            resizedBitmap.compress(Bitmap.CompressFormat.PNG, 40, byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();
            encodedPhotoString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        }
    }

    public static void captureImage(Activity activity) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        activity.startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    public static void browseImage(Activity activity) {
        // TODO Auto-generated method stub
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(galleryIntent, BROWSE_IMAGE_REQUEST_CODE);
    }
    public void cropCapturedImage(Uri picUri){
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        cropIntent.setDataAndType(picUri, "image/*");
        cropIntent.putExtra("crop", "true");

        //indicate aspect of desired crop
        cropIntent.putExtra("aspectX", 0);
        cropIntent.putExtra("aspectY", 0);
        cropIntent.putExtra("scale", true);
        cropIntent.putExtra("return-data", true);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(cropIntent, CROP_IMAGE_REQUEST_CODE);
    }
    public static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }
    // returning image
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("TAG", "Oops! Failed create "+ IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator+ "IMG_" + timeStamp + ".jpg");
        }
        else {
            return null;
        }

        return mediaFile;
    }
    private String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);

            return path;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void saveDetail(){
        if(txtAmout.getText().toString().equalsIgnoreCase("")){
            Toast.makeText(currentActivity,"please enter the amount",Toast.LENGTH_SHORT).show();
            return;
        }
        final JsonParserVolley jsonParserVolley = new JsonParserVolley(currentActivity);
        pDialog.show();
        jsonParserVolley.addParameter("order_id",workOrderPojo.getPkid());
        jsonParserVolley.addParameter("comment",txtComment.getText().toString());
        jsonParserVolley.addParameter("amount",txtAmout.getText().toString());
        jsonParserVolley.addParameter("image", encodedPhotoString);
        jsonParserVolley.addParameter("userid",SessionManager.getMyInstance(currentActivity).getEmpid());

        jsonParserVolley.executeRequest(Request.Method.POST, WebUrl.ADD_EXPENSES ,new JsonParserVolley.VolleyCallback() {
                    @Override
                    public void getResponse(String response) {
                        pDialog.dismiss();
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            int success=jsonObject.getInt("success");
                            if(success==1){
                                Toast.makeText(currentActivity,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(currentActivity, ViewAllExpenses.class);
                                intent.putExtra("orderData",workOrderPojo);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                Toast.makeText(currentActivity,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(currentActivity, ViewAllExpenses.class);
        intent.putExtra("orderData",workOrderPojo);
        startActivity(intent);
        finish();
    }
}
