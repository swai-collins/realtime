package com.example.bluez.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Binder
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import com.bumptech.glide.Glide
import com.example.bluez.R
import com.example.bluez.model.User
import com.example.bluez.ui.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayOutputStream
import com.example.bluez.ui.LoginActivity
import io.grpc.Context
import android.content.ContentResolver as ContentResolver
import android.content.ContentResolver as ContentResolver1

class ProfileFragment : Fragment() {
    lateinit var mAuth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var image: ImageView
    private val REQUEST_IMAGE_CAPTURE = 100
    private lateinit var imageUri: Uri
    private lateinit var filePath: Uri
    private lateinit var button: Button
    private lateinit var update: Button



    @SuppressLint("RestrictedApi")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        mAuth = FirebaseAuth.getInstance()
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        userInfo()
        return view
        setHasOptionsMenu(true)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        update = view.findViewById(R.id.btnUpdate)
        update.setOnClickListener {
            startFileChooser()
        }

        image = view.findViewById(R.id.imageUser)
        image.setOnClickListener {
            takePicture()
        }
        button = view.findViewById(R.id.btnLogout)
        button.setOnClickListener {
            logout()
        }
        setHasOptionsMenu(true)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_post,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.delete -> {
                showDeleteDialog()
                true
            } else -> super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDeleteDialog() {
        AlertDialog.Builder(context)
            .setTitle("Are you sure?")
            .setMessage("Deleting this account will result in completely removing your account from accessing the system and you won`t be able to access the app.")
            .setPositiveButton("YES") { dialog, _ ->
                dialog.dismiss()
            }
            .setNegativeButton("NO") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    private fun startFileChooser() {
        var i = Intent()
        i.setType("image/*")
        i.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(i,"Choose Picture"),100)

    }

    private fun logout(){
        if (mAuth.currentUser != null)
            mAuth.signOut()
        val intent = Intent(activity, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun takePicture(){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { pictureIntent ->
            pictureIntent.resolveActivity(activity?.packageManager!!)?.also {
                startActivityForResult(pictureIntent,REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null){
            filePath = data.data!!
            var bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, filePath)
            image.setImageBitmap(bitmap)
            val imageBitmap = data?.extras?.get("data") as Bitmap
            uploadImageAndSaveUrl(imageBitmap)
        }
    }

    private fun uploadImageAndSaveUrl(bitmap: Bitmap){
        val baos = ByteArrayOutputStream()
        val storageRef = FirebaseStorage.getInstance().reference.child("pics/${FirebaseAuth.getInstance().currentUser?.uid}")
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos)
        val image = baos.toByteArray()

        val upload = storageRef.putBytes(image)


        upload.addOnCompleteListener { uploadTask ->
            if (upload.isSuccessful){
                storageRef.downloadUrl.addOnCompleteListener { urlTask ->
                    urlTask.result?.let {
                        imageUri = it
                    }
                }
            } else {
                Toast.makeText(activity,"Can`t upload image",Toast.LENGTH_SHORT).show();
            }
        }
    }
    private fun userInfo(){
        val userRef = FirebaseDatabase.getInstance().reference.child("USERS").child(firebaseUser.uid)
        userRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val user = snapshot.getValue<User>(User::class.java)
                    val tvUser: TextView = view!!.findViewById(R.id.tv_User) as TextView
                    val tvName: TextView = view!!.findViewById(R.id.tv_Name_Id) as TextView
                    val tvEmail: TextView = view!!.findViewById(R.id.tvEmail_id) as TextView
                    tvUser.text = user!!.username
                    tvEmail.text = firebaseUser.email
                    tvName.text = user!!.phoneNumber
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}