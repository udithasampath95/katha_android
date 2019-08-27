package com.kbase.katha;


import com.kbase.katha.model.Story;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIService {
    @POST("getAllShortStories.php")
    Call<ArrayList<Story>> getAllShortStories(@Query("offset") int offset, @Query("story_type") String story_type);
}
