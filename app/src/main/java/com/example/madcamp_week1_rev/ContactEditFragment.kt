package com.example.madcamp_week1_rev

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.hdodenhof.circleimageview.CircleImageView

class ContactEditFragment : Fragment() {
    private lateinit var contactViewModel : ContactViewModel
    private var position :Int? = 0
    private lateinit var contact : Contact
    private var additionChecker = false
    private var imageChecker = false
    private lateinit var previousProfile : Any

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contactViewModel = ViewModelProvider(requireActivity()).get(ContactViewModel::class.java)
        position = arguments?.getInt("position")
        Log.d("contactEditFragment","$position")
        if (position==-1){
            contact = Contact("","","", R.drawable.default_profile)
            additionChecker = true
            previousProfile = contact.profile
            contactViewModel.addContact(contact)
            position = contactViewModel.findPosition("", "", "")
            Log.d("contactEditFragment","$additionChecker")
        }
        else{
            contact = contactViewModel.getContact(position!!)!!
            previousProfile = contact.profile
            additionChecker = false
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_contact_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val completeButtonView = view.findViewById<Button>(R.id.completeButton)
        val cancelButtonView = view.findViewById<Button>(R.id.cancelButton)
        val imageAdditionButtonView = view.findViewById<Button>(R.id.imageButton)
        val profileView = view.findViewById<CircleImageView>(R.id.profileEdit)
        val nameEdit = view.findViewById<EditText>(R.id.name)
        val phoneEdit = view.findViewById<EditText>(R.id.number)
        val memoEdit = view.findViewById<EditText>(R.id.memo)
        var changedName : String
        var changedPhone : String
        var changedMemo : String


        if (!additionChecker){
            nameEdit.setText(contact.name)
            phoneEdit.setText(contact.phone)
            memoEdit.setText(contact.information)
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
        }
        else{
            nameEdit.hint = "이름"
            nameEdit.text = null
            phoneEdit.hint = "전화번호"
            phoneEdit.text = null
            memoEdit.hint = "메모"
            memoEdit.text = null
            Log.d("${contact.profile}","profile")
            Log.d("$profileView","profileView")
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
        }

        changedName = contact.name
        changedMemo = contact.information
        changedPhone = contact.phone
        completeButtonView.isEnabled = false
        completeButtonView.setTextColor(Color.LTGRAY)
        nameEdit.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                changedName= s.toString()
                if (additionChecker){
                    if (changedName.isNotEmpty()){
                        completeButtonView.isEnabled = true
                        completeButtonView.setTextColor(Color.WHITE)
                    }
                    else{
                        completeButtonView.isEnabled = false
                        completeButtonView.setTextColor(Color.LTGRAY)
                    }
                }
                else{
                    if(imageChecker||(changedName!=contact.name)||(changedPhone!=contact.phone)||(changedMemo!=contact.information)){
                        completeButtonView.isEnabled = true
                        completeButtonView.setTextColor(Color.WHITE)
                    }
                    else{
                        completeButtonView.isEnabled = false
                        completeButtonView.setTextColor(Color.LTGRAY)
                    }
                    if(changedName.isEmpty()){
                        completeButtonView.isEnabled = false
                        completeButtonView.setTextColor(Color.LTGRAY)
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
        phoneEdit.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                changedPhone= s.toString()
                if (additionChecker){
                    if (changedName.isNotEmpty()){
                        completeButtonView.isEnabled = true
                        completeButtonView.setTextColor(Color.WHITE)
                    }
                    else{
                        completeButtonView.isEnabled = false
                        completeButtonView.setTextColor(Color.LTGRAY)
                    }
                }
                else{
                    if(imageChecker||(changedName!=contact.name)||(changedPhone!=contact.phone)||(changedMemo!=contact.information)){
                        completeButtonView.isEnabled = true
                        completeButtonView.setTextColor(Color.WHITE)
                    }
                    else{
                        completeButtonView.isEnabled = false
                        completeButtonView.setTextColor(Color.LTGRAY)
                    }
                    if(changedName.isEmpty()){
                        completeButtonView.isEnabled = false
                        completeButtonView.setTextColor(Color.LTGRAY)
                    }
                }
            }
        })
        memoEdit.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                changedMemo= s.toString()
                if (additionChecker){
                    if (changedName.isNotEmpty()){
                        completeButtonView.isEnabled = true
                        completeButtonView.setTextColor(Color.WHITE)
                    }
                    else{
                        completeButtonView.isEnabled = false
                        completeButtonView.setTextColor(Color.LTGRAY)
                    }
                }
                else{
                    if(imageChecker||(changedName!=contact.name)||(changedPhone!=contact.phone)||(changedMemo!=contact.information)){
                        completeButtonView.isEnabled = true
                        completeButtonView.setTextColor(Color.WHITE)
                    }
                    else{
                        completeButtonView.isEnabled = false
                        completeButtonView.setTextColor(Color.LTGRAY)
                    }
                    if(changedName.isEmpty()){
                        completeButtonView.isEnabled = false
                        completeButtonView.setTextColor(Color.LTGRAY)
                    }
                }
            }
        })



        completeButtonView.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            contact.name = changedName
            contact.phone = changedPhone
            contact.information = changedMemo
            contactViewModel.updateContact(position!!, contact)
            fragmentManager.popBackStack()
        }

        cancelButtonView.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager

            if (additionChecker){
                contactViewModel.deleteContact(position!!)
                Log.d("deletecontact", "performed with $additionChecker")
            }
            if (imageChecker){
                contactViewModel.getContact(position!!)!!.profile = previousProfile
            }
            fragmentManager.popBackStack()
        }

        imageAdditionButtonView.setOnClickListener {
            val contactGalleryFragment = ContactGalleryFragment.newInstance(position!!)
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frame, contactGalleryFragment)
            transaction.addToBackStack(null)
            transaction.commit()
            imageChecker = true
        }

        view.isFocusableInTouchMode = true
        view.requestFocus()
        view.setOnKeyListener{ _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK){
                requireActivity().supportFragmentManager.popBackStack()
                if (additionChecker){
                    contactViewModel.deleteContact(position!!)
                }
                return@setOnKeyListener true
            }
            false
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