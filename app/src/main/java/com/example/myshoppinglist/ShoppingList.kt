package com.example.myshoppinglist

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.unit.dp


data class ShoppingItems(       // Data class that will contain information about the item we want to add
    val id: Int,
    val name: String,
    val quantity: Int,
    val isEditing: Boolean = false
)

@Composable
fun ShoppingListApp() {
    var sItems by remember {
        mutableStateOf(listOf<ShoppingItems>())     // We can also store lists in states
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
                                        val newItem = ShoppingItems(id = sItems.size + 1,
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