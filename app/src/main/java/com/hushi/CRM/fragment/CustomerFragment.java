package com.hushi.CRM.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hushi.CRM.R;
import com.p.library.widget.BaseTitle;

public class CustomerFragment extends BaseFragment {

    private View view;
    private BaseTitle mCustomerBaseTitle;

    public static CustomerFragment newInstance() {
        CustomerFragment fragment = new CustomerFragment();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_customer, container, false);
        initView(inflate);
        return inflate;
    }

    private void initView(View inflate) {
        mCustomerBaseTitle = (BaseTitle) inflate.findViewById(R.id.customerBaseTitle);
        mCustomerBaseTitle.disableClickBack();
    }
}
