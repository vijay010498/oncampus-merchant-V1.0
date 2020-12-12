package com.svijayr007.oncampuspartner.ui.deliveryAgents;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.svijayr007.oncampuspartner.R;
import com.svijayr007.oncampuspartner.adapter.DeliveryAgentAdapter;
import com.svijayr007.oncampuspartner.common.SpacesItemDecoration;
import com.svijayr007.oncampuspartner.model.DeliveryAgentModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DeliveryAgentFragment extends Fragment {

    private DeliveryAgentViewModel deliveryAgentViewModel;

    Unbinder unbinder;
    @BindView(R.id.recycler_deliveryAgent)
    RecyclerView recycler_deliveryAgent;
    LayoutAnimationController layoutAnimationController;
    DeliveryAgentAdapter agentAdapter;

    public static DeliveryAgentFragment newInstance() {
        return new DeliveryAgentFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        deliveryAgentViewModel = ViewModelProviders.of(this).get(DeliveryAgentViewModel.class);
        View root =  inflater.inflate(R.layout.delivery_agent_fragment, container, false);

        unbinder = ButterKnife.bind(this,root);
        initViews();
        deliveryAgentViewModel.getMessageError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                Toast.makeText(getContext(), ""+error, Toast.LENGTH_SHORT).show();
            }
        });
        deliveryAgentViewModel.getDeliveryAgentMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<DeliveryAgentModel>>() {
            @Override
            public void onChanged(List<DeliveryAgentModel> deliveryAgentModels) {
                agentAdapter = new DeliveryAgentAdapter(getContext(),deliveryAgentModels);
                recycler_deliveryAgent.setAdapter(agentAdapter);
                recycler_deliveryAgent.setLayoutAnimation(layoutAnimationController);


            }
        });

        return root;
    }

    private void initViews() {
        layoutAnimationController = AnimationUtils.loadLayoutAnimation(getContext(),R.anim.layout_item_fade_in);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recycler_deliveryAgent.setLayoutManager(linearLayoutManager);
        recycler_deliveryAgent.addItemDecoration(new SpacesItemDecoration(5));

    }


}