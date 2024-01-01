package com.example.madcamp_week1_rev

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ContactEditFragment : Fragment() {
    private lateinit var contactViewModel : ContactViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contactViewModel = ViewModelProvider(requireActivity()).get(ContactViewModel::class.java)
        val completeButtonView = view.findViewById<Button>(R.id.completeButton)
        val cancelButtonView = view.findViewById<Button>(R.id.cancelButton)
        val imageAdditionButtonView = view.findViewById<Button>(R.id.imageButton)
        val nameEdit = view.findViewById<EditText>(R.id.name)
        val phoneEdit = view.findViewById<EditText>(R.id.number)
        val memoEdit = view.findViewById<EditText>(R.id.memo)
        var position = arguments?.getInt("position")
        var contact = contactViewModel.getContact(position!!)


        nameEdit.setText(contact.name)
        phoneEdit.setText(contact.phone)
        memoEdit.setText(contact.information)


        completeButtonView.setOnClickListener {
            completeButtonView.text = "미구현"
        }

        cancelButtonView.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.popBackStack()
        }

        imageAdditionButtonView.setOnClickListener {
            imageAdditionButtonView.text = "미구현"
        }
    }
    companion object {
        fun newInstance(position: Int): ContactEditFragment{
            val fragment = ContactEditFragment()
            val args = Bundle()
            args.putInt("position", position)
            fragment.arguments = args
            return fragment
        }
    }
}