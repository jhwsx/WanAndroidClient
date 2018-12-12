package com.wan.android.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.wan.android.R;
import com.wan.android.data.network.model.AccountData;
import com.wan.android.di.component.ActivityComponent;
import com.wan.android.ui.base.BaseFragment;
import com.wan.android.util.constant.AppConstants;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

/**
 * 登录 Fragment
 *
 * @author wzc
 * @date 2018/8/3
 */
public class LoginFragment extends BaseFragment implements LoginContract.View {

    public static final String EXTRA_USER_NAME = "com.wan.android.extra_user_name";
    private static final String TAG = LoginFragment.class.getSimpleName();
    public static LoginFragment newInstance() {
        Bundle args = new Bundle();
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.name)
    EditText mEtName;
    @BindView(R.id.password)
    EditText mEtPwd;
    @BindView(R.id.sign_in_button)
    Button mBtnLogin;
    @BindView(R.id.tv_register)
    TextView mTvRegister;
    @Inject
    LoginPresenter<LoginContract.View> mPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            setUnBinder(ButterKnife.bind(this, view));
            mPresenter.onAttach(this);
        }
        return view;
    }

    @Override
    protected void setUp(View view) {
        subscribeLoginEvent();
    }

    @Override
    public void onDestroyView() {
        mPresenter.onDetach();
        super.onDestroyView();
    }

    private void subscribeLoginEvent() {
        mPresenter.addRxBindingSubscribe(RxView.clicks(mBtnLogin)
                .throttleFirst(AppConstants.CLICK_INTERVAL_MILLIS, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Timber.d("login");
                        String username = mEtName.getText().toString();
                        String password = mEtPwd.getText().toString();
                        mPresenter.login(username, password);
                    }
                }));
    }

    @OnClick(R.id.tv_register)
    public void register(View view) {
        Timber.d("go to register");
        RegisterActivity.start(getActivity());
    }

    @Override
    public void showLoginSuccess(AccountData accountData) {
        Timber.d("showLoginSuccess: %s", accountData.toString());
        showMessage(R.string.login_ok);
        mPresenter.setLoginStatus(true);
        mPresenter.setUserName(accountData.getUsername());
        Intent intent = new Intent();
        intent.putExtra(EXTRA_USER_NAME, accountData.getUsername());
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }
    @Override
    public String getFragmentName() {
        return TAG;
    }
}
