package com.example.madcamp_week1_rev

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ContactAdapter(var contactList: ArrayList<Contact>) :
    RecyclerView.Adapter<ContactAdapter.ContactViewHolder>(){

    class ContactViewHolder(view: View): RecyclerView.ViewHolder(view){
        val contact = view.findViewById<TextView>(R.id.contact_name)!!
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.contacts,parent, false)
        return ContactViewHolder(view)
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.contact.text = contactList[position].name
        holder.itemView.setOnClickListener {
            contactClickListener.onClick(it, contactList[position])
        }
    }

    interface OnContactClickListener{
        fun onClick(view: View, contact: Contact)
    }

    fun setContactClickListener(onContactClickListener: OnContactClickListener){
        this.contactClickListener = onContactClickListener
    }

    private lateinit var contactClickListener: OnContactClickListener

}