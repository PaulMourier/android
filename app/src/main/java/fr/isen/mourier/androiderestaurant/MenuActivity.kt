package fr.isen.mourier.androiderestaurant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import fr.isen.mourier.androiderestaurant.databinding.ActivityHomeBinding
import fr.isen.mourier.androiderestaurant.databinding.ActivityMenuBinding
import org.json.JSONObject
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.i("myTag", "je pense que ca marche")
        val categoryName = intent.getStringExtra("category")
        binding.categoryTitle.text = categoryName
        loadFromUrl(categoryName ?: "")

        //binding.listeMenu.layoutManager = LinearLayoutManager(this)
        //binding.listeMenu.adapter = CategoryAdapter(listOf("salade", "burger", "coucou", "numero", "deux", "oignons")) { item ->
        //    val intent = Intent(this, DetailActivity::class.java)
        //    intent.putExtra("dish", item)
        //    startActivity(intent)
        //}
    }

    private fun loadFromUrl(category: String) {
        var url = "http://test.api.catering.bluecodegames.com/menu"
        val jsonIdShop = JSONObject()
        jsonIdShop.put("id_shop", "1")

        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, jsonIdShop,
                Response.Listener { response ->
                    Log.i("myTag", "${response.toString()}")
                    parsingResult(response.toString(), category)
                },
                Response.ErrorListener { error ->
                    // TODO: Handle error
                    Log.e("erreur", "response: ${error.message}")
                }
        )
        Volley.newRequestQueue(this).add(jsonObjectRequest)
    }

    private fun parsingResult(result: String, category: String?) {
        val listMenu = GsonBuilder().create().fromJson(result, DataResult::class.java)
        val items = listMenu.data.firstOrNull { it.name == category }
        loadList(items?.items)
        //Log.i("myTag222", "response: ${items}")
    }

    private fun loadList(items: List<Item>?) {
        items?.let {
            val adapter = CategoryAdapter(it) { item ->
                val intent = Intent(this, DetailActivity::class.java)
                intent.putExtra("items", item)
                startActivity(intent)
                }
            binding.listeMenu.layoutManager = LinearLayoutManager(this)
            binding.listeMenu.adapter = adapter
        }
    }
}