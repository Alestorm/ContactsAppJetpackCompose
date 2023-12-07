package com.mvvm.contactsapproom.ui.contact.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.mvvm.contactsapproom.application.contact.ContactDto
import com.mvvm.contactsapproom.ui.contact.ContactUiState
import com.mvvm.contactsapproom.ui.contact.ContactsViewModel

@Composable
fun ContactScreen(contactsViewModel: ContactsViewModel) {

    val showDialog: Boolean by contactsViewModel.showDialog.observeAsState(false)

    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val uiState by produceState<ContactUiState>(
        initialValue = ContactUiState.Loading,
        key1 = lifecycle,
        key2 = contactsViewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            contactsViewModel.uiState.collect { value = it }
        }
    }
    when (uiState) {
        is ContactUiState.Error -> {

        }

        is ContactUiState.Loading -> {
            CircularProgressIndicator()
        }

        is ContactUiState.Success -> {
            Box(modifier = Modifier.fillMaxSize()) {
                FabAddContactDialog(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    contactsViewModel = contactsViewModel
                )
                AddContactDialog(
                    show = showDialog,
                    onDismiss = { contactsViewModel.closeDialog() },
                    onContactAdded = { contactsViewModel.addContact(it) }
                )
                ContactList(
                    contacts = (uiState as ContactUiState.Success).contacts,
                    contactsViewModel = contactsViewModel
                )
            }
        }
    }
}

@Composable
fun ContactList(contacts: List<ContactDto>, contactsViewModel: ContactsViewModel) {
    LazyColumn {
        items(contacts, key = { it.id }) { contact ->
            ItemContact(contact, contactsViewModel)
        }
    }
}

@Composable
fun ItemContact(contactDto: ContactDto, contactsViewModel: ContactsViewModel) {
    val showEditDialog: Boolean by contactsViewModel.showEditDialog.observeAsState(false)


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(8.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = contactDto.name[0].toString().uppercase(),
                fontSize = 40.sp,
                modifier = Modifier.padding(8.dp),
                fontWeight = FontWeight.Bold
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = "${contactDto.name} ${contactDto.lastname}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(text = contactDto.phoneNumber)
            }
            IconButton(onClick = {
                contactsViewModel.openEditDialog()
            }
            ) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = null,
                    tint = Color.Blue,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
            IconButton(onClick = { contactsViewModel.deleteContact(contactDto) }
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = null,
                    tint = Color.Red,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
            EditContactDialog(
                show = showEditDialog,
                contactDto = contactsViewModel.findContact(contactDto),
                onDismiss = { contactsViewModel.closeEditDialog() },
                onContactEdit = { contactsViewModel.updateContact(contactDto) }
            )
        }
    }
}

@Composable
fun FabAddContactDialog(modifier: Modifier, contactsViewModel: ContactsViewModel) {
    FloatingActionButton(
        onClick = {
            contactsViewModel.openDialog()
        },
        modifier = modifier
            .padding(16.dp)
            .clip(CircleShape)
    ) {
        Icon(Icons.Default.Add, contentDescription = null)
    }
}

@Composable
fun AddContactDialog(show: Boolean, onDismiss: () -> Unit, onContactAdded: (ContactDto) -> Unit) {
    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var enterprise by remember { mutableStateOf("") }

    if (show) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(Color.White)
            ) {
                Text(
                    text = "Nuevo contacto", fontSize = 20.sp, fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Divider(Modifier.padding(16.dp))
                InputText("Nombre", name) { name = it }
                InputText("Apellido", lastName) { lastName = it }
                InputText("Teléfono", phoneNumber) { phoneNumber = it }
                InputText("Empresa", enterprise) { enterprise = it }

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        onContactAdded(
                            ContactDto(
                                name = name,
                                lastname = lastName,
                                phoneNumber = phoneNumber,
                                enterprise = enterprise
                            )
                        )
                        name = ""
                        lastName = ""
                        phoneNumber = ""
                        enterprise = ""
                    },
                    Modifier.fillMaxWidth()
                ) {
                    Text(text = "Agregar contacto")
                }
            }
        }
    }
}

@Composable
fun EditContactDialog(
    show: Boolean,
    contactDto: ContactDto,
    onDismiss: () -> Unit,
    onContactEdit: (ContactDto) -> Unit
) {


    var id by remember { mutableIntStateOf(contactDto.id) }
    var name by remember { mutableStateOf(contactDto.name) }
    var lastName by remember { mutableStateOf(contactDto.lastname) }
    var phoneNumber by remember { mutableStateOf(contactDto.phoneNumber) }
    var enterprise by remember { mutableStateOf(contactDto.enterprise) }



    if (show) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(Color.White)
            ) {
                Text(
                    text = "Actualizar contacto", fontSize = 20.sp, fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Text(text = id.toString())
                Divider(Modifier.padding(16.dp))
                InputText("Nombre", name) { name = it }
                InputText("Apellido", lastName) { lastName = it }
                InputText("Teléfono", phoneNumber) { phoneNumber = it }
                InputText("Empresa", enterprise) { enterprise = it }

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        onContactEdit(
                            ContactDto(
                                id = id,
                                name = name,
                                lastname = lastName,
                                phoneNumber = phoneNumber,
                                enterprise = enterprise
                            )
                        )
//                        name = ""
//                        lastName = ""
//                        phoneNumber = ""
//                        enterprise = ""
                    },
                    Modifier.fillMaxWidth()
                ) {
                    Text(text = "Editar contacto")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputText(label: String, value: String, onChange: (String) -> Unit) {
    TextField(
        value = value,
        onValueChange = { onChange(it) },
        Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 16.dp),
        label = { Text(text = label) }
    )
}