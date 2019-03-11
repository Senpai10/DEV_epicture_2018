package eu.epitech.epicture;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import imageRender.imageRendering;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.support.constraint.Constraints.TAG;

public class ProfileFragment extends Fragment {

    private String username;
    private imageRendering renderEngine;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        renderEngine = new imageRendering();
        renderEngine.accessToken = getActivity().getIntent().getExtras().getString("token");
        username = getActivity().getIntent().getExtras().getString("username");
        fetchData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    private void fetchData() {
        renderEngine.httpClient = new OkHttpClient.Builder().build();
        Request request = new Request.Builder()
                .url("https://api.imgur.com/3/account/me/images")
                .header("Authorization","Bearer " + renderEngine.accessToken)
                .build();
        renderEngine.httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "An error as occured " + e);
            }

            @Override
            public void onResponse(Call call, Response response) {
                renderEngine.parseImageResponse(response, getActivity());
            }
        });
    }
}
