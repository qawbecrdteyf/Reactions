package com.example.android.tabswithswipes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class CreatePoll extends AppCompatActivity {

    private static ImageView closeShare, addImage, loadedImage;
    private static TextView post;
    private final int PICK_IMAGE_REQUEST = 71;
    private Uri filePath;
    static FirebaseAuth auth = FirebaseAuth.getInstance();
    private FrameLayout imageFrameLayout;
    private EditText editCaption;
    private String uploadtext;
    private String uploadUri;
    private Uri downloadUrl;
    static FirebaseDatabase database = FirebaseDatabase.getInstance();
    static DatabaseReference myRef = database.getReference();
    private StorageReference mStorageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mStorageRef = FirebaseStorage.getInstance().getReference();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_poll);
        closeShare = (ImageView)findViewById(R.id.close_share);
        addImage = (ImageView)findViewById(R.id.add_image);
        loadedImage = (ImageView)findViewById(R.id.loaded_image);
        post = (TextView)findViewById(R.id.share_poll);
        imageFrameLayout = (FrameLayout)findViewById(R.id.frame_image_layout);
        editCaption = (EditText)findViewById(R.id.editCaption);

        closeShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                imageFrameLayout.setBackgroundColor(Color.WHITE);
            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CreatePoll.this, "Posting the poll", Toast.LENGTH_SHORT).show();
                if((editCaption == null || editCaption.getText().toString().length() == 0 || editCaption.getText().toString().trim().equals("")) && (loadedImage.getDrawable() == null)){
                    Log.d("posting", "none");
                    Toast.makeText(CreatePoll.this, "Text and image can't be empty", Toast.LENGTH_SHORT);
                }else if(loadedImage.getDrawable() == null){
                    Log.d("posting","only text");
                    uploadtext = editCaption.getText().toString();
                    uploadUri = "";
                }else if(editCaption == null || editCaption.getText().toString().length() == 0 || editCaption.getText().toString().trim().equals("")){
                    Log.d("posting","only img");
                    uploadtext = "";
                    uploadUri = filePath.toString();
                }else{
                    Log.d("posting","both");
                    uploadUri = filePath.toString();
                    uploadtext = editCaption.getText().toString();
                }
                Log.d("Enter", "yes");
                Log.d("UPLOADURI", uploadUri);
                final ProgressDialog pd = new ProgressDialog(CreatePoll.this);
                pd.setTitle("Uploading your new profile pic");
                pd.show();
                Log.d("PD IS CREAED", "WY LORD KARIUS?");
                final StorageReference profileref = mStorageRef.child("images/rivers3.jpg");
                profileref.putFile(Uri.parse(uploadUri)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d("Says success", "is it really true?");
                        pd.dismiss();


                        profileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                downloadUrl = uri;
                                Log.d("downloaded url", downloadUrl.toString());
                                //Do what you want with the url
                            }
                            //Toast.makeText(EditProfileActivity.this,"Upload Done",Toast.LENGTH_LONG).show();
                        });
                        //Toast.makeText(Edithis, "Image successfully uploaded", Toast.LENGTH_SHORT).show();
                    }


                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        //Toast.makeText(EditProfileActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                        Log.d("UPLDERROR", e.getMessage());
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                        pd.setMessage("Uploaded"+(int)progress + "%");
                    }
                });
                String key = myRef.child("posts").push().getKey();
                Log.d("generatedkey", key);
                Post p = new Post(uploadtext,downloadUrl.toString(),auth.getCurrentUser().getUid());
                Log.d("uploadtext", p.getUploadUri());
                myRef.child("posts").child(key).setValue(p);
                Log.d("postdone", "POST DONE");
                myRef.child("exposts").child(auth.getCurrentUser().getUid()).setValue(p);

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                loadedImage.setImageBitmap(bitmap);

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}

