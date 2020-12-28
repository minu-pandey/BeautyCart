package com.juhi.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.juhi.R;
import com.juhi.Util.GlideApp;
import com.juhi.model.PackageItem;

import java.util.List;

public class PackageListAdapter extends RecyclerView.Adapter<PackageListAdapter.PackageListViewHolder> {
    private List<PackageItem> packageItemList;
    private PackageListClickListener packageListClickListener;
    private Activity context;

    public PackageListAdapter(Activity mContext, List<PackageItem> packageItemList, PackageListClickListener packageListClickListener) {
        this.packageItemList = packageItemList;
        this.context = mContext;
        this.packageListClickListener = packageListClickListener;
    }

    @NonNull
    @Override
    public PackageListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.r_container_package, parent, false);
        return new PackageListViewHolder(view,packageListClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final PackageListViewHolder holder, int position) {
        holder.packageTitle.setText(packageItemList.get(position).getPackageTitle());

        GlideApp.with(context)
                .load(packageItemList.get(position).getResourceId())
                .apply(new RequestOptions().override(472, 473))
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override

                    public boolean onResourceReady(final Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                holder.packageImg.setBackground(resource);
                            }
                        });

                        return false;
                    }
                })
                .submit();


    }


    @Override
    public int getItemCount() {
        return packageItemList.size();
    }

    class PackageListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout packageImg;
        TextView packageTitle;
        CardView packageRoot;
        PackageListClickListener packageListClickListener;


        public PackageListViewHolder(@NonNull View itemView, PackageListClickListener packageListClickListener) {
            super(itemView);
            packageImg = itemView.findViewById(R.id.Package_img);
            packageTitle = itemView.findViewById(R.id.Package_title);
            packageRoot = itemView.findViewById(R.id.Package_root);
            packageRoot.setOnClickListener(this);
            this.packageListClickListener = packageListClickListener;


        }

        @Override
        public void onClick(View view) {
            packageListClickListener.onPackageItemSelected(getAdapterPosition());
        }
    }

    public interface PackageListClickListener {
        void onPackageItemSelected(int selected_position);
    }
}
