package eu.epitech.epicture;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class UploadFragment extends Fragment {

    public static final int REQUEST_CODE_PICK_IMAGE = 1001;
    private Uri selectedImage;
    private ImageView imageView;
    private String accessToken;
    private String picturePath = "";
    private String uploadedImageUrl = "";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accessToken = getActivity().getIntent().getExtras().getString("token");
        if (Build.VERSION.SDK_INT>22) {
            requestPermissions(new String[] {WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    public void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    public void onChooseImage(View view) {
        chooseImage();
    }

    public void uploadImage() {
        if (picturePath != null && picturePath.length() > 0 &&
                accessToken != null && accessToken.length() > 0) {
            Log.d("tag" , "acessToken " + accessToken + "picturePath " + picturePath);
            (new UploadToImgurTask()).execute(picturePath);
            Toast.makeText(getActivity(), "Image successfully uploaded", Toast.LENGTH_SHORT).show();
        }
    }

    public void onUploadImage(View view) {
        uploadImage();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("tag", "request code : " + requestCode + ", result code : " + resultCode);
        if (data == null) {
            Log.d("tag" , "data is null");
        }
        if (resultCode == getActivity().RESULT_OK && requestCode == REQUEST_CODE_PICK_IMAGE && null != data) {
            selectedImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            Log.d("tag", "image path : " + picturePath);
            cursor.close();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    class UploadToImgurTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            final String upload_to = "https://api.imgur.com/3/image";

            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(upload_to);

            try {
                HttpEntity entity = MultipartEntityBuilder.create()
                        .addPart("image", new FileBody(new File(params[0])))
                        .build();

                httpPost.setHeader("Authorization", "Bearer " + accessToken);
                httpPost.setEntity(entity);

                final HttpResponse response = httpClient.execute(httpPost, localContext);

                final String response_string = EntityUtils.toString(response
                        .getEntity());

                final JSONObject json = new JSONObject(response_string);

                Log.d("tag", json.toString());

                JSONObject data = json.optJSONObject("data");
                uploadedImageUrl = data.optString("link");
                Log.d("tag", "uploaded image url : " + uploadedImageUrl);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_upload, container, false);
        imageView = (ImageView) view.findViewById(R.id.img_preview);
        return view;
    }
}
