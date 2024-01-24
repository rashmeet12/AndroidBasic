package com.example.shoppinglistapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingList() {
    var sitems by remember {
        mutableStateOf(listOf<ListItem>())
    }
    var ShowDialog by remember {
        mutableStateOf(false)
    }
    var ItemName by remember {
        mutableStateOf("")
    }
    var ItemQuantity by remember {
        mutableStateOf("")
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(20.dp))
        
        Button(
            onClick = { ShowDialog = true },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Add Item")
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        )
        {
            items(sitems) {
                item ->
                if(item.isEdited) {
                      ShoppingItemEditor(item = item, onEditComplete ={
                          editedName,editedQuantity ->
                          sitems=sitems.map{it.copy(isEdited = false)}
                          val editedItem = sitems.find { it.id==item.id }

                          editedItem?.let {
                              it.name=editedName
                              it.quantity=editedQuantity
                          }
                      } )
                  }
                else{
                    ShoppingListItem(item = item,
                        onEditClick = {
                        sitems=sitems.map { it.copy(isEdited = it.id==item.id) }
                    },
                    onDeleteClick = {sitems=sitems-item})
                }
            }
        }


    }

    if (ShowDialog) {
        AlertDialog(
            onDismissRequest = { ShowDialog = false },
            confirmButton = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = { ShowDialog = false }) {
                        Text(text = "Cancel")
                    }
                    Button(onClick = {
                        if (ItemName.isNotBlank()) {
                            val newitem = ListItem(
                                id = sitems.size + 1,
                                name = ItemName,
                                quantity = ItemQuantity.toInt()
                            )
                            ItemName = ""
                            ItemQuantity = ""
                            ShowDialog = false
                            sitems = sitems + newitem
                        }
                    }) {
                        Text(text = "Add")
                    }
                }
            },

            text = {
                Column() {

                    OutlinedTextField(
                        value = ItemName,
                        onValueChange = { ItemName = it },
                        label = { Text(text = "Enter Item Name") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )

                    OutlinedTextField(
                        value = ItemQuantity,
                        onValueChange = { ItemQuantity = it },
                        label = { Text(text = "Enter Quantity") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }
            },

            title = {
                Text(text = "Add Item")
            },

            shape = CutCornerShape(15.dp),


            )


    }
}
    @Composable
    fun ShoppingListItem(
        item: ListItem,
        onEditClick: () -> Unit,
        onDeleteClick: () -> Unit
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .border(border = BorderStroke(1.dp, Color.Black), shape = RoundedCornerShape(8.dp)), verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = item.name, modifier = Modifier.padding(8.dp))
            Text(text = "Qty: ${item.quantity}", modifier = Modifier.padding(30.dp,8.dp) )
            Row(modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                IconButton(onClick = onEditClick) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                }
            }
        }
    }

@Composable

fun ShoppingItemEditor(item:ListItem,onEditComplete:(String,Int) -> Unit){
    var editedName by remember {
        mutableStateOf(item.name)
    }
    var editedQuantity by remember {
        mutableStateOf(item.quantity.toString())
    }
    var isEditing by remember {
        mutableStateOf(item.isEdited)
    }
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .background(color = Color.White), horizontalArrangement = Arrangement.SpaceEvenly) {
        
        Column(modifier = Modifier.padding(8.dp)) {
            BasicTextField(value = editedName, onValueChange ={ editedName=it} , modifier = Modifier.wrapContentSize())
            BasicTextField(value = editedQuantity, onValueChange ={ editedQuantity=it},modifier = Modifier.wrapContentSize() )
        }
        Button(onClick = { isEditing=false
        onEditComplete(editedName,editedQuantity.toIntOrNull()?: 1)
        }) {
            Text(text = "Save")
        }
        
    }
}



