package imageRender;

import android.graphics.Color;
import android.graphics.Rect;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import eu.epitech.epicture.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.support.constraint.Constraints.TAG;

public class imageRendering {

    public static OkHttpClient httpClient;
    public static String accessToken;

    public static class Photo {
        public String id;
        public String title;
        public Boolean favorite;
    }

    private static class PhotoVH extends RecyclerView.ViewHolder {
        ImageView photo;
        TextView title;
        ImageButton favorite;

        private PhotoVH(View itemView) {
            super(itemView);
        }
    }

    private static void render(final List<Photo> photos, final FragmentActivity layout) {
        RecyclerView rv = layout.findViewById(R.id.rv_of_photos);
        rv.setLayoutManager(new LinearLayoutManager(layout));
        RecyclerView.Adapter<PhotoVH> adapter = new RecyclerView.Adapter<PhotoVH>() {

            @Override
            public PhotoVH onCreateViewHolder(ViewGroup parent, int viewType) {
                PhotoVH vh = new PhotoVH(layout.getLayoutInflater().inflate(R.layout.item, null));
                vh.photo = vh.itemView.findViewById(R.id.photo);
                vh.title = vh.itemView.findViewById(R.id.title);
                vh.favorite = vh.itemView.findViewById(R.id.favorite);
                return vh;
            }

            @Override
            public void onBindViewHolder(PhotoVH holder, int position) {
                Picasso.with(layout).load("https://i.imgur.com/" +
                        photos.get(position).id + ".jpg").into(holder.photo);
                holder.title.setText(photos.get(position).title);
                if (photos.get(position).favorite)
                    holder.favorite.setColorFilter(Color.RED);
                onClickFavorite(holder, position);
            }

            private void onClickFavorite(final PhotoVH holder, final int position) {
                holder.favorite.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        RequestBody reqbody = RequestBody.create(null, new byte[0]);
                        Request request = new Request.Builder()
                                .url("https://api.imgur.com/3/image/" + photos.get(position).id + "/favorite")
                                .header("Authorization", "Bearer " + accessToken)
                                .post(reqbody)
                                .build();
                        httpClient.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.e(TAG, "An error as occurred " + e);
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                Log.i(TAG, response.body().string());
                                if (photos.get(position).favorite) {
                                    holder.favorite.clearColorFilter();
                                    photos.get(position).favorite = false;
                                } else {
                                    holder.favorite.setColorFilter(Color.RED);
                                    photos.get(position).favorite = true;

                                }
                            }
                        });
                    }
                });
            }

            @Override
            public int getItemCount() {
                return photos.size();
            }
        };
        rv.setAdapter(adapter);
        rv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom = 40;
            }
        });
    }

    public void parseImageResponse(Response response, final FragmentActivity fragment) {
        try {
            JSONObject data = new JSONObject(response.body().string());
            JSONArray items = data.getJSONArray("data");
            final List<Photo> photos = new ArrayList<>();
            for(int i=0; i<items.length();i++) {
                JSONObject item = items.getJSONObject(i);
                Photo photo = new Photo();
                if (item.has("is_album")) {
                    if (item.getBoolean("is_album")) {
                        photo.id = item.getString("cover");
                    } else {
                        photo.id = item.getString("id");
                    }
                } else {
                    if (item.getBoolean("in_gallery")) {
                        photo.id = item.getString("cover");
                    } else {
                        photo.id = item.getString("id");
                    }
                }
                photo.title = item.getString("title");
                if (photo.title == "null")
                    photo.title = "No Title";
                photo.favorite = item.getBoolean("favorite");
                photos.add(photo);
            }
            fragment.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    imageRendering.render(photos, fragment);
                }
            });
        }
        catch (Exception e) {
            Log.e(TAG, "An error as occured " + e.toString());
        }
    }
}
