package com.example.bikehub

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bikehub.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var onSaleBikeAdapter: OnSaleBikeAdapter

    private val allOnSaleBikes = generateMockBikes()
    private val displayedOnSaleBikes = mutableListOf<Bike>()
    private var isLoading = false
    private var page = 0
    private val pageSize = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDiscoverRecyclerView()
        setupOnSaleRecyclerView()

        showDiscover()

        binding.btnDiscover.setOnClickListener {
            showDiscover()
        }

        binding.btnBuy.setOnClickListener {
            showOnSaleBikes()
        }
    }

    private fun setupDiscoverRecyclerView() {
        val categories = mockCategories().toMutableList()
        categoryAdapter = CategoryAdapter(categories, { bike ->
        }, showCheckmarks = false)

        binding.recyclerViewCategories.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewCategories.adapter = categoryAdapter

    }
    private fun setupOnSaleRecyclerView() {
        onSaleBikeAdapter = OnSaleBikeAdapter(displayedOnSaleBikes) { bike ->
        }
        binding.recyclerViewOnSale.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewOnSale.adapter = onSaleBikeAdapter

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                displayedOnSaleBikes.removeAt(position)
                onSaleBikeAdapter.notifyItemRemoved(position)
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewOnSale)

        binding.recyclerViewOnSale.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                val lm = rv.layoutManager as LinearLayoutManager
                val totalItemCount = lm.itemCount
                val lastVisible = lm.findLastVisibleItemPosition()

                if (!isLoading && lastVisible >= totalItemCount - 2) {
                    loadMoreOnSaleBikes()
                }
            }
        })
    }


    private fun showDiscover() {
        binding.tvHeader.text = "Discover"
        binding.recyclerViewCategories.visibility = View.VISIBLE
        binding.recyclerViewOnSale.visibility = View.GONE
    }

    private fun showOnSaleBikes() {
        binding.tvHeader.text = "Buy"
        binding.recyclerViewCategories.visibility = View.GONE
        binding.recyclerViewOnSale.visibility = View.VISIBLE
        if (displayedOnSaleBikes.isEmpty()) {
            loadMoreOnSaleBikes()
        }
    }

    private fun loadMoreOnSaleBikes() {
        isLoading = true
        binding.recyclerViewOnSale.post {
            val start = page * pageSize
            val oldSize = displayedOnSaleBikes.size
            for (i in start until start + pageSize) {
                val bike = allOnSaleBikes[i % allOnSaleBikes.size]
                displayedOnSaleBikes.add(bike)
            }
            onSaleBikeAdapter.notifyItemRangeInserted(oldSize, pageSize)
            page++
            isLoading = false
        }
    }

    private fun mockCategories(): List<Category> {
        val sportBikes = mutableListOf(
            Bike(1, "Suzuki GSX-R750", "A lightweight, agile sport bike designed for precision and speed.", R.drawable.suzuki_gsx_r750),
            Bike(2, "Yamaha R1", "High-performance sport bike with advanced electronics and sharp handling.", R.drawable.yamaha_r1),
            Bike(5, "Kawasaki Ninja ZX-6R", "Aggressive middleweight sportbike with a perfect balance of power and control.", R.drawable.kawasaki_ninja_zx6r),
            Bike(6, "Ducati Panigale V2", "Italian superbike combining stunning looks with thrilling performance.", R.drawable.ducati_panigale_v2),
            Bike(7, "Honda CBR1000RR", "A powerful sport bike built for both street and track performance.", R.drawable.honda_cbr1000rr)
        )

        val cruiserBikes = mutableListOf(
            Bike(3, "Harley Davidson", "Classic cruiser with a strong engine and timeless style.", R.drawable.harley_davidson),
            Bike(4, "Indian Chief", "Iconic cruiser known for its vintage design and smooth ride.", R.drawable.indian_chief),
            Bike(8, "Yamaha V Star 250", "Lightweight cruiser ideal for beginners and city cruising.", R.drawable.yamaha_vstar_250),
            Bike(9, "Suzuki Boulevard M50", "Modern cruiser with muscular styling and comfortable ergonomics.", R.drawable.suzuki_boulevard_m50),
            Bike(10, "BMW R18", "Luxury cruiser combining retro design with a powerful engine.", R.drawable.bmw_r18)
        )

        val adventureBikes = mutableListOf(
            Bike(11, "KTM 1290 Super Adventure", "A powerful adventure bike built for long-distance off-road journeys.", R.drawable.ktm_1290_super_adventure),
            Bike(12, "BMW GS Adventure", "Versatile bike perfect for both rugged trails and highway cruising.", R.drawable.bmw_gs_adventure),
            Bike(13, "Honda Africa Twin", "Reliable and rugged motorcycle designed for extreme adventure riding.", R.drawable.honda_africa_twin)
        )

        val electricBikes = mutableListOf(
            Bike(14, "Zero SR/F", "High-performance electric motorcycle offering instant torque and smooth ride.", R.drawable.zero_srf),
            Bike(15, "Harley-Davidson LiveWire", "Electric bike featuring iconic Harley styling and cutting-edge technology.", R.drawable.harley_livewire),
            Bike(16, "Energica Ego", "Italian electric superbike with impressive speed and advanced features.", R.drawable.energica_ego)
        )

        return listOf(
            Category(1, "Sport Bikes", sportBikes),
            Category(2, "Cruiser Bikes", cruiserBikes),
            Category(3, "Adventure Bikes", adventureBikes),
            Category(4, "Electric Bikes", electricBikes)
        )
    }

    private fun generateMockBikes(): List<Bike> {
        return listOf(
            Bike(1, "Honda CBR600RR", "Sporty and reliable", R.drawable.honda_cbr600rr),
            Bike(2, "Yamaha MT-07", "Versatile middleweight", R.drawable.yamaha_mt07),
            Bike(3, "Kawasaki Ninja 400", "Lightweight sport bike", R.drawable.kawasaki_ninja400),
            Bike(4, "Suzuki SV650", "Smooth V-twin", R.drawable.suzuki_sv650),
            Bike(5, "Ducati Monster", "Italian performance", R.drawable.ducati_monster),
            Bike(6, "BMW S1000RR", "High tech superbike", R.drawable.bmw_s1000rr),
            Bike(7, "KTM Duke 390", "Aggressive styling", R.drawable.ktm_duke390),
            Bike(8, "Triumph Street Triple", "Nimble and fun", R.drawable.triumph_street_triple),
            Bike(9, "Harley-Davidson Iron 883", "Classic cruiser", R.drawable.harley_iron_883),
            Bike(10, "Aprilia RSV4", "Racing DNA", R.drawable.aprilia_rsv4),
        )
    }
}
