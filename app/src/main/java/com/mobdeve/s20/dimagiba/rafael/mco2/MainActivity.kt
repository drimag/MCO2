package com.mobdeve.s20.dimagiba.rafael.mco2

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuInflater
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.mobdeve.s20.dimagiba.rafael.mco2.databinding.MainActivityBinding
import java.time.LocalDate

class MainActivity : AppCompatActivity() {

    /*
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
             Add all posts to the data list
            data.add(post1)
            data.add(post2)
            data.add(post3)
            filtered_data = data
        }
    }

     */

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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showPopupMenu(view: View) {
        // Inflate the menu resource
        val popupMenu = PopupMenu(this, view)
        val inflater: MenuInflater = popupMenu.menuInflater
        inflater.inflate(R.menu.filter_menu, popupMenu.menu)


        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.filter_by_today -> {
                    filterTreasures("Posted Today")
                    true
                }
                R.id.filter_by_last_week -> {
                    filterTreasures("Posted Last Week")
                    true
                }
                R.id.filter_by_last_month -> {
                    filterTreasures("Posted Last Month")
                    true
                }
                R.id.filter_by_year -> {
                    filterTreasures("Posted This Year")
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

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {

       
        super.onCreate(savedInstanceState)
        val viewBinding = MainActivityBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

            // Initialize Firestore and the treasures list
        val db = FirebaseFirestore.getInstance()
        val treasuresList = ArrayList<TreasureHunt>()
        val treasuresAdapter = postAdapter(treasuresList, this)

        val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
        val pfpString = sharedPreferences.getString("pfp", null) // Retrieve user ID
        val pfp = pfpString?.let { getDrawableIdFromString(it) };

        // check if user is logged in first before anything else
        // if not logged in open up the login activity

            // RecyclerView setup
        viewBinding.recyclerView.apply {
            adapter = treasuresAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

            // Load treasures from Firestore with snapshot listener
        db.collection("Treasures").addSnapshotListener { snapshot, error ->
            if (error != null) {
                Toast.makeText(this, "Error fetching treasures: ${error.message}", Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }
            snapshot?.let {
                treasuresList.clear()
                treasuresList.addAll(it.documents.map { document ->

                    document.toObject(TreasureHunt::class.java)?.copy(id = document.id)
                }.filterNotNull()) // Filter out any null objects
                treasuresAdapter.notifyDataSetChanged()
            }
        }


            // Load user profile picture
        Glide.with(this)
            .load(pfp)
            .circleCrop()
            .into(viewBinding.profileImage)

            // Navigate to user profile on profile image click
        viewBinding.profileImage.setOnClickListener {
            startActivity(Intent(this, UserProfileActivity::class.java))
        }

            // Filter icon click listener
        viewBinding.filterIcon.setOnClickListener { view ->
            showPopupMenu(view)
        }

            // Floating Action Button click listener for adding new treasures
        viewBinding.fab.setOnClickListener {
            val intent = Intent(this, AddTreasureActivity::class.java).apply {
                putExtra("userPFP", R.drawable.chopper)
            }
            newTreasureResultLauncher.launch(intent)
        }
        
        /*
        //for getting all posts from Firestore
        val db = FirebaseFirestore.getInstance()
        val treasuresList = mutableListOf<TreasureHunt>()
        val treasuresAdapter = postAdapter(treasuresList, this)




        db.collection("Treasures").addSnapshotListener { snapshot, error ->
            if (error != null) {
                Toast.makeText(this, "Error fetching treasures: ${error.message}", Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }
            treasuresList.clear()
            if (snapshot != null) {
                for (document in snapshot.documents) {
                    val treasure = document.toObject(TreasureHunt::class.java)?.copy(id = document.id)
                    if (treasure != null) {
                        treasuresList.add(treasure)
                    }
                }
                postAdapter.notifyDataSetChanged()
            }
        }




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
            intent.putExtra("userPFP", R.drawable.chopper)
            this.newTreasureResultLauncher.launch(intent)
        }

        this.recyclerView = viewBinding.recyclerView

        //change filtered_data to get all the posts from the database collection of Treasure
        val treasuresArrayList = ArrayList(treasuresList)
        this.postAdapter = postAdapter(treasuresArrayList, this)
        this.recyclerView.adapter = postAdapter
        this.recyclerView.layoutManager = LinearLayoutManager(this)

         */
    }
    private fun getDrawableIdFromString(drawableString: String): Int {
        if (drawableString.startsWith("R.drawable.")) {
            val resourceName = drawableString.substring("R.drawable.".length)
            return resources.getIdentifier(resourceName, "drawable", packageName)
        } else {
            throw IllegalArgumentException("Invalid drawable string: $drawableString")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun filterTreasures(filterType: String) {
        val db = FirebaseFirestore.getInstance()
        val treasuresList = ArrayList<TreasureHunt>()
        val treasuresAdapter = postAdapter(treasuresList, this)

        // Inflate and set the view binding
        val viewBinding = MainActivityBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // Set up RecyclerView
        viewBinding.recyclerView.apply {
            adapter = treasuresAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        // Fetch data from Firestore
        db.collection("Treasures").addSnapshotListener { snapshot, error ->
            if (error != null) {
                // Handle error
                Toast.makeText(this, "Error fetching treasures: ${error.message}", Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }

            snapshot?.let {
                treasuresList.clear()
                treasuresList.addAll(it.documents.map { document ->

                    document.toObject(TreasureHunt::class.java)?.copy(id = document.id)
                }.filterNotNull()) // Filter out any null objects
            }

            val now = LocalDate.now()
            // Filter treasures based on the filter type
            val filteredList = when (filterType) {
                "Posted Today" -> {
                    val startOfDay = now.minusDays(1)
                    treasuresList.filter { treasure ->
                        treasure.toLocalDate()?.isAfter(startOfDay) == true
                    }
                }
                "Posted Last Week" -> {
                    val oneWeekAgo = now.minusWeeks(1)
                    treasuresList.filter { treasure ->
                        treasure.toLocalDate()?.isAfter(oneWeekAgo) == true
                    }
                }
                "Posted Last Month" -> {
                    val oneMonthAgo = now.minusMonths(1)
                    treasuresList.filter { treasure ->
                        treasure.toLocalDate()?.isAfter(oneMonthAgo) == true
                    }
                }
                "Posted This Year" -> {
                    val startOfYear = now.withDayOfYear(1)
                    treasuresList.filter { treasure ->
                        treasure.toLocalDate()?.isAfter(startOfYear.minusDays(1)) == true
                    }
                }
                else -> treasuresList // Default: show all
            }.toCollection(ArrayList()) // Ensure we convert to ArrayList

            treasuresList.clear()
            treasuresList.addAll(filteredList)
            Toast.makeText(this, "filteredlist size: ${filteredList.count()}", Toast.LENGTH_SHORT).show()
            // Update adapter with the filtered list
            treasuresAdapter.notifyDataSetChanged()

        }

        // Load profile image
        val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
        val pfpString = sharedPreferences.getString("pfp", null)
        val pfp = pfpString?.let { getDrawableIdFromString(it) }

        Glide.with(this)
            .load(pfp)
            .circleCrop()
            .into(viewBinding.profileImage)

        // Navigate to user profile on profile image click
        viewBinding.profileImage.setOnClickListener {
            startActivity(Intent(this, UserProfileActivity::class.java))
        }

        // Filter icon click listener
        viewBinding.filterIcon.setOnClickListener { view ->
            showPopupMenu(view)
        }

        // Floating Action Button click listener for adding new treasures
        viewBinding.fab.setOnClickListener {
            val intent = Intent(this, AddTreasureActivity::class.java).apply {
                putExtra("userPFP", R.drawable.chopper)
            }
            newTreasureResultLauncher.launch(intent)
        }
    }

}