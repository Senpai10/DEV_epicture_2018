package eu.epitech.epicture;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.io.IOException;

import imageRender.imageRendering;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.support.constraint.Constraints.TAG;

public class SearchFragment extends Fragment {

    private EditText text;
    private ImageButton btn_search;
    private Spinner filtre;
    private String filtreText;
    private imageRendering renderEngine;

    private void buttonClick() {
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_search.setBackgroundColor(Color.parseColor("#06492B"));
                String query = text.getText().toString();
                if (text.isFocused()) {
                    text.clearFocus();
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                renderEngine.httpClient = new OkHttpClient.Builder().build();
                String url = "";
                if (filtreText.equals("Time")) {
                    url = "https://api.imgur.com/3/gallery/search/0?q=";
                } else if (filtreText.equals("Viral")) {
                    url = "https://api.imgur.com/3/gallery/search/viral/0?q=";
                } else if (filtreText.equals("Top")) {
                    url = "https://api.imgur.com/3/gallery/search/top/0?q=";
                }
                Request request = new Request.Builder()
                        .url(url + query)
                        .header("Authorization", "Bearer " + renderEngine.accessToken)
                        .build();
                renderEngine.httpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e(TAG, "An error as occured " + e.toString());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        renderEngine.parseImageResponse(response, getActivity());
                    }
                });
            }
        });
    }

    public void onTouchScreen(View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (text.isFocused()) {
                    text.clearFocus();
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                }
                return false;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View view = inflater.inflate(R.layout.fragment_search, container, false);
        renderEngine = new imageRendering();
        text = view.findViewById(R.id.search_text);
        btn_search = view.findViewById(R.id.search_button);
        filtre = view.findViewById(R.id.spinner_filtre);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.filtre, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filtre.setAdapter(adapter);
        filtre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                filtreText = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        renderEngine.accessToken = getActivity().getIntent().getExtras().getString("token");
        buttonClick();
        onTouchScreen(view);
        return view;
    }
}
