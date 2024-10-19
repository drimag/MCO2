package com.mobdeve.s20.dimagiba.rafael.mco2

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuInflater
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobdeve.s20.dimagiba.rafael.mco2.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {

    companion object {
        private var data = ArrayList<TreasureHunt>()
        private var filtered_data = ArrayList<TreasureHunt>()

        val user1 = User("kuromi", R.drawable.kuromi)
        val user2 = User("luffy", R.drawable.luffy)
        val user3 = User("chopper", R.drawable.chopper)

        val post1 = TreasureHunt("FIND MY LAPTOP IN LASALLE!!!!!", user1, CustomDate(), Location("Manila"), R.drawable.dlsu)
        val post2 = TreasureHunt("The quick brown fox jumps over the", user2, CustomDate(), Location("Makati"))
        val post3 = TreasureHunt("find my treasure guys heheheheh", user3, CustomDate(2020, 10, 9), Location("Taguig"))

        init {
            // Add all posts to the data list
            data.add(post1)
            data.add(post2)
            data.add(post3)
            filtered_data = data
        }
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var postAdapter: postAdapter
    private lateinit var userProfilePic: ImageView

    private fun showLocationFilterDialog() {
        val locations = arrayOf(
            "All",
            "Caloocan",
            "Las Pinas",
            "Makati",
            "Malabon",
            "Mandaluyong",
            "Manila",
            "Marikina",
            "Muntinlupa",
            "Navotas",
            "Paranaque",
            "Pasay",
            "Pasig",
            "Pateros",
            "Quezon City",
            "San Juan",
            "Taguig",
            "Valenzuela"
        )

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Location")
            .setItems(locations) { dialog, which ->
                val selectedLocation = locations[which]
                Toast.makeText(this, "Filtered by $selectedLocation (Logic not implemented)", Toast.LENGTH_SHORT).show()
//                filtered_data = data.filter { it.location.getCity().toString() == selectedLocation } as ArrayList<TreasureHunt>
            }
            .show()
    }

    private fun showPopupMenu(view: View) {
        // Inflate the menu resource
        val popupMenu = PopupMenu(this, view)
        val inflater: MenuInflater = popupMenu.menuInflater
        inflater.inflate(R.menu.filter_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.filter_by_today -> {
                    Toast.makeText(this, "filter by today selected", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.filter_by_last_week -> {
                    Toast.makeText(this, "filter by last weekselected", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.filter_by_last_month -> {
                    Toast.makeText(this, "filter by last month selected", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.filter_by_year -> {
                    Toast.makeText(this, "filter by year selected", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.filter_by_location -> {
                    showLocationFilterDialog()
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    private val newTreasureResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            val description : String = result.data?.getStringExtra(AddTreasureActivity.TREASURE_CONTENT_KEY)!!

            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setTitle("Reminder")
            builder.setMessage("Please Remember to Verify Your Post in Your Profile Page!")

            builder.setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss() // Close the dialog when OK is clicked
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val viewBinding = MainActivityBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        R.drawable.chopper.let {
            Glide.with(viewBinding.profileImage)
                .load(it)
                .circleCrop()
                .into(viewBinding.profileImage)
        }

        userProfilePic = findViewById(R.id.profileImage)
        userProfilePic.setOnClickListener{
            val intent = Intent(this, UserProfileActivity::class.java)
            startActivity(intent)
        }

        val filterIcon: ImageView = findViewById(R.id.filterIcon)

        filterIcon.setOnClickListener { view ->
            showPopupMenu(view)
        }

        // new treasure listener
        viewBinding.fab.setOnClickListener {
            val intent : Intent = Intent(this@MainActivity, AddTreasureActivity::class.java)
            this.newTreasureResultLauncher.launch(intent)
        }

        this.recyclerView = viewBinding.recyclerView
        this.postAdapter = postAdapter(filtered_data, this)
        this.recyclerView.adapter = postAdapter
        this.recyclerView.layoutManager = LinearLayoutManager(this)
    }
}