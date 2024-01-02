package com.example.madcamp_week1_rev

import androidx.lifecycle.ViewModel
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader

class ContactViewModel: ViewModel() {
    private val contactList = arrayListOf<Contact>()

    fun getContactList(): ArrayList<Contact>{
        return contactList
    }
    fun getContact(position: Int): Contact?{
        return if (position in 0 until contactList.size) {
            contactList[position]
        } else{
            null
        }
    }
    fun addContact(contact: Contact){
        contactList.add(contact)
    }
    fun findPosition(name: String, number: String, memo: String): Int{
        var contact:Contact
        for (i in 0 until contactList.size){
            contact = contactList[i]
            if ((contact.name==name)&&(contact.phone==number)&&(contact.information==memo)){
                return i
            }
        }
        return -1
    }
    fun setImage(position:Int, image: Any){
        contactList[position].profile = image
    }
    fun deleteContact(position: Int){
        contactList.removeAt(position)
    }
    fun updateContact(position: Int, newContact:Contact){
        if (position in 0 until contactList.size)
        {
            contactList[position] = newContact
        }
    }
}