package com.example.jade;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Docsscreen extends AppCompatActivity {

    Button upload, viewall;
    ProgressBar progress;
    ImageView image;

    String currentUserId;
    FirebaseAuth mAuth;
    DatabaseReference root;
    StorageReference reference;

    Uri imageuri;

    MaterialToolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docsscreen);

        upload = findViewById(R.id.uploadbtn);
        viewall = findViewById(R.id.viewbtn);
        image = findViewById(R.id.imageView);
        progress = findViewById(R.id.progressBar2);

        toolbar = findViewById(R.id.topAppbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent i;
                int id = item.getItemId();
                drawerLayout.closeDrawer(GravityCompat.START);
                switch (id)
                {
                    case R.id.nav_home:
                        i = new Intent(Docsscreen.this,Homescreen.class);
                        startActivity(i);
                        break;
                    case R.id.nav_translate:
                        i = new Intent(Docsscreen.this,Translatescreen.class);
                        startActivity(i);
                        break;
                    case R.id.nav_maps:
                        i = new Intent(Docsscreen.this,Mapscreen.class);
                        startActivity(i);
                        break;
                    case R.id.nav_docs:
                        i = new Intent(Docsscreen.this,Docsscreen.class);
                        startActivity(i);
                        break;
                    case R.id.nav_pay:
                        i = new Intent(Docsscreen.this,Paymentscreen.class);
                        startActivity(i);
                        break;
                    case R.id.nav_food:
                        i = new Intent(Docsscreen.this,Foodscreen.class);
                        startActivity(i);
                        break;
                    case R.id.nav_logout:
                        i = new Intent(Docsscreen.this,Loginscreen.class);
                        startActivity(i);
                        finish();
                        FirebaseAuth.getInstance().signOut();
                        break;
                    default:
                        return true;
                }
                return true;
            }
        });

        progress.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        root = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId);
        reference = FirebaseStorage.getInstance().getReference();

        viewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Docsscreen.this,Showdocsscreen.class);
                startActivity(i);
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 2);

            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload_to_firebase();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 2 && resultCode == RESULT_OK && data!=null){

            imageuri = data.getData();
            image.setImageURI(imageuri);
        }
    }

    public void upload_to_firebase(){
        if(imageuri != null){
            StorageReference fileRef= reference.child(System.currentTimeMillis() + "." + getFileExtension(imageuri));
            fileRef.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(@NonNull Uri uri) {
                            Model model = new Model(uri.toString());
                            String modelId = root.push().getKey();
                            root.child(modelId).setValue(model);
                            progress.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "Document uploaded successfully!", Toast.LENGTH_SHORT).show();
                            image.setImageResource(R.drawable.upload);
                        }
                    });
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    progress.setVisibility(View.VISIBLE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progress.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Document upload failed!", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            Toast.makeText(getApplicationContext(),"Please select an image document!",Toast.LENGTH_SHORT).show();
        }
    }

    public String getFileExtension(Uri imageuri) {

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(imageuri));
    }
}