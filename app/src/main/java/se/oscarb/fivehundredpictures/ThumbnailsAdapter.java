package se.oscarb.fivehundredpictures;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import se.oscarb.fivehundredpictures.databinding.ItemThumbnailBinding;


public class ThumbnailsAdapter extends RecyclerView.Adapter<ThumbnailsAdapter.ViewHolder> {


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemThumbnailBinding viewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_thumbnail, parent, false);
        return new ViewHolder(viewDataBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.binding.description.setText("Pos: " + position);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ItemThumbnailBinding binding;

        public ViewHolder(ItemThumbnailBinding itemView) {
            super(itemView.getRoot());
        }
    }
}
