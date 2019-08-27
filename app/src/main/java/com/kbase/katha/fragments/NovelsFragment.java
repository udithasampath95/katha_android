package com.kbase.katha.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.kbase.katha.APIService;
import com.kbase.katha.R;
import com.kbase.katha.RetrofitInstance;
import com.kbase.katha.VollySingleton;
import com.kbase.katha.adapter.StoryAdapter;
import com.kbase.katha.model.Customer;
import com.kbase.katha.model.Story;
import com.kbase.katha.utill.AppConsts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NovelsFragment extends Fragment implements SearchView.OnQueryTextListener, StoryAdapter.OnLoadMoreListener {
    private StoryAdapter storyAdapter;
    private SearchView searchView;
    RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    StoryAdapter.OnLoadMoreListener onLoadMoreListener;
    List<Story> list;
    ArrayList<Story> stories;
    private int i;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_novels, container, false);
        onLoadMoreListener = this;
        recyclerView = root.findViewById(R.id.recyclingView);
        searchView = root.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        startProgress();
        getAllShortStories();
//        getDataUsingVollyRequest();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager llManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (dy > 0 && llManager.findLastCompletelyVisibleItemPosition() == (storyAdapter.getItemCount() - 2)) {
                    storyAdapter.showLoading();
                }
            }
        });
    }

//    private void getDataUsingVollyRequest() {
//        i = 0;
//        String url = AppConsts.BASEURL + "offset=" + i + "&" + "story_type=" + "short";
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, null, new com.android.volley.Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(JSONArray response) {
//                try {
//                    JSONObject jsonObject = response.getJSONObject(0);
//                    System.out.println(jsonObject.getString("storyId"));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, new com.android.volley.Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
//                60000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        VollySingleton.getInstance(getContext()).addToRequestQueue(jsonArrayRequest);
//    }

    private void getAllShortStories() {
        i = 0;
        APIService getNoticeDataService = RetrofitInstance.getRetrofitInstance().create(APIService.class);
        Call<ArrayList<Story>> call = getNoticeDataService.getAllShortStories(i, "novels");
        call.enqueue(new Callback<ArrayList<Story>>() {
            @Override
            public void onResponse(Call<ArrayList<Story>> call, Response<ArrayList<Story>> response) {
                dismissProgress();
                stories = new ArrayList<>();
                for (int i = 0; i < response.body().size(); i++) {
                    stories.add(response.body().get(i));
                }
                storyAdapter = new StoryAdapter(stories, getContext(), onLoadMoreListener);
                recyclerView.setAdapter(storyAdapter);
            }

            @Override
            public void onFailure(Call<ArrayList<Story>> call, Throwable t) {
                dismissProgress();
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startProgress() {
        progressDialog = new ProgressDialog(getContext(), R.style.CustomDialogTheme);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMax(100);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }

    private void dismissProgress() {
        progressDialog.dismiss();
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if (storyAdapter == null) {
            System.out.println("No filters");
        } else {
            storyAdapter.filter(s);
        }
        return false;
    }

    @Override
    public void onLoadMore() {
        i = i + 1;
        list = new ArrayList<>();
        APIService getNoticeDataService = RetrofitInstance.getRetrofitInstance().create(APIService.class);
        int x = 5 * i;
        Call<ArrayList<Story>> call = getNoticeDataService.getAllShortStories(x, "novels");
        call.enqueue(new Callback<ArrayList<Story>>() {
            @Override
            public void onResponse(Call<ArrayList<Story>> call, Response<ArrayList<Story>> response) {
                dismissProgress();
                for (int i = 0; i < response.body().size(); i++) {
                    list.add(response.body().get(i));
                }
                storyAdapter.notifyItemInserted(stories.size());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        storyAdapter.dismissLoading();
                        storyAdapter.addItemMore(list);
                        storyAdapter.setMore(true);
                    }
                }, 1000);
            }

            @Override
            public void onFailure(Call<ArrayList<Story>> call, Throwable t) {
                dismissProgress();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        storyAdapter.dismissLoading();
                    }
                }, 1000);

            }
        });
    }
}