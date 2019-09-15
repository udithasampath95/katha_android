package com.kbase.katha.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kbase.katha.APIService;
import com.kbase.katha.R;
import com.kbase.katha.RetrofitInstance;
import com.kbase.katha.StoryReadActivity;
import com.kbase.katha.adapter.StoryAdapter;
import com.kbase.katha.model.Story;
import com.startapp.android.publish.adsCommon.StartAppSDK;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HorrorFragment extends Fragment implements SearchView.OnQueryTextListener, StoryAdapter.OnLoadMoreListener {
    private StoryAdapter storyAdapter;
    private SearchView searchView;
    RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    StoryAdapter.OnLoadMoreListener onLoadMoreListener;
    List<Story> list;
    ArrayList<Story> stories;
    private int i;
//    private AdView mAdView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_horror, container, false);
        onLoadMoreListener = this;
        initAds(root);
        recyclerView = root.findViewById(R.id.recyclingView);
        searchView = root.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        StartAppSDK.init(getActivity(), "208604706", true);
        startProgress();
        getAllShortStories();
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

    private void initAds(View root) {
//        mAdView = root.findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder()
//                .build();
//        mAdView.loadAd(adRequest);
    }

    private void getAllShortStories() {
        i = 0;
        APIService getNoticeDataService = RetrofitInstance.getRetrofitInstance().create(APIService.class);
        Call<ArrayList<Story>> call = getNoticeDataService.getAllShortStories(i, "horror");
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
        Call<ArrayList<Story>> call = getNoticeDataService.getAllShortStories(x, "horror");
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