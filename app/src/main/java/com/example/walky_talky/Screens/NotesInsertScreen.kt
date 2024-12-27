package com.example.walky_talky.Screens

import android.text.style.ClickableSpan
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.walky_talky.Models.Notes
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun NotesInsertScreen(navHostController: NavHostController, id: String?){
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val notesdbRef = db.collection("notes")

    val title = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
       // Toast.makeText(context,""+id,Toast.LENGTH_SHORT).show()
        if (id != "defaultId"){
            notesdbRef.document(id.toString()).get().addOnSuccessListener {
                val singleData = it.toObject(Notes::class.java)
                title.value =singleData!!.title
                description.value =singleData.description
            }
        }
    }

    Scaffold (floatingActionButton = {
        FloatingActionButton(containerColor = Color(0xff7eb06d),onClick = {
            if(title.value.isEmpty() && description.value.isEmpty()){
                Toast.makeText(context,"Enter valid data",Toast.LENGTH_SHORT).show()
            }
            else{

                var mynotesID = ""

                mynotesID = if (id != "defaultId"){
                    id.toString()

                } else{
                    notesdbRef.document().id
                }
                val notes = Notes(
                    id = mynotesID,
                    title = title.value,
                    description = description.value
                )
                notesdbRef.document(mynotesID).set(notes).addOnCompleteListener(){
                    if (it.isSuccessful){
                        Toast.makeText(
                            context,
                            "Notes add successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        navHostController.popBackStack()
                    }
                    else{
                        Toast.makeText(
                            context,
                            "Something gone wrong",
                            Toast.LENGTH_SHORT).show()

                    }
                }

            }
        },
            ) {
            Icon(imageVector = Icons.Default.Done, contentDescription = "add")
        }
    }){innerPadding->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(color = Color(0xff737b4c))
        ) {
            Column (modifier = Modifier.padding(15.dp)){
            Text(modifier = Modifier.clickable {

                    if(title.value.isEmpty() && description.value.isEmpty()){
                        Toast.makeText(context,"Enter valid data",Toast.LENGTH_SHORT).show()
                    }
                    else{

                        var mynotesID = ""

                        mynotesID = if (id != "defaultId"){
                            id.toString()

                        } else{
                            notesdbRef.document().id
                        }
                        val notes = Notes(
                            id = mynotesID,
                            title = title.value,
                            description = description.value
                        )
                        notesdbRef.document(mynotesID).set(notes).addOnCompleteListener(){
                            if (it.isSuccessful){
                                Toast.makeText(
                                    context,
                                    "Notes add successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                navHostController.popBackStack()
                            }
                            else{
                                Toast.makeText(
                                    context,
                                    "Something gone wrong",
                                    Toast.LENGTH_SHORT).show()

                            }
                        }

                    }



            },text = "Insert Data",
                style = TextStyle(fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xffA2B06D)))


                Spacer(modifier = Modifier.height(15.dp))


                OutlinedTextField(shape = RoundedCornerShape(10.dp),
                    colors = TextFieldDefaults.colors(focusedContainerColor = Color(0xfff7ffce),
                        unfocusedTextColor = Color(0xfff7ffce),
                        unfocusedLabelColor = Color(0xfff7ffce),
                        focusedLabelColor = Color(0xfff7ffce),
                        focusedTextColor = Color.Black,
                        focusedIndicatorColor = Color(0xfff7ffce),
                        unfocusedIndicatorColor = Color(0xfff7ffce),
                        unfocusedContainerColor = Color.Transparent)
                    , label = {Text(text = "Enter your Title")}
                    , value = title.value
                    ,onValueChange = {title.value=it}
                    , modifier = Modifier.fillMaxWidth())

                Spacer(modifier = Modifier.height(15.dp))
                OutlinedTextField(shape = RoundedCornerShape(10.dp),
                    colors = TextFieldDefaults.colors(focusedContainerColor = Color(0xfff7ffce),
                        unfocusedTextColor = Color(0xfff7ffce),
                        unfocusedLabelColor = Color(0xfff7ffce),
                        focusedLabelColor = Color(0xfff7ffce),
                        focusedTextColor = Color.Black,
                        focusedIndicatorColor = Color(0xfff7ffce),
                        unfocusedIndicatorColor = Color(0xfff7ffce),
                        unfocusedContainerColor = Color.Transparent)
                    , label = {Text(text = "Enter your description")}
                    , value = description.value
                    ,onValueChange = {description.value = it}
                    , modifier = Modifier.fillMaxWidth().fillMaxHeight(0.6f))
            }
        }

    }
}