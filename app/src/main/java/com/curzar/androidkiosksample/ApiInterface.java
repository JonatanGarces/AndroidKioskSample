package com.curzar.androidkiosksample;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

interface  ApiInterface {
    //@GET("/coinmachine")
    //Call<MultipleResource> doGetListResources();
    @POST("/coinmachine/")
    Call<ApiUser> createUser(@Body ApiUser user);

    //@GET("/coinmachine")
   // Call<UserList> doGetUserList(@Query("page") String page);

  //  @FormUrlEncoded
   // @POST("/api/users?")
   // Call<UserList> doCreateUserWithField(@Field("name") String name, @Field("job") String job);
}
