package com.wan.android.data.network.model;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author wzc
 * @date 2018/8/8
 */
public class TagsBeanConverter implements PropertyConverter<List<ArticleDatas.TagsBean>, String> {

    @Override
    public List<ArticleDatas.TagsBean> convertToEntityProperty(String databaseValue) {
        if (databaseValue == null) {
            return null;
        } else {
            List<String> list = Arrays.asList(databaseValue.split(","));
            List<ArticleDatas.TagsBean> result = new ArrayList<>();
            try {
                for (String element : list) {
                    result.add(new Gson().fromJson(element, ArticleDatas.TagsBean.class));
                }
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
            return result;
        }

    }

    @Override
    public String convertToDatabaseValue(List<ArticleDatas.TagsBean> entityProperty) {
        if (entityProperty == null) {
            return null;
        } else {
            StringBuilder sb = new StringBuilder();
            try {
                for (ArticleDatas.TagsBean element : entityProperty) {
                    String s = new Gson().toJson(element);
                    sb.append(s).append(",");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return sb.toString();
        }
    }
}
