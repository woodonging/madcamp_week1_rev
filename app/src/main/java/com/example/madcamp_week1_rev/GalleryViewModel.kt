/*
package com.example.madcamp_week1_rev

import androidx.lifecycle.ViewModel

class GalleryViewModel: ViewModel() {
    private val contactList = arrayListOf<GalleryRecyclerModel>()

    fun getContactList(): ArrayList<Contact>{
        return contactList
    }
    fun getContact(position: Int): Contact{
        return contactList[position]
    }
    fun addContact(contact: Contact){
        contactList.add(contact)
        contactList.sortBy{it.name}
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

    fun updateContact(position: Int, newContact:Contact){
        if (position in 0 until contactList.size)
        {
            contactList[position] = newContact
            contactList.sortBy{it.name}
        }
    }
}*/
