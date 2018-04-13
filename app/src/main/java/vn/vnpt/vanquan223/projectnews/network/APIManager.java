package vn.vnpt.vanquan223.projectnews.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import vn.vnpt.vanquan223.projectnews.model.ListNewsModel;

public interface APIManager {

    //http://tommyprivateguide.com/wp-json/wp/v2/posts?page=1&per_page=50
    String SERVER_URL_LISTNEWS = "http://tommyprivateguide.com";
    @GET("/wp-json/wp/v2/posts")
    Call<List<ListNewsModel>> getListNews(@Query("page") String page, @Query("per_page") String per_page);
}
