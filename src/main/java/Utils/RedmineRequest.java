package Utils;

import model.IssuesModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RedmineRequest{

    @GET("issues.json")
    Call<IssuesModel> getAllIssues(@Query(value = "created_on", encoded = true) String created_on, @Query(value = "sort", encoded = true) String sort);

    @POST("createNotification")
    Call<IssuesModel> createNotification();
}
