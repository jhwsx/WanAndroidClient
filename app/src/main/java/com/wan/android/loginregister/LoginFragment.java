package com.wan.android.loginregister;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wan.android.R;
import com.wan.android.data.bean.AccountData;
import com.wan.android.data.bean.LoginMessageEvent;
import com.wan.android.constant.SpConstants;
import com.wan.android.base.BaseFragment;
import com.wan.android.data.bean.CommonException;
import com.wan.android.util.PreferenceUtils;

import org.greenrobot.eventbus.EventBus;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author wzc
 * @date 2018/3/27
 */
public class LoginFragment extends BaseFragment implements LoginContract.View {

    private LoginContract.Presenter mPresenter;
    private AutoCompleteTextView mNameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private TextView mTvRegister;
    private Button mSignInButton;

    public static LoginFragment newInstance() {

        Bundle args = new Bundle();

        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View inflate = LayoutInflater.from(mActivity).inflate(R.layout.login_fragment, null);
        mSwipeRefreshLayout.addView(inflate);
        // 设置可下拉刷新的子view
        mSwipeRefreshLayout.setSwipeableChildren(R.id.framelayout_login_root);
        // Set up the login form.
        mNameView = (AutoCompleteTextView) view.findViewById(R.id.name);
        mPasswordView = (EditText) view.findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attempLogin();
                    return true;
                }
                return false;
            }
        });

        mSignInButton = (Button) view.findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attempLogin();
            }
        });

        mLoginFormView = view.findViewById(R.id.login_form);
        mProgressView = view.findViewById(R.id.login_progress);
        mTvRegister = (TextView) view.findViewById(R.id.tv_register);
        mTvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, RegisterActivity.class);
                startActivity(intent);
            }
        });
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

    private void attempLogin() {
        // Reset errors.
        mNameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mNameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mNameView.setError(getString(R.string.error_field_required));
            focusView = mNameView;
            cancel = true;
        }

        if (cancel) {
            // There was an ic_error; don't attempt login and focus the first
            // form field with an ic_error.
            focusView.requestFocus();
        } else {
            mPresenter.login(email, password);
        }
    }


    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void showLoginSuccess(AccountData accountData) {
        // 登录成功
        Toast.makeText(mActivity, R.string.login_ok, Toast.LENGTH_SHORT).show();
        // 保存username
        PreferenceUtils.putString(mActivity, SpConstants.KEY_USERNAME, accountData.getUsername());
        // EventBus发消息,通知MyFragment页面更新
        EventBus.getDefault().post(new LoginMessageEvent());

        mActivity.finish();
    }

    @Override
    public void showLoginFail(CommonException e) {
        // 登录失败
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

    private void showProgress(final boolean show) {

        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mActivity.finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
