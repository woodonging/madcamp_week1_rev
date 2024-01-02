package com.example.madcamp_week1_rev

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import de.hdodenhof.circleimageview.CircleImageView

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
        val profileView = view.findViewById<CircleImageView>(R.id.profilePart)
        val nameView = view.findViewById<TextView>(R.id.namePart)
        val numberView = view.findViewById<TextView>(R.id.number)
        val memoView = view.findViewById<TextView>(R.id.memo)
        val editButtonView = view.findViewById<Button>(R.id.editButton)
        val deleteButtonView = view.findViewById<Button>(R.id.deleteButton)
        var contact: Contact? = contactViewModel.getContact(position!!)
        nameView.text = contact!!.name
        numberView.text = contact.phone
        memoView.text = contact.information
        when (contact.profile) {
            is Uri -> {
                profileView.setImageURI(contact.profile as Uri)
                Log.d("Here", "ImageURI")
            }

            is Bitmap -> {
                profileView.setImageBitmap(contact.profile as Bitmap)
                Log.d("Here", "ImageBitmap")
            }

            is Int -> {
                profileView.setImageResource(contact.profile as Int)
                Log.d("Here", "Int")
            }

            is Drawable -> {
                profileView.setImageDrawable(contact.profile as Drawable)
                Log.d("Here", "Drawable")
            }
        }
        editButtonView.setOnClickListener{
            val edit = ContactEditFragment.newInstance(position)
            Log.d("$position", "2번")
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frame, edit)
            transaction.addToBackStack(null)
            transaction.commit()
        }
        deleteButtonView.setOnClickListener {
            val deleteDialog = ContactDelete(contact)
            Log.d("${contact.name}","contact_name")
            deleteDialog.isCancelable = true
            activity?.let {
                deleteDialog.show(it.supportFragmentManager, "DeleteDialog")
            }
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
