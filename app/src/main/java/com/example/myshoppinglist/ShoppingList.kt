package com.example.myshoppinglist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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


data class ShoppingItem(       // Data class that will contain information about the item we want to add
    val id: Int,
    var name: String,
    var quantity: Int,
    val isEditing: Boolean = false
)

@Composable
fun ShoppingListApp() {
    var sItems by remember {
        mutableStateOf(listOf<ShoppingItem>())     // We can also store lists in states
    }
    var showDialog by remember {                // State for alert dialog
        mutableStateOf(false)
    }
    var itemName by remember {
        mutableStateOf("")
    }
    var itemQuantity by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp),       // Just like spacer we can also use padding to give space in between our components
        verticalArrangement = Arrangement.Center,
    ) {
        Button(onClick = { showDialog = true }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(text = "Add Item")
        }

        LazyColumn(     // This is used to display lists inside of our apps, this renders only the items that are visible to the user so that memory is used efficiently
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)       // modifier is used to modify properties, here we want lazy column to have all the available space
        ) {
            items(sItems) {     // This will add items to lazy column
                item ->  // Renamed it to item, this means the item that we are currently at or the item that the user has clicked
                if(item.isEditing) {        // Checking if the current item is in editing state or not
                    ShoppingItemEditor(item = item, onEditComplete = {
                        editedName, editedQuantity ->    // Given two inputs one as String(editedName) and one as Int(editedQuantity) to the lambda function
                        sItems = sItems.map { it.copy(isEditing = false) }      // Updating the each item inside sItems and setting the isEditing value of edited item to false
                        val editedItem = sItems.find { it.id == item.id }       // Finding which item is currently edited by using id by going through each element in the list using find{} and 'it'(find return the items one by one) and compares it to the 'item' user have clicked
                        editedItem?.let {                          // Finally updating the values of edited item
                            it.name = editedName
                            it.quantity = editedQuantity
                        }
                    })
                } else {
                    ShoppingListItem(item = item,
                        onEditClick = {
                            // Updating the list and then setting the isEditing value of the item that the user has clicked(currently wants to edit) to true by comparing all the elements inside the list one by one
                            sItems = sItems.map { it.copy(isEditing = it.id == item.id) }
                        },
                        onDeleteClick = {
                            // Deleting the item from the list
                            sItems = sItems - item
                        })
                }
            }
        }
    }

    if(showDialog) {        // Checking if user wants to add an item
        AlertDialog(onDismissRequest = { showDialog = false },
            confirmButton = {       // This is used to confirm choices in alert dialog
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween    // It give maximum space between two items
                            ) {
                                // Add Button
                                Button(onClick = {
                                    if(itemName.isNotBlank() && itemQuantity.isNotBlank()) {        // Checking if the user entered item name and quantity
                                        val newItem = ShoppingItem(id = sItems.size + 1,
                                            name = itemName,
                                            quantity = itemQuantity.toIntOrNull() ?: 0
                                        )
                                        sItems = sItems + newItem       // Adding new item to the list
                                        showDialog = false
                                        itemName = ""
                                        itemQuantity = ""
                                    }
                                }) {
                                    Text(text = "Add")
                                }
                                // Cancel Button
                                Button(onClick = { showDialog = false }) {
                                    Text(text = "Cancel")
                                }
                            }
            },
            title = { Text(text = "Add Shopping Item")},        // Used to give title to alert dialog
            text = {        // Generally used to display text inside the alert dialog
                Column {
                    // Item Name text field
                    OutlinedTextField(value = itemName,
                        onValueChange = {itemName = it},
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        label = { Text(text = "Enter item name")},
                        singleLine = true       // It will ensure only single line entry in text field
                    )

                    // Item Quantity text field
                    OutlinedTextField(value = itemQuantity,
                        onValueChange = {itemQuantity = it},
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        label = { Text(text =  "Enter item quantity")},
                        singleLine = true
                    )
                }
            }
        )
    }
}

@Composable
fun ShoppingItemEditor(
    item: ShoppingItem,
    onEditComplete: (String, Int) -> Unit
    ) {
    var editedName by remember {
        mutableStateOf(item.name)
    }
    var editedQuantity by remember {
        mutableStateOf(item.quantity.toString())
    }
    var isEditing by remember {
        mutableStateOf(item.isEditing)
    }

    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .background(Color.White),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column {
            // Text Field for editing name
            BasicTextField(value = editedName, onValueChange = {editedName = it}, singleLine = true, modifier = Modifier
                .wrapContentSize()
                .padding(8.dp))     // Modifier.wrapContentSize() will give text field only the space that our text will need
            // Text Field for editing quantity
            BasicTextField(value = editedQuantity, onValueChange = {editedQuantity = it}, singleLine = true, modifier = Modifier
                .wrapContentSize()
                .padding(8.dp))
        }

        // Save Button
        Button(onClick = {
            isEditing = false
            onEditComplete(editedName, editedQuantity.toIntOrNull() ?: 0)
        }) {
            Text(text = "Save")
        }
    }
}

@Composable
fun ShoppingListItem(          // Function used to display item
    item: ShoppingItem,
    onEditClick: () -> Unit,        // Lambda function for what should happen on edit button click
    onDeleteClick: () -> Unit       // Lambda function for what should happen on delete button click
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(        // Added border to the row
                border = BorderStroke(2.dp, Color(0xFFD0BCFF)),
                shape = RoundedCornerShape(20)
            ),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = item.name, modifier = Modifier.padding(8.dp))
        Text(text = "Qty: ${item.quantity}", modifier = Modifier.padding(8.dp))
        Row(
            modifier = Modifier.padding(8.dp)
        ) {
            //Edit Button
            IconButton(onClick = { onEditClick }) {     // Icon Button is used where a button only has icon
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Button")
            }
            //Delete Button
            IconButton(onClick = { onDeleteClick }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Button")
            }
        }
    }
}