package com.jianhui_zhu.retrofitexample;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by jianhuizhu on 2016-02-23.
 */
public interface GithubService {
    @GET("users/{user}/repos")
    Call<List<Repo>> getRepoFromGithub(@Path("user")String user);
}
