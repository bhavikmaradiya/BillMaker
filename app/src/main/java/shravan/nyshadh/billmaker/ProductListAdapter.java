package shravan.nyshadh.billmaker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductHolder> {
    private Context context;
    private List<Product> productList;
    private ProductOptionsListener productOptionsListener;

    ProductListAdapter(Context context, ProductOptionsListener productOptionsListener) {
        this.productList = new ArrayList<>();
        this.context = context;
        this.productOptionsListener = productOptionsListener;
    }

    @NonNull
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductHolder(LayoutInflater.from(context).inflate(R.layout.item_product, parent, false));
    }

    private void increaseQuantity(int pos) {
        productList.get(pos).setQuantity(productList.get(pos).getQuantity() + 1);
        notifyItemChanged(pos);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        Product product = productList.get(holder.getAdapterPosition());
        holder.increase.setOnClickListener(view -> {
            product.setQuantity(product.getQuantity() + 1);
            holder.productQuantity.setText(String.valueOf(product.getQuantity()));
            Common.SELECTED_PRODUCTS = productList;
        });
        holder.decrease.setOnClickListener(view -> {
            if (product.getQuantity() == 1) {
                productList.remove(product);
                notifyItemRemoved(holder.getAdapterPosition());
            } else {
                product.setQuantity(product.getQuantity() - 1);
                holder.productQuantity.setText(String.valueOf(product.getQuantity()));
            }
            Common.SELECTED_PRODUCTS = productList;
        });
        holder.productQuantity.setText(String.valueOf(product.getQuantity()));
        holder.productPrice.setText(NumberFormat.getCurrencyInstance(new Locale("en", "us")).format(product.getTotalAmount()));
        holder.productName.setText(product.getProductName());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    boolean hasProducts() {
        return productList.size() > 0;
    }

    void addProduct(Product product) {
        if (productList.size() <= 0) {
            productList.add(product);
            notifyItemInserted(productList.size() + 1);
            productOptionsListener.onProductAdded();
        } else {
            boolean isAdded = false;
            for (int i = 0; i < productList.size(); i++) {
                if (productList.get(i).getSrNo().equals(product.getSrNo())) {
                    increaseQuantity(i);
                    productOptionsListener.onProductAdded();
                    isAdded = true;
                    break;
                }
            }

            if (!isAdded) {
                productList.add(product);
                notifyItemInserted(productList.size() + 1);
                productOptionsListener.onProductAdded();
            }
        }
        Common.SELECTED_PRODUCTS = productList;

    }

    static class ProductHolder extends RecyclerView.ViewHolder {
        ImageView increase, decrease;
        TextView productQuantity, productPrice, productName;

        ProductHolder(@NonNull View itemView) {
            super(itemView);
            increase = itemView.findViewById(R.id.increase);
            decrease = itemView.findViewById(R.id.decrease);
            productName = itemView.findViewById(R.id.productName);
            productQuantity = itemView.findViewById(R.id.productQuantity);
            productPrice = itemView.findViewById(R.id.productPrice);
        }
    }

    public interface ProductOptionsListener {
        void onDelete();

        void onProductAdded();
    }
}
