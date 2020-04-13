package com.technotium.technotiumapp.after_sales.dialog;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import com.android.volley.Request;
import com.technotium.technotiumapp.BuildConfig;
import com.technotium.technotiumapp.R;
import com.technotium.technotiumapp.after_sales.adapter.MeterReadingAdapter;
import com.technotium.technotiumapp.after_sales.adapter.SerialNoAdapter;
import com.technotium.technotiumapp.after_sales.model.MeterReadingPojo;
import com.technotium.technotiumapp.after_sales.model.SerialNoPojo;
import com.technotium.technotiumapp.config.ImageProcessing;
import com.technotium.technotiumapp.config.JsonParserVolley;
import com.technotium.technotiumapp.config.SessionManager;
import com.technotium.technotiumapp.config.WebUrl;
import com.technotium.technotiumapp.payment.activity.AddPaymentActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddMeterReadingDlgFrag  extends DialogFragment {
    public static final String TAG = AddSerailNoDlgFrag.class.getSimpleName();
    private Context mContext;
    private Dialog mDialog;
    private EditText txtSerialNo,txtDate,txtPass;
    private TextView txtTitle;
    String OrderDate,type,order_id;
    String orderToset;
    private Button btnAdd;
    private ProgressDialog pDialog;
    private ArrayList<MeterReadingPojo> panelno_list;
    MeterReadingAdapter serialNoAdapter;
    private TextView txtviewSerial;

    private LinearLayout portal_lay,upload_nac_img,serial_no_lay;

    //upload Image
    Button btnBrowse_AttachDocument,btnCapture_AttachDocument;
    ImageView imgPreview_AttachDocument ;
    Button btnRotate_AttachDocument ,btnRotate1_AttachDocument,btnCrop_AttachDocument,btnSave_AttachDocument ;

    public static final int BROWSE_IMAGE_REQUEST_CODE=101,CAMERA_CAPTURE_IMAGE_REQUEST_CODE=102,MEDIA_TYPE_IMAGE = 1,CROP_IMAGE_REQUEST_CODE = 4;;
    private static Uri fileUri;
    static String filePath = "",filename = "",IMAGE_DIRECTORY_NAME = "Technotium",encodedPhotoString="";
    Bitmap bitmap;
    public AddMeterReadingDlgFrag(String type, String order_id, ArrayList<MeterReadingPojo> panelno_list, MeterReadingAdapter serialNoAdapter) {
        this.type=type;
        this.order_id=order_id;
        this.panelno_list=panelno_list;
        this.serialNoAdapter=serialNoAdapter;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mContext = getActivity();
        mDialog = new Dialog(mContext);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mDialog.setContentView(R.layout.dlg_add_serial_no);
        init();
        return mDialog;
    }

    private void init() {
        txtSerialNo=mDialog.findViewById(R.id.txtSerialNo);
        txtDate=mDialog.findViewById(R.id.txtDate);
        txtPass=mDialog.findViewById(R.id.txtPass);
        txtTitle=mDialog.findViewById(R.id.txtTitle);
        btnAdd=mDialog.findViewById(R.id.btnAdd);
        txtviewSerial=mDialog.findViewById(R.id.txtviewSerial);
        portal_lay=mDialog.findViewById(R.id.portal_lay);
        upload_nac_img=mDialog.findViewById(R.id.upload_nac_img);
        serial_no_lay=mDialog.findViewById(R.id.serial_no_lay);

        btnBrowse_AttachDocument=mDialog.findViewById(R.id.btnBrowse_AttachDocument);
        btnCapture_AttachDocument=mDialog.findViewById(R.id.btnCapture_AttachDocument);
        imgPreview_AttachDocument=mDialog.findViewById(R.id.imgPreview_AttachDocument);
        btnRotate_AttachDocument=mDialog.findViewById(R.id.btnRotate_AttachDocument) ;
        btnRotate1_AttachDocument=mDialog.findViewById(R.id.btnRotate1_AttachDocument)  ;
        btnCrop_AttachDocument=mDialog.findViewById(R.id.btnCrop_AttachDocument) ;
        btnSave_AttachDocument=mDialog.findViewById(R.id.btnSave_AttachDocument);

        if(type.equals("portal")){
            portal_lay.setVisibility(View.VISIBLE);
            txtTitle.setText("Portal Details");
        }
        else if(type.equals("gen_meter_reading") || type.equals("net_meter_reading")){
            serial_no_lay.setVisibility(View.GONE);
            upload_nac_img.setVisibility(View.VISIBLE);
            if(type.equals("gen_meter_reading")){ txtTitle.setText("Generation Meter Serial No.");}
            if(type.equals("net_meter_reading")){txtTitle.setText("Net Meter Serial No."); }
        }
        else if(type.equals("panel")){txtTitle.setText("Panel Details"); }
        else if(type.equals("inverter")){txtTitle.setText("Inverter Details"); }
        else if(type.equals("wifi_stick")){txtTitle.setText("Wifi Stick Details"); }
        else if(type.equals("get_meter_serial")){ txtTitle.setText("Generation Meter Serial No.");}
        else if(type.equals("net_meter_serial")){txtTitle.setText("Net Meter Serial No."); }
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMeterReading();
            }
        });
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal=Calendar.getInstance();
        Date dt=cal.getTime();
        orderToset=sdf.format(dt);
        txtDate.setText(new SimpleDateFormat("dd-MM-yyyy").format(dt));
        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateFunction();
            }
        });

        btnBrowse_AttachDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                browseImage(getActivity());
            }
        });
        btnCapture_AttachDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage(getActivity());
            }
        });
    }

    private void addMeterReading() {
        final JsonParserVolley jsonParserVolley = new JsonParserVolley(getActivity());
        pDialog.show();
        jsonParserVolley.addParameter("order_id",order_id);;
        jsonParserVolley.addParameter("userid", SessionManager.getMyInstance(getActivity()).getEmpid());
        jsonParserVolley.addParameter("image", encodedPhotoString);
        jsonParserVolley.addParameter("type", type);
        jsonParserVolley.addParameter("add_date", orderToset);
        jsonParserVolley.executeRequest(Request.Method.POST, WebUrl.ADD_METER_READING ,new JsonParserVolley.VolleyCallback() {
                    @Override
                    public void getResponse(String response) {
                        pDialog.dismiss();
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            int success=jsonObject.getInt("success");
                            if(success==1){
                                int pkid=jsonObject.getInt("pkid");
                                String imagepath=jsonObject.getString("imagepath");
                                Toast.makeText(getActivity(),jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                                MeterReadingPojo meterReadingPojo=new MeterReadingPojo();
                                meterReadingPojo.setInserttimestamp(orderToset);
                                meterReadingPojo.setPkid(pkid+"");
                                meterReadingPojo.setType(type);
                                meterReadingPojo.setActive(1);
                                meterReadingPojo.setReading_img(imagepath);
                                meterReadingPojo.setOrder_id(order_id);
                                meterReadingPojo.setAdded_by(SessionManager.getMyInstance(getActivity()).getEmpName());
                                panelno_list.add(meterReadingPojo);
                                serialNoAdapter.notifyDataSetChanged();
                                mDialog.dismiss();
                            }
                            else{
                                Toast.makeText(getActivity(),jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }

    public void dateFunction(){
        Calendar calendar= Calendar.getInstance();
        int year =calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int days=calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dg=new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                int monthofyear=month+1;
                String date=dayOfMonth+"-"+monthofyear+"-"+year;
                txtDate.setText(date);
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                Date dt = null;
                try {
                    dt = format.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat your_format = new SimpleDateFormat("yyyy-MM-dd");
                OrderDate = your_format.format(dt);
                orderToset=OrderDate;
            }
        },year,month,days);
        dg.getDatePicker().setMaxDate(new Date().getTime());
        dg.show();
    }

    public void showProgressDialog(){
        pDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        //***************** DIR For Edited Image *********************\
        File myDir = new File(Environment.getExternalStorageDirectory() + "/Technotium/Images");
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

                        cropCapturedImage(FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider",file));

                    }
                    catch(ActivityNotFoundException aNFE){
                        //display an error message if user device doesn't support
                        String errorMessage = "Sorry - your device doesn't support the crop action!";
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
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
                    filename = "0" + "_" + SessionManager.getMyInstance(getContext()).getEmpid()+ "_" +"1" + ".jpg";
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
                    Toast.makeText(getContext(),"Sorry, file path is missing!", Toast.LENGTH_LONG).show();

                }
            }
            else if (resultCode == Activity.RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getContext(),"User cancelled image capture", Toast.LENGTH_SHORT).show();

            }
            else {
                // failed to capture image
                Toast.makeText(getContext(),"Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();

            }
        }


        if (requestCode == BROWSE_IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                Uri uri = data.getData();
                filePath = getRealPathFromURI(getContext(), uri);

                if (filePath != null) {
                    //BitmapFactory.Options options = new BitmapFactory.Options();
                    //options.inSampleSize = 8;
                    //bitmap = BitmapFactory.decodeFile(filePath, options);
                    bitmap = ImageProcessing.decodeSampledBitmapFromFile(filePath, 1024,768);
                    filename = "0" + "_" + SessionManager.getMyInstance(getContext()).getEmpid()+ "_" +"1" + ".jpg";

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
                    Toast.makeText(getContext(),"Sorry, file path is missing!", Toast.LENGTH_LONG).show();
                }

            }
            else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getContext(),"User cancelled image browsing", Toast.LENGTH_SHORT).show();
            }
            else {
                // failed to record video
                Toast.makeText(getContext(),"Sorry! Failed to browse image", Toast.LENGTH_SHORT).show();
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

    public  void captureImage(Activity activity) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        activity.startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    public  void browseImage(Activity activity) {
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
}
