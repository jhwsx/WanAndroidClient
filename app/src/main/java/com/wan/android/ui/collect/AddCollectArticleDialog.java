package com.wan.android.ui.collect;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.wan.android.R;
import com.wan.android.data.network.model.CollectDatas;
import com.wan.android.di.component.ActivityComponent;
import com.wan.android.ui.base.BaseActivity;
import com.wan.android.ui.base.MvpView;

import javax.inject.Inject;

/**
 * @author wzc
 * @date 2018/8/28
 */
public class AddCollectArticleDialog extends DialogFragment implements MvpView, AddCollectContract.View {
    private BaseActivity mActivity;
    public static final String RESPONSE_ADD_COLLECT_DATA = "com.wan.android.response_add_collect_data";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            BaseActivity activity = (BaseActivity) context;
            mActivity = activity;
            activity.onFragmentAttached();
        }
    }

    @Inject
    AddCollectPresenter<AddCollectContract.View> mPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getActivityComponent() != null) {
            getActivityComponent().inject(this);
            mPresenter.onAttach(this);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.collection_add_collect_dialog, null);
        View title = inflater.inflate(R.layout.collection_add_title_dialog, null);
        final EditText etTitle = (EditText) view.findViewById(R.id.title_edit);
        final EditText etAuthor = (EditText) view.findViewById(R.id.author_edit);
        final EditText etLink = (EditText) view.findViewById(R.id.link_edit);
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setCustomTitle(title)
                .setView(view)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                }).create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save(etTitle, etAuthor, etLink);
            }
        });
        return dialog;
    }

    private void save(EditText etTitle, EditText etAuthor, EditText etLink) {
        String title = etTitle.getText().toString();
        String author = etAuthor.getText().toString();
        String link = etLink.getText().toString();
        if (TextUtils.isEmpty(title)) {
            Toast.makeText(mActivity, R.string.title_cannot_be_null, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(link)) {
            Toast.makeText(mActivity, R.string.link_cannot_be_null, Toast.LENGTH_SHORT).show();
            return;
        }
        mPresenter.collectOutOfSiteArticle(title, author, link);
    }

    @Override
    public void onError(String message) {
        if (mActivity != null) {
            mActivity.onError(message);
        }
    }

    @Override
    public void onDestroyView() {
        mPresenter.onDetach();
        super.onDestroyView();
    }

    @Override
    public void showMessage(String message) {
        if (message != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), getString(R.string.some_error), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showMessage(@StringRes int resId) {
        showMessage(getString(resId));
    }

    @Override
    public void onError(@StringRes int resId) {
        if (mActivity != null) {
            mActivity.onError(resId);
        }
    }

    @Override
    public boolean isNetworkConnected() {
        if (mActivity != null) {
            return mActivity.isNetworkConnected();
        }
        return false;
    }

    @Override
    public void onDetach() {
        mActivity = null;
        super.onDetach();
    }

    @Override
    public void hideKeyboard() {
        if (mActivity != null) {
            mActivity.hideKeyboard();
        }
    }

    public ActivityComponent getActivityComponent() {
        return mActivity.getActivityComponent();
    }

    public BaseActivity getBaseActivity() {
        return mActivity;
    }

    @Override
    public void showLoading() {
        // do nothing
    }

    @Override
    public void hideLoading() {
        // do nothing
    }

    @Override
    public void showCollectOutOfSiteArticleSuccess(CollectDatas datas) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(RESPONSE_ADD_COLLECT_DATA, datas);
        getTargetFragment().onActivityResult(MyCollectionFragment.REQUEST_ADD_COLLECT_CODE,
                Activity.RESULT_OK, intent);
        dismiss();
    }

    @Override
    public void showCollectOutOfSiteArticleFail() {
        dismiss();
    }
}
