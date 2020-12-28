package com.juhi.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.juhi.R;
import com.juhi.Util.PackageListProvider;
import com.juhi.adapter.PackageListAdapter;
import com.juhi.model.PackageItem;
import com.juhi.ui.ListProductActivty;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Packages extends Fragment implements PackageListAdapter.PackageListClickListener {
    private RecyclerView mRecyclerview;
    private PackageListAdapter packageListAdapter;
    private List<PackageItem> packageItemList;


    public Packages() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        packageItemList = PackageListProvider.getPackageItemList();
        packageListAdapter = new PackageListAdapter(getActivity(), packageItemList, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_packages, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerview = view.findViewById(R.id.Packages_list_r_view);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerview.setAdapter(packageListAdapter);

    }

    @Override
    public void onPackageItemSelected(int selected_position) {
        Intent productActivity=new Intent(getActivity(),ListProductActivty.class);
        productActivity.putExtra("collectionname",packageItemList.get(selected_position).getCollectionName());
        startActivity(productActivity);

    }
}
