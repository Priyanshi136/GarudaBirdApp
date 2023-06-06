package com.example.garudabirdapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Home : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var birdArrayList: ArrayList<Bird>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       val view = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        birdArrayList = arrayListOf<Bird>()

        val birdImageArray = arrayOf(
            R.drawable.img1,
            R.drawable.img2,
            R.drawable.img3,
            R.drawable.img4,
            R.drawable.img5,
            R.drawable.img6,
            R.drawable.img7,
            R.drawable.img8,
            R.drawable.img9
        )

        val birdNameArray = arrayOf("Common tailorbird", "Indian roller", "Indian peafowl", "Rose-ringed parakeet",
                            "Black drongo", "Asian koel", "Kingfisher", "Columbidae", "Satyr tragopan")


        for(index in birdImageArray.indices){
            val bird = Bird(birdNameArray[index], birdImageArray[index])
            birdArrayList.add(bird)
        }

        recyclerView.layoutManager = LinearLayoutManager(context)
        val myAdapter = MyAdapter(birdArrayList, this.requireActivity())
        recyclerView.adapter = myAdapter


        myAdapter.setOnItemClickListener(object : MyAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                // on clicking each item what action do you want to perform.
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://en.wikipedia.org/wiki/${birdNameArray[position]}"))
                startActivity(intent)
            }

        })

        return view
    }
}