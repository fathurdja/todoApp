package com.example.walky_talky.Screens

import android.app.AlertDialog
import android.content.DialogInterface
import android.provider.ContactsContract
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavHostController
import com.example.walky_talky.Models.Notes
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects

@Composable
fun HomeScreen(navHostController: NavHostController, modifier: Modifier = Modifier) {
    val db = FirebaseFirestore.getInstance()
    val notesdbRef = db.collection("notes")
    val notesLists = remember { mutableStateListOf<Notes>() }
    val dataValue = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        notesdbRef.addSnapshotListener { value, error ->
            if (error == null) {
                val data = value?.toObjects(Notes::class.java)
                if (data != null) {
                    notesLists.clear()
                    notesLists.addAll(data)
                    dataValue.value = true
                } else {
                    dataValue.value = false
                }
            } else {
                dataValue.value = false
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(containerColor = Color(0xff7eb06d), onClick = { navHostController.navigate("insertScreen/defaultId") }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "add")
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(color = Color(0xff737b4c))
        ) {
            Column(modifier = Modifier.padding(15.dp)) {
                Text(
                    text = "Create Notes \nCrud",
                    style = TextStyle(
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xffA2B06D)
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))

                if (dataValue.value) {
                    LazyColumn {
                        items(notesLists) { Notes ->
                            ListItems(Notes, notesdbRef, navHostController)
                        }
                    }
                } else {
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(25.dp)
                                .align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListItems(notes: Notes, notesdbRef: CollectionReference, navHostController: NavHostController) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 10.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .background(color = Color(0xfff7ffce))
            .padding(10.dp)
    ) {
        DropdownMenu(
            modifier = Modifier.background(color = Color(0xfffbffe7)),
            properties = PopupProperties(clippingEnabled = true),
            offset = DpOffset(x = (-40).dp, y = -50.dp),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text(text = "Update", style = TextStyle(color = Color.Black)) },
                onClick = {
                    expanded = false
                    navHostController.navigate("insertScreen/${notes.id}")
                }
            )

            //delete


            DropdownMenuItem(
                text = { Text(text = "delete", style = TextStyle(color = Color.Black)) },
                onClick = {
                    val alertDialogBox = AlertDialog.Builder(context)
                    alertDialogBox.setTitle("Are you sure you want to delete this note?")
                    alertDialogBox.setPositiveButton("Yes") { dialog, _ ->
                        notesdbRef.document(notes.id).delete()
                        dialog?.dismiss()
                        expanded = false
                    }
                    alertDialogBox.setNegativeButton("No") { dialog, _ ->
                        dialog?.dismiss()
                        expanded = false
                    }
                    alertDialogBox.show()
                }
            )
        }
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = "options",
            tint = Color.Black,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .clickable { expanded = true }
        )

        Column(modifier = Modifier
            .padding(10.dp)
            .fillMaxSize()) {
            Text(text = notes.title, style = TextStyle(color = Color.Black, fontSize = 20.sp))
            Text(text = notes.description, style = TextStyle(color = Color.Gray, fontSize = 15.sp))
        }
    }
}