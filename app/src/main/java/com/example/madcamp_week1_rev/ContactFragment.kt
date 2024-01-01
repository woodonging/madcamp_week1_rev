package com.example.madcamp_week1_rev

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader

class ContactFragment : Fragment() {

    private lateinit var contactAdapter: ContactAdapter
    private lateinit var contactViewModel : ContactViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val inflatedView = inflater.inflate(R.layout.fragment_contact, container, false)
        contactViewModel = ViewModelProvider(requireActivity()).get(ContactViewModel::class.java)
        if (contactViewModel.getContactList().isEmpty())
        {
            val inputStream = requireActivity().assets.open("contacts.json")
            val reader = BufferedReader(InputStreamReader(inputStream))
            val stringBuilder = StringBuilder()
            var line: String?
            while(reader.readLine().also {line = it} != null)
            {
                stringBuilder.append(line)
            }
            reader.close()
            val jsonArrayString = stringBuilder.toString()
            val jsonArray = JSONArray(jsonArrayString)
            for (i in 0 until jsonArray.length()){
                val jsonObject = jsonArray.getJSONObject(i)
                val name = jsonObject.getString("name")
                val phone = jsonObject.getString("phone")
                val information = jsonObject.getString("information")

                contactViewModel.addContact(Contact(name, phone, information))
            }
        }


        return inflatedView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val contacts = view.findViewById<RecyclerView>(R.id.contact_board)

        var contactList = contactViewModel.getContactList()
        this.contactAdapter = ContactAdapter(contactList)

        contactAdapter.setContactClickListener(object: ContactAdapter.OnContactClickListener{
            override fun onClick(view: View, contact: Contact){
                var position = contactViewModel.findPosition(contact.name, contact.phone, contact.information)
                val details = ContactDetailFragment.newInstance(position)
                val transaction = requireActivity().supportFragmentManager.beginTransaction()

                transaction.replace(R.id.frame, details)
                transaction.addToBackStack(null)
                transaction.commit()
            }
        })
        val searchEdit = view.findViewById<EditText>(R.id.search)

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
    }
    private fun filterData(query: String?){
        this.contactAdapter.contactList = contactViewModel.getContactList()
        if (query.isNullOrBlank()){
            this.contactAdapter.contactList = contactViewModel.getContactList()
        }
        else{
            this.contactAdapter.contactList = contactViewModel.getContactList().filter{ contact -> query in contact.name } as ArrayList<Contact>
        }
        this.contactAdapter.notifyDataSetChanged()
    }

}