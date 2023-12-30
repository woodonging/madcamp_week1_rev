package com.example.madcamp_week1_rev

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ScrollView
import android.widget.TextView
import org.w3c.dom.Text

class ContactDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val inflatedView = inflater.inflate(R.layout.fragment_contact_detail, container, false)
        val name = arguments?.getString("name")
        val phone = arguments?.getString("phone")
        val information = arguments?.getString("information")
        val nameView = inflatedView.findViewById<TextView>(R.id.namePart)
        val numberView = inflatedView.findViewById<TextView>(R.id.number)
        val memoView = inflatedView.findViewById<TextView>(R.id.memo)

        nameView.text = name
        numberView.text = phone
        memoView.text = information

        return inflatedView
    }

    companion object {
        fun newInstance(contact: Contact): ContactDetailFragment{
            val fragment = ContactDetailFragment()
            val args = Bundle()
            args.putString("name", contact.name)
            args.putString("phone", contact.phone)
            args.putString("information", contact.information)
            fragment.arguments = args
            return fragment
        }

    }
}
