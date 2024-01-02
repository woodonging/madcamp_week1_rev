package com.example.madcamp_week1_rev

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.madcamp_week1_rev.databinding.ContactDeleteDialogBinding

class ContactDelete(contact : Contact) : DialogFragment() {
    private var _binding: ContactDeleteDialogBinding? = null
    private val binding get() = _binding!!

    private lateinit var contactViewModel : ContactViewModel
    private var contact : Contact = contact
    private var position :Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contactViewModel = ViewModelProvider(requireActivity()).get(ContactViewModel::class.java)
        position = contactViewModel.findPosition(contact.name, contact.phone, contact.information)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ContactDeleteDialogBinding.inflate(inflater, container, false)
        val view = binding.root

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.confirmText.text = "${contact?.name} 연락처를 삭제합니다"
        binding.cancelDeleteButton.setOnClickListener {
            dismiss()
        }
        binding.confirmDeleteButton.setOnClickListener {
            contactViewModel.deleteContact(position!!)
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.popBackStack()
            dismiss()
        }
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}