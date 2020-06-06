package com.technotium.technotiumapp.config.syncingEntities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.technotium.technotiumapp.config.ImageProcessing;
import com.technotium.technotiumapp.config.JsonParserVolley;
import com.technotium.technotiumapp.config.SessionManager;
import com.technotium.technotiumapp.config.SyncingCallbacks;
import com.technotium.technotiumapp.config.WebUrl;
import com.technotium.technotiumapp.db.dao.DocsDao;
import com.technotium.technotiumapp.db.entities.Docs;
import com.technotium.technotiumapp.docscan.model.UploadDocPojo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PostDocsModule {
    private Context mContext;
    private DocsDao docsDao;
    private long mSyncCount;
    private SyncingCallbacks mSyncingCallbacks;

    public PostDocsModule(Context context, DocsDao docsDao,SyncingCallbacks callbacks){
        mContext=context;
        this.docsDao=docsDao;
        mSyncingCallbacks=callbacks;
    }

    public void getProductListForPost() {
        if(docsDao==null){
            return;
        }
        List<Docs> alstDocs = docsDao.getAllDocsForPush();
        ArrayList<UploadDocPojo> alstPostDocs=new ArrayList<>();
        for(Docs docs: alstDocs){
            UploadDocPojo uploadDocPojo=new UploadDocPojo();
            String filePath = docs.getDoc_path();
            if (filePath != null) {
                Bitmap bitmap = ImageProcessing.decodeSampledBitmapFromFile(filePath, 1024,768);
                Matrix matrix = new Matrix();
                if(bitmap!=null){
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    String encodedPhotoString= compressImage(bitmap);
                    uploadDocPojo.setEncodedString(encodedPhotoString);
                }
            }
            uploadDocPojo.setDoc_type(docs.getDoc_name());
            uploadDocPojo.setOrder_id(docs.getOrder_id());
            uploadDocPojo.setPkid(docs.getPkid());
            alstPostDocs.add(uploadDocPojo);
        }
        if(alstPostDocs.size()>0){
            postDocs(alstPostDocs);
        }


    }

    public void postDocs(ArrayList<UploadDocPojo> alstDocs) {
        final JsonArray  docJsonArray = (JsonArray) new Gson().toJsonTree(alstDocs);
        final JsonParserVolley jsonParserVolley = new JsonParserVolley(mContext);
        jsonParserVolley.addParameter("doc_json",docJsonArray.toString());
        jsonParserVolley.addParameter("userid", SessionManager.getMyInstance(mContext).getEmpid());
        jsonParserVolley.executeRequest(Request.Method.POST, WebUrl. POST_DOCS_SYNC_SERVICE_URL ,new JsonParserVolley.VolleyCallback() {
                    @Override
                    public void getResponse(String response) {
                        Log.d("iss","all doc="+response);
                        try {
                            ResDocs resDocs = new Gson().fromJson(response, ResDocs.class);
                            if(resDocs.getSuccess()==1){
                                Docs docs=new Docs();
                                docs.setPkid(resDocs.getPkid());
                                docs.setServer_pkid(resDocs.getServer_pkid());
                                docs.setPushflag(2);
                                String doc_path=docsDao.getDocs(resDocs.getPkid()+"").getDoc_path();
                                File file = new File(doc_path);
                                if(file.exists()){
                                    file.delete();
                                }
                                docsDao.updateDocs(docs);
                                getProductListForPost();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        );

    }
    public String compressImage(Bitmap bitmap){
        String encodedPhotoString=null;
        if (bitmap != null) {
            int maxHeight=bitmap.getHeight();
            int maxWidth=bitmap.getWidth();
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
        return encodedPhotoString;
    }

}
