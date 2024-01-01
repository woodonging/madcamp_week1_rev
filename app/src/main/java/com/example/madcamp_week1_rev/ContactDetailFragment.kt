package com.example.madcamp_week1_rev

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider

class ContactDetailFragment : Fragment() {

    private lateinit var contactViewModel: ContactViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        contactViewModel = ViewModelProvider(requireActivity()).get(ContactViewModel::class.java)
        return inflater.inflate(R.layout.fragment_contact_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val position = arguments?.getInt("position")
        Log.d("$position", "1번")
        val nameView = view.findViewById<TextView>(R.id.namePart)
        val numberView = view.findViewById<TextView>(R.id.number)
        val memoView = view.findViewById<TextView>(R.id.memo)
        val editButtonView = view.findViewById<Button>(R.id.editButton)
        val deleteButtonView = view.findViewById<Button>(R.id.deleteButton)
        var contact: Contact = contactViewModel.getContact(position!!)
        nameView.text = contact.name
        numberView.text = contact.phone
        memoView.text = contact.information

        editButtonView.setOnClickListener{
            val edit = ContactEditFragment.newInstance(position)
            Log.d("$position", "2번")
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frame, edit)
            transaction.addToBackStack(null)
            transaction.commit()
        }
        deleteButtonView.setOnClickListener {
            
        }

    }

    companion object {
        fun newInstance(position: Int): ContactDetailFragment{
            val fragment = ContactDetailFragment()
            val args = Bundle()
            args.putInt("position", position)
            fragment.arguments = args
            return fragment
        }

    }
}
