package com.wan.android.data.client;

import com.wan.android.data.bean.CommonResponse;
import com.wan.android.data.bean.ProjectTreeBranchData;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * @author wzc
 * @date 2018/7/20
 */
public interface ProjectTreeClient {
    // http://www.wanandroid.com/project/tree/json
    @GET("/project/tree/json")
    Observable<CommonResponse<List<ProjectTreeBranchData>>> getProjectTree();
}
