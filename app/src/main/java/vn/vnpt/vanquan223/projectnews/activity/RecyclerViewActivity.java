package vn.vnpt.vanquan223.projectnews.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vn.vnpt.vanquan223.projectnews.R;
import vn.vnpt.vanquan223.projectnews.adapter.RecyclerViewAdapter;
import vn.vnpt.vanquan223.projectnews.database.DatabaseHelper;
import vn.vnpt.vanquan223.projectnews.model.ListNewsModel;
import vn.vnpt.vanquan223.projectnews.network.APIManager;

public class RecyclerViewActivity extends AppCompatActivity implements RecyclerViewAdapter.IRegisterClick {
    RecyclerView rvRecyclerList;
    List<ListNewsModel> listNewsModels = new ArrayList<>();
    RecyclerViewAdapter adapter;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        rvRecyclerList = findViewById(R.id.rvRecyclerList);
        db = new DatabaseHelper(this);

        parserRecyclerView();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                RecyclerViewActivity.this,
                LinearLayoutManager.VERTICAL,
                false);
        adapter = new RecyclerViewAdapter(RecyclerViewActivity.this, listNewsModels);
        rvRecyclerList.setLayoutManager(layoutManager);
        adapter.registerClickAdapter(this);
        rvRecyclerList.setAdapter(adapter);
    }

    /*
     * Sử dụng ListView hiển thị nhiều dữ liệu lấy từ API
     * */
    private void parserRecyclerView() {
        String page = "1";
        String per_page = "50";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIManager.SERVER_URL_LISTNEWS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIManager manager = retrofit.create(APIManager.class);
        manager.getListNews(page, per_page).enqueue(new Callback<List<ListNewsModel>>() {
            @Override
            public void onResponse(Call<List<ListNewsModel>> call, Response<List<ListNewsModel>> response) {
                listNewsModels = response.body();

                for (ListNewsModel list : listNewsModels) {
                    list.setBookMark(db.checkBookMark(list));
                }

                adapter.reloadData(listNewsModels);
            }

            @Override
            public void onFailure(Call<List<ListNewsModel>> call, Throwable t) {
            }
        });

    }

    @Override
    public void onClickItem(int position) {
        Log.d("LOG", "onClickItem: " + position);
    }

    @Override
    public void onClickBookMark(int position) {
        ListNewsModel listNewsModel = listNewsModels.get(position);
        if (!db.checkBookMark(listNewsModel)) {
            listNewsModel.setBookMark(true);
            db.insertDB(listNewsModel);
            adapter.reloadData(listNewsModels);
        } else {
            //delete
            listNewsModel.setBookMark(false);
            db.deleteBookmark(listNewsModel.getId());
            adapter.reloadData(listNewsModels);
        }
        Log.d("LOG", "onClickItem: " + position);
    }


    @Override
    public void onClickShare(int position) {
        ListNewsModel listNewsModel = listNewsModels.get(position);
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareSubject = (listNewsModel.getTitle() != null)?listNewsModel.getTitle().getRendered():"shareSubject";
        String shareBody = (listNewsModel.getExcerpt() != null)?listNewsModel.getExcerpt().getRendered():"shareBody";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSubject);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share Item"));

        Log.d("LOG", "onClickItem: " + position);
    }
}
