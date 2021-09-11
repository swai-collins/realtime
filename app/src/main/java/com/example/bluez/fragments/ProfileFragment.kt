package com.example.bluez.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.ProgressDialog
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
import android.content.DialogInterface
import android.text.TextUtils
import com.google.firebase.ktx.Firebase


class ProfileFragment : Fragment() {
    lateinit var mAuth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var image: CircleImageView
    private val REQUEST_IMAGE_CAPTURE = 100
    private lateinit var filePath: Uri
    private lateinit var button: Button
    private lateinit var update: Button
    private lateinit var txtEmail: Button
    private lateinit var txtUsername: Button
    private lateinit var txtPhone: Button
    private lateinit var txtId: Button


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
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        update = view.findViewById(R.id.btnUpdate)
        update.setOnClickListener {
            uploadFile()
        }
        image = view.findViewById(R.id.imageUser)
        image.setOnClickListener {
            startFileChooser()
        }
        button = view.findViewById(R.id.btnLogout)
        button.setOnClickListener {
            logout()
        }
        //update userName
        txtUsername =  view.findViewById(R.id.btnUserName)
        txtUsername.setOnClickListener {
        }
        //update Email
        txtEmail = view.findViewById(R.id.btnUpdateEmail)
        txtEmail.setOnClickListener {

        }
        //update Phone
        txtPhone = view.findViewById(R.id.btnUpdatePhone)
        txtPhone.setOnClickListener {

        }
        //update National Id
        txtId = view.findViewById(R.id.btnUpdateId)
        txtId.setOnClickListener {

        }
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_post, menu)
        super.onCreateOptionsMenu(menu, inflater)
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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null  ){
            filePath= data.data!!
            var bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, filePath)
            image.setImageBitmap(bitmap)
        }
    }
    private fun uploadFile(){
        if (filePath!=null){
            var pd = ProgressDialog(context)
            pd.setTitle("Uploading")
            pd.show()
            var imageRef = FirebaseStorage.getInstance().reference.child("images/${FirebaseAuth.getInstance().currentUser?.uid}")
            imageRef.putFile(filePath)
                .addOnSuccessListener {
                    pd.dismiss()
                    Toast.makeText(activity,"Image Uploaded",Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener{p0 ->
                    pd.dismiss()
                    Toast.makeText(activity,p0.message,Toast.LENGTH_LONG).show()
                }
                .addOnProgressListener {p0 ->
                    var progress = (100.0 * p0.bytesTransferred) / p0.totalByteCount
                    pd.setMessage("Upload ${progress.toInt()}%")
                }
        }
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
        i.type = "image/*"
        i.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(i,"Choose Picture"),100)
    }
    private fun logout(){
        if (mAuth.currentUser != null)
            mAuth.signOut()
        val intent = Intent(activity, RegisterActivity::class.java)
        startActivity(intent)
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