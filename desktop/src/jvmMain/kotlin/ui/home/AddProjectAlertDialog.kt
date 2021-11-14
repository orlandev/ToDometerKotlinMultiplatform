/*
 * Copyright 2021 Sergio Belda
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import dev.sergiobelda.todometer.common.compose.ui.components.TitledTextField

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddProjectAlertDialog(
    onDismissRequest: () -> Unit,
    addProject: (name: String) -> Unit
) {
    var projectName by rememberSaveable { mutableStateOf("") }
    var projectNameInputError by remember { mutableStateOf(false) }

    AlertDialog(
        title = {
            Text(text = "Add project", modifier = Modifier.padding(start = 16.dp))
        },
        onDismissRequest = onDismissRequest,
        text = {
            Column {
                TitledTextField(
                    title = "Name",
                    value = projectName,
                    onValueChange = {
                        projectName = it
                        projectNameInputError = false
                    },
                    placeholder = { Text(text = "Enter project name") },
                    singleLine = true,
                    isError = projectNameInputError,
                    errorMessage = "Field must not be empty",
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Done
                    )
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (projectName.isBlank()) {
                        projectNameInputError = true
                    } else {
                        addProject(projectName)
                        onDismissRequest()
                    }
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        },
        modifier = Modifier.requiredWidth(480.dp)
    )
}