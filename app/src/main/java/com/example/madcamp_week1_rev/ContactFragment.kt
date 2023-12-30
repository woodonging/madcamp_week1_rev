package com.example.madcamp_week1_rev

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader

class ContactFragment : Fragment() {

    private lateinit var contactAdapter: ContactAdapter
    private lateinit var contactList: ArrayList<Contact>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val inflatedView = inflater.inflate(R.layout.fragment_contact, container, false)
        val contacts = inflatedView.findViewById<RecyclerView>(R.id.contact_board)
        val jsonArrayString = readAssetJsonFile("contacts.json")
        val jsonArray = JSONArray(jsonArrayString)

        contactList = ArrayList()

        for (i in 0 until jsonArray.length()){
            val jsonObject = jsonArray.getJSONObject(i)

            val name = jsonObject.getString("name")
            val phone = jsonObject.getString("phone")
            val information = jsonObject.getString("information")

            contactList.add(Contact(name, phone, information))
        }
        contactList.sortBy { it.name }
        this.contactAdapter = ContactAdapter(contactList)

        contactAdapter.setContactClickListener(object: ContactAdapter.OnContactClickListener{
            override fun onClick(view: View, position: Int){
                val details = ContactDetailFragment.newInstance(contactAdapter.contactList[position])
                val transaction = requireActivity().supportFragmentManager.beginTransaction()

                transaction.replace(R.id.frame, details)
                transaction.addToBackStack(null)
                transaction.commit()
            }
        })
        val searchEdit = inflatedView.findViewById<EditText>(R.id.search)

        searchEdit.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(edit: Editable?){
                searchEdit.isCursorVisible = edit.toString().isNotEmpty()
                filterData(edit.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        contacts.adapter = contactAdapter
        contacts.layoutManager = LinearLayoutManager(this.context)
        contactAdapter.notifyDataSetChanged()
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

    private fun filterData(query: String?){
        this.contactAdapter.contactList = contactList
        if (query.isNullOrBlank()){
            this.contactAdapter.contactList = contactList
        }
        else{
            val filteredList = this.contactAdapter.contactList.filter{ contact -> query in contact.name}
            this.contactAdapter.contactList = filteredList as ArrayList<Contact>
        }
        this.contactAdapter.notifyDataSetChanged()
    }

}