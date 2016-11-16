package se.oscarb.fivehundredpictures;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import se.oscarb.fivehundredpictures.databinding.ItemThumbnailBinding;


public class ThumbnailsAdapter extends RecyclerView.Adapter<ThumbnailsAdapter.ViewHolder> {

    private List<Photo> photos;
    private OnThumbnailClickListener thumbnailClickListener;

    public ThumbnailsAdapter(List<Photo> photos) {
        this.photos = photos;
    }

    public void setOnThumbnailClickListener(OnThumbnailClickListener listener) {
        thumbnailClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemThumbnailBinding viewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_thumbnail, parent, false);
        return new ViewHolder(viewDataBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Photo photo = photos.get(position);
        String imageUrl = photo.getImageUrl(2);
        Uri uri = Uri.parse(imageUrl);

        holder.binding.nsfw.setVisibility((photo.isNsfw()) ? View.VISIBLE : View.GONE);
        holder.binding.thumbnail.setImageURI(uri);
        holder.clickListener = thumbnailClickListener;
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public interface OnThumbnailClickListener {
        void onThumbnailClick(int adapterPosition);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ItemThumbnailBinding binding;
        private OnThumbnailClickListener clickListener;

        public ViewHolder(ItemThumbnailBinding itemViewBinding) {
            super(itemViewBinding.getRoot());
            itemViewBinding.getRoot().setOnClickListener(this);
            binding = itemViewBinding;
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            // Check if position is valid and listener is available
            if (position != RecyclerView.NO_POSITION && clickListener != null) {
                clickListener.onThumbnailClick(position);
            }
        }
    }
}
