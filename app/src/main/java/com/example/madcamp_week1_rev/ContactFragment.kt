package com.example.madcamp_week1_rev

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class ContactFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val inflatedView = inflater.inflate(R.layout.fragment_contact, container, false)
        val contacts = inflatedView.findViewById<RecyclerView>(R.id.contact_board)
        val jsonArrayString = readAssetJsonFile("contacts.json")
        val jsonArray = JSONArray(jsonArrayString)
        var contactList = ArrayList<Contact>()

//        contactList.add(Contact("a","a",""))
//        contactList.add(Contact("b","b",""))
//        contactList.add(Contact("c","c",""))
//        contactList.add(Contact("d","d",""))
//        contactList.add(Contact("a","e",""))
//        contactList.add(Contact("b","f",""))
//        contactList.add(Contact("c","g",""))
//        contactList.add(Contact("d","a",""))
//        contactList.add(Contact("a","b",""))
//        contactList.add(Contact("b","c",""))
//        contactList.add(Contact("c","d",""))
//        contactList.add(Contact("d","e",""))
        for (i in 0 until jsonArray.length()){
            val jsonObject = jsonArray.getJSONObject(i)

            val name = jsonObject.getString("name")
            val phone = jsonObject.getString("phone")
            val information = jsonObject.getString("information")

            contactList.add(Contact(name, phone, information))
        }
        contactList.sortBy { it.name }
        val contactAdapter = ContactAdapter(contactList)

        contactAdapter.setContactClickListener(object: ContactAdapter.OnContactClickListener{
            override fun onClick(view: View, position: Int){
                val details = ContactDetailFragment.newInstance(contactList[position])
                val transaction = requireActivity().supportFragmentManager.beginTransaction()

                transaction.replace(R.id.frame, details)
                transaction.addToBackStack(null)
                transaction.commit()
            }
        })

        contactAdapter.notifyDataSetChanged()
        contacts.adapter = contactAdapter
        contacts.layoutManager = LinearLayoutManager(this.context)

        return inflatedView
    }

    private fun readAssetJsonFile(fileName: String): String {
        val assetManager = requireContext().assets
        val inputStream = assetManager.open(fileName)
        val reader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()
        var line: String?
        while(reader.readLine().also {line = it} != null)
        {
            stringBuilder.append(line)
        }
        reader.close()
        return stringBuilder.toString()
    }

}