package vn.vnpt.vanquan223.projectnews.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

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
import vn.vnpt.vanquan223.projectnews.model.ListNewsDBModel;
import vn.vnpt.vanquan223.projectnews.model.ListNewsModel;
import vn.vnpt.vanquan223.projectnews.network.APIManager;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RecyclerViewAdapter.IRegisterClick {

    RecyclerView rvRecyclerList;
    List<ListNewsModel> listNewsModels = new ArrayList<>();
    List<ListNewsModel> listBookMark = new ArrayList<>();
    RecyclerViewAdapter adapter;
    private DatabaseHelper db;
    private boolean isScreenBookmark = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        rvRecyclerList = findViewById(R.id.rvRecyclerList);

        db = new DatabaseHelper(this);

        parserRecyclerView();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                HomeActivity.this,
                LinearLayoutManager.VERTICAL,
                false);
        adapter = new RecyclerViewAdapter(HomeActivity.this, listNewsModels);
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
        ListNewsModel listNewsModel;

        if (isScreenBookmark) {
            listNewsModel = listBookMark.get(position);
        }else {
            listNewsModel = listNewsModels.get(position);
        }

        if (listNewsModel.getContent() != null && !listNewsModel.getContent().getRendered().equals("")) {
            Intent intent = new Intent(this, WebViewActivity.class);

            String html = "<h2>" + listNewsModel.getTitle().getRendered() + "</h2>"
                    + listNewsModel.getDate()
                    + listNewsModel.getContent().getRendered();

            intent.putExtra("CONTENT", html);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Không có dữ liệu", Toast.LENGTH_LONG).show();
        }
        Log.d("LOG", "onClickItem: " + position);
    }

    @Override
    public void onClickBookMark(int position) {
        ListNewsModel listNewsModel;
        if (isScreenBookmark == true) {
            listNewsModel = listBookMark.get(position);
        }else {
            listNewsModel = listNewsModels.get(position);
        }

        if (db.checkBookMark(listNewsModel) == false) {
            listNewsModel.setBookMark(true);
            db.insertDB(listNewsModel);
        } else {
            listNewsModel.setBookMark(false);
            db.deleteBookmark(listNewsModel.getId());
        }

        if (isScreenBookmark == true) {
            loadBookmark();
        }else {
            loadListNews();
        }

        Log.d("LOG", "onClickItem: " + position);
    }


    @Override
    public void onClickShare(int position) {
        ListNewsModel listNewsModel = listNewsModels.get(position);
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareSubject = (listNewsModel.getTitle() != null) ? listNewsModel.getTitle().getRendered() : "shareSubject";
        String shareBody = (listNewsModel.getExcerpt() != null) ? listNewsModel.getExcerpt().getRendered() : "shareBody";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSubject);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share Item"));

        Log.d("LOG", "onClickItem: " + position);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            isScreenBookmark = false;
            loadListNews();
        } else if (id == R.id.nav_bookmark) {
            isScreenBookmark = true;
            loadBookmark();
        } else if (id == R.id.nav_about) {

            /*Dialog dialog = new Dialog(HomeActivity.this);
            dialog.setContentView(R.layout.about_layout);
            dialog.setTitle("About");
            dialog.show();*/
            Toast.makeText(this, "Đây là about!", Toast.LENGTH_LONG).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadBookmark() {
        listBookMark = db.selectAllNews();
        for (ListNewsModel list : listBookMark) {
            list.setBookMark(true);
        }
        adapter.reloadData(listBookMark);
    }

    private void loadListNews() {
        for (ListNewsModel model : listNewsModels) {
            model.setBookMark(db.checkBookMark(model));
        }
        adapter.reloadData(listNewsModels);
    }
}
