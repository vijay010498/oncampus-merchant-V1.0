package com.svijayr007.oncampuspartner.ui.orders;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.svijayr007.oncampuspartner.R;
import com.svijayr007.oncampuspartner.adapter.MyOrderAdapter;
import com.svijayr007.oncampuspartner.common.Common;

import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class NewOrdersFragment extends Fragment {
    @BindView(R.id.recycler_order)
    RecyclerView recycler_order;
    @BindView(R.id.status_title)
    TextView status_title;
    MyOrderAdapter adapter;
    private OrderViewModel orderViewModel;
    Unbinder unbinder;


    public NewOrdersFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.new_orders_fragment,container,false);
        unbinder = ButterKnife.bind(this, root);
        recycler_order.setHasFixedSize(true);
        recycler_order.setLayoutManager(new LinearLayoutManager(getContext()));
        orderViewModel =
                new ViewModelProvider(this).get(OrderViewModel.class);
        status_title.setText(new StringBuilder()
                .append(Common.convertStatusToText(getArguments().getInt("orderStatus"))));
        if(getArguments() != null){
            orderViewModel.getMessageError().observe(getViewLifecycleOwner(), new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    Toast.makeText(getContext(), "" + s, Toast.LENGTH_SHORT).show();
                }
            });
            orderViewModel.getOrderModelMutableLiveData(getArguments().getInt("orderStatus")).observe(getViewLifecycleOwner(), orderModels -> {
                if(orderModels != null){
                    Collections.reverse(orderModels);
                    adapter = new MyOrderAdapter(getContext(),orderModels);
                    recycler_order.setAdapter(adapter);
                }

            });
        }
        return root;
    }
    public Fragment OrderFilter(int status){
        Log.i("STATUS", String.valueOf(status));
        NewOrdersFragment NewOrdersFragment = new NewOrdersFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("orderStatus",status);
        NewOrdersFragment.setArguments(bundle);
        return NewOrdersFragment;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

}