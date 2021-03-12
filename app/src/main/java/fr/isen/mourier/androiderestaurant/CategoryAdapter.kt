package fr.isen.mourier.androiderestaurant

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import fr.isen.mourier.androiderestaurant.databinding.CellCategoryBinding


class CategoryAdapter(private val categories: List<Item>, private val clickListener: (Item) -> Unit): RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> (){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = CellCategoryBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding.root)
    }

    override fun getItemCount() = categories.size

    override fun onBindViewHolder(holder:CategoryViewHolder, position: Int) {
        holder.title.text = categories[position].name
        holder.price.text = categories [position].getAffichagePrice()
        if(categories[position].getFirstPicture().isNullOrEmpty()){
            Picasso.get().load("https://www.coraf.org/wp-content/themes/consultix/images/no-image-found-360x250.png").into(holder.image)
        }else{
            Picasso.get().load(categories[position].getFirstPicture()).into(holder.image)
        }

        holder.layout.setOnClickListener{
            clickListener.invoke(categories[position])

        }
    }

    class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.cellCategoryName)
        val image: ImageView = view.findViewById(R.id.cellCategoryImage)
        val price : TextView = view.findViewById(R.id.cellCategoryPrice)
        val layout = view.findViewById<View>(R.id.cellLayout)
    }
}