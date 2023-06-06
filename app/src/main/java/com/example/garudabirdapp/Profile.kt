package com.example.garudabirdapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class Profile : Fragment() {

    lateinit var menuBtn: ImageButton
    lateinit var setEmail: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        setEmail = view.findViewById(R.id.profileEmail)

        setEmail.text = FirebaseAuth.getInstance().currentUser?.email

        menuBtn = view.findViewById(R.id.menu_btn)
        menuBtn.setOnClickListener {
            showMenu()
        }

        return view
    }

    private fun showMenu() {
        // display menu
        val popupMenu = PopupMenu(context, menuBtn)
        popupMenu.menu.add("LogOut")
        popupMenu.show()
        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item->

            when(item.title){
                "LogOut"->{
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(context, IntroScreen::class.java))
                    activity?.finish()
                }
            }
            true
        })
    }
}