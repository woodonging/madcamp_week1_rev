package com.example.madcamp_week1_rev

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
        var changedName : String
        var changedPhone : String
        var changedMemo : String

        if (contact != null){
            nameEdit.setText(contact.name)
            phoneEdit.setText(contact.phone)
            memoEdit.setText(contact.information)
        }
        else{
            contact = Contact("", "", "", GalleryRecyclerModel(R.drawable.default_profile))
            nameEdit.hint = "이름"
            nameEdit.text = null
            phoneEdit.hint = "전화번호"
            phoneEdit.text = null
            memoEdit.hint = "메모"
            memoEdit.text = null
        }
        changedName = contact.name
        changedMemo = contact.information
        changedPhone = contact.phone
        nameEdit.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                changedName= s.toString()
                Log.d("change", "change")

                if((changedName!=contact.name)||(changedPhone!=contact.phone)||((changedMemo!=contact.information))){
                    completeButtonView.isEnabled = true
                    completeButtonView.setTextColor(Color.WHITE)
                }
                else{
                    completeButtonView.isEnabled = false
                    completeButtonView.setTextColor(Color.LTGRAY)
                }
                if((changedName=="")&&(changedPhone=="")&&(changedMemo=="")){
                    completeButtonView.isEnabled = false
                    completeButtonView.setTextColor(Color.LTGRAY)
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        phoneEdit.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                changedPhone= s.toString()

                if((changedName!=contact.name)||(changedPhone!=contact.phone)||((changedMemo!=contact.information))){
                    completeButtonView.isEnabled = true
                    completeButtonView.setTextColor(Color.WHITE)
                }
                else{
                    completeButtonView.isEnabled = false
                    completeButtonView.setTextColor(Color.LTGRAY)
                }
                if((changedName=="")&&(changedPhone=="")&&(changedMemo=="")){
                    completeButtonView.isEnabled = false
                    completeButtonView.setTextColor(Color.LTGRAY)
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
        memoEdit.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                changedMemo= s.toString()

                if((changedName!=contact.name)||(changedPhone!=contact.phone)||((changedMemo!=contact.information))){
                    completeButtonView.isEnabled = true
                    completeButtonView.setTextColor(Color.WHITE)
                }
                else{
                    completeButtonView.isEnabled = false
                    completeButtonView.setTextColor(Color.LTGRAY)
                }
                if((changedName=="")&&(changedPhone=="")&&(changedMemo=="")){
                    completeButtonView.isEnabled = false
                    completeButtonView.setTextColor(Color.LTGRAY)
                }

            }

            override fun afterTextChanged(s: Editable?) {
            }
        })



        completeButtonView.isEnabled = false
        completeButtonView.setTextColor(Color.LTGRAY)
        completeButtonView.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager

            if (position == -1){
                contactViewModel.addContact(Contact(changedName, changedPhone, changedMemo, GalleryRecyclerModel(R.drawable.default_profile)))
            }
            else{
                contactViewModel.updateContact(position, Contact(changedName, changedPhone, changedMemo, GalleryRecyclerModel(R.drawable.default_profile)))
            }
            fragmentManager.popBackStack()
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