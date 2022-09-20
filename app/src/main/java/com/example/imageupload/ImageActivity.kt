package com.example.imageupload

import android.app.ProgressDialog
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView

class ImageActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var pickImage: Button
    private lateinit var upload: Button
    private val mMediaUri: Uri? = null

    private var fileUri: Uri? = null

    private var mediaPath: String? = null

    private val btnCapturePicture: Button? = null

    private var mImageFileLocation = ""

    private lateinit var pDialog: ProgressDialog
    private var postPath: String? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        imageView = findViewById(R.id.preview) as ImageView
        pickImage = findViewById(R.id.pickImage) as Button
        upload = findViewById(R.id.upload) as Button

        pickImage.setOnClickListener(this)
        upload.setOnClickListener(this)
        initDialog()
    }
    override fun onClick(v: View){
        when (v.id){
            R.id.pickImage -> MaterialDialog.Builder(this)
                .title("Set your Image")
                .items(R.array.uploadImages)
                .itemsIds(R.array.itemIds)
        }
    }
}