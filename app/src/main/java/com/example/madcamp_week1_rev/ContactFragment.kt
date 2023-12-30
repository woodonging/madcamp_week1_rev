package com.example.madcamp_week1_rev

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ContactFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val inflatedView = inflater.inflate(R.layout.fragment_contact, container, false)
        val contacts = inflatedView.findViewById<RecyclerView>(R.id.contact_board)
        val contactList = ArrayList<Contact>()

        contactList.add(Contact("a"))
        contactList.add(Contact("b"))
        contactList.add(Contact("c"))
        contactList.add(Contact("d"))


        val contactAdapter = ContactAdapter(contactList)

        contactAdapter.notifyDataSetChanged()
        contacts.adapter = contactAdapter
        contacts.layoutManager = LinearLayoutManager(this.context)

        return inflatedView
    }

}