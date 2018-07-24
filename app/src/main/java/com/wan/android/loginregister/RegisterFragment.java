package com.wan.android.loginregister;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.wan.android.R;
import com.wan.android.base.BaseFragment;
import com.wan.android.data.bean.AccountData;
import com.wan.android.data.bean.CommonException;
import com.wan.android.util.ToastUtils;

/**
 * @author wzc
 * @date 2018/3/27
 */
public class RegisterFragment extends BaseFragment implements RegisterContract.View {
    // UI references.
    private TextInputEditText mNameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mRegisterFormView;
    private EditText mRePasswordView;
    private RegisterContract.Presenter mPresenter;

    public static RegisterFragment newInstance() {

        Bundle args = new Bundle();

        RegisterFragment fragment = new RegisterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View inflate = LayoutInflater.from(mActivity).inflate(R.layout.register_fragment, null);
        mSwipeRefreshLayout.addView(inflate);
        // 设置可下拉刷新的子view
        mSwipeRefreshLayout.setSwipeableChildren(R.id.framelayout_register_root);
        // Set up the register form.
        mNameView =  view.findViewById(R.id.name);
        mPasswordView = (EditText) view.findViewById(R.id.password);
        mRePasswordView = (EditText) view.findViewById(R.id.repassword);
        mRePasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptRegister();
                    return true;
                }
                return false;
            }
        });

        Button mRegisterButton = (Button) view.findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });

        mRegisterFormView = view.findViewById(R.id.register_form);
        mProgressView = view.findViewById(R.id.register_progress);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }, 1000);
                }
            }
        });
    }

    private void attemptRegister() {

        // Store values at the time of the login attempt.
        String username = mNameView.getText().toString();
        String password = mPasswordView.getText().toString();
        String repassword = mRePasswordView.getText().toString();

        mPresenter.register(username, password, repassword);

    }

    @Override
    public void showNetworkError() {
        Logger.d("showNetworkError");
        ToastUtils.showShort(R.string.check_network);
    }

    @Override
    public void showRegisterUsernameEmpty() {
        Logger.d("showRegisterUsernameEmpty");
        ToastUtils.showShort(R.string.login_username_empty);
    }

    @Override
    public void showRegisterPasswordEmpty() {
        Logger.d("showRegisterPasswordEmpty");
        ToastUtils.showShort(R.string.login_password_empty);
    }

    @Override
    public void showRegisterRepasswordEmpty() {
        Logger.d("showRegisterRepasswordEmpty");
        ToastUtils.showShort(R.string.register_repassword_empty);

    }
    @Override
    public void showRegisterPasswordRepasswordInconsistent() {
        Logger.d("showRegisterPasswordRepasswordInconsistent");
        ToastUtils.showShort(R.string.register_password_repassword_inconsistent);
    }

    @Override
    public void showRegisterSuccess(AccountData accountData) {
        Logger.d("showRegisterSuccess");
        ToastUtils.showShort(R.string.register_ok);
        mActivity.finish();
    }

    @Override
    public void showRegisterFail(CommonException e) {
        Toast.makeText(mActivity, e.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressBar() {
        showProgress(true);
    }

    @Override
    public void dismissProgressBar() {
        showProgress(false);
    }

    @Override
    public void setPresenter(RegisterContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mActivity.finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showProgress(final boolean show) {

        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mRegisterFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }
}
