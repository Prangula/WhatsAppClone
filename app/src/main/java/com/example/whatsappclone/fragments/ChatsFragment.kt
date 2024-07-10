package com.example.whatsappclone.fragments

import android.annotation.SuppressLint
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.PermissionRequest
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.whatsappclone.R
import com.example.whatsappclone.adapter.ContactAdapter
import com.example.whatsappclone.databinding.ChatsFragmentBinding
import com.example.whatsappclone.models.ContactData
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

import java.util.Date

class ChatsFragment : Fragment(R.layout.chats_fragment) {

    private lateinit var adapter: ContactAdapter
    private var items: ArrayList<ContactData> = ArrayList()
    private lateinit var binding: ChatsFragmentBinding
    private var date = Date()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ChatsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        requestPermissions()
    }

    private fun setupRecyclerView() {
        adapter = ContactAdapter()
        binding.rvChat.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@ChatsFragment.adapter
        }
    }

    private fun requestPermissions() {
        Dexter.withContext(requireContext())
            .withPermissions(android.Manifest.permission.READ_CONTACTS)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report != null && report.areAllPermissionsGranted()) {
                        getContacts()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                    p1: PermissionToken?
                ) {
                    TODO("Not yet implemented")
                }


            }).onSameThread().check()
    }

    @SuppressLint("Range", "NotifyDataSetChanged")
    private fun getContacts() {
        val phones: Cursor? = requireContext().contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null, null, null, null
        )

        phones?.use {
            val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            val photoIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI)

            items.clear() // Clear existing items before adding new ones

            while (it.moveToNext()) {
                val name = it.getString(nameIndex) ?: ""
                val phoneNumber = it.getString(numberIndex) ?: ""
                val image = it.getString(photoIndex) ?: android.R.drawable.ic_dialog_email

                // Check if the phone number already exists in items to avoid duplicates
                val existingContact = items.find {
                    contact -> contact.name == name
                }
                if (existingContact == null) {
                    val contact = ContactData(name, phoneNumber, image.toString(), date)
                    items.add(contact)
                }
            }

            items.sortBy { it.name }
            adapter.items = items
        }
    }


}