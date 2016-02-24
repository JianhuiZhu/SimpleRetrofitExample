package com.jianhui_zhu.retrofitexample;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.android.schedulers.AndroidSchedulers;
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit=new Retrofit.Builder().baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final GithubService githubService=retrofit.create(GithubService.class);

        final TextView resultArea=(TextView)findViewById(R.id.result);
        Button button=(Button)findViewById(R.id.start_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Observable.create(new Observable.OnSubscribe<List<Repo>>() {
                        @Override
                        public void call(Subscriber<? super List<Repo>> subscriber) {
                            try {
                                List<Repo> result=githubService.getRepoFromGithub("user").execute().body();
                                subscriber.onNext(result);
                                subscriber.onCompleted();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<List<Repo>>() {
                        @Override
                        public void call(List<Repo> repos) {
                            StringBuilder sb=new StringBuilder();
                            for(Repo repo:repos){
                                sb.append(repo.toString()+"\n");
                            }
                            resultArea.setText(sb.toString());
                        }
                    });
            }
        });

    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }
}
