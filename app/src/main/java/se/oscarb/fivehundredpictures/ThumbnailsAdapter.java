package se.oscarb.fivehundredpictures;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import se.oscarb.fivehundredpictures.databinding.ItemThumbnailBinding;


public class ThumbnailsAdapter extends RecyclerView.Adapter<ThumbnailsAdapter.ViewHolder> {

    private List<Photo> photos;

    public ThumbnailsAdapter(List<Photo> photos) {
        this.photos = photos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemThumbnailBinding viewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_thumbnail, parent, false);
        return new ViewHolder(viewDataBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.binding.description.setText(photos.get(position).image_url);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ItemThumbnailBinding binding;

        public ViewHolder(ItemThumbnailBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
