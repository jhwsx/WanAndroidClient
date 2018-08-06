package com.wan.android.ui.login;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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
import io.reactivex.functions.Consumer;
import timber.log.Timber;

/**
 * 注册 Fragment
 * @author wzc
 * @date 2018/8/6
 */
public class RegisterFragment extends BaseFragment implements RegisterContract.View {

    public static RegisterFragment newInstance() {
        Bundle args = new Bundle();
        RegisterFragment fragment = new RegisterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.name)
    EditText mEtName;
    @BindView(R.id.password)
    EditText mEtPassword;
    @BindView(R.id.repassword)
    EditText mEtRepassword;
    @BindView(R.id.register_button)
    Button mBtnRegister;
    @Inject
    RegisterPresenter<RegisterContract.View> mPresenter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_fragment, container, false);
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
        subscribeRegisterEvent();
    }

    private void subscribeRegisterEvent() {
        mPresenter.addRxBindingSubscribe(RxView.clicks(mBtnRegister)
        .throttleFirst(AppConstants.CLICK_INTERVAL_MILLIS, TimeUnit.MILLISECONDS)
        .subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                Timber.d("register");
                String username = mEtName.getText().toString();
                String password = mEtPassword.getText().toString();
                String repassword = mEtRepassword.getText().toString();
                mPresenter.register(username, password, repassword);
            }
        }));
    }

    @Override
    public void showRegisterSuccess(AccountData accountData) {
        showMessage(R.string.register_ok);
        getActivity().finish();
    }

}
