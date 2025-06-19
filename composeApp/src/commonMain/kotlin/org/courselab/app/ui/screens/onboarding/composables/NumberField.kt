package org.courselab.app.ui.screens.onboarding.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun NumberField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    decimals: Int = 0,
    allowNegative: Boolean = false,
    isCompulsory: Boolean = false,
) {
    val decimalPart = if (decimals > 0) "(?:\\.\\d{0,$decimals})?" else ""
    val signPart = if (allowNegative) "-?" else ""
    val regex = remember(decimals, allowNegative) {
        Regex("^$signPart\\d*$decimalPart\$")
    }

    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(25.dp),
        value = value,
        onValueChange = { new ->
            println("sadfsfasdfadsf")
            if (regex.matches(new)) onValueChange(new)
        },
        label = {
            Text(
                text = if (isCompulsory && value.isBlank()) "$label (Required)" else label
            )
        },
        isError = isCompulsory && value.isBlank(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = if (decimals == 0) KeyboardType.Number else KeyboardType.Decimal
        ),
        trailingIcon = {
            Text("Kg")
        }
    )
}