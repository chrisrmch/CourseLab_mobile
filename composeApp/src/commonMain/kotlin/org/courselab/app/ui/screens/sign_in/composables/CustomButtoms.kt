package org.courselab.app.ui.screens.sign_in.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight


/**
 * Explicación del parámetro `content: (@Composable RowScope.() -> Unit)? = null`
 * Este parámetro permite al usuario del `CustomOutlinedButton` definir un contenido
 * personalizado para el botón. Vamos a desglosarlo:
 * - `@Composable`: Indica que la función lambda que se pase como `content` puede
 *                 llamar a otras funciones Composable. Esto es fundamental en Compose,
 *                 ya que la UI se construye componiendo estas funciones.
 * - `RowScope.()`: Esto significa que la lambda se ejecutará en el contexto de un `RowScope`.
 *                 `OutlinedButton` internamente usa un `Row` para organizar su contenido.
 *                 Al estar en `RowScope`, puedes usar modificadores y alineaciones específicas
 *                 de `Row` si necesitas organizar múltiples elementos horizontalmente dentro
 *                 de tu contenido personalizado (por ejemplo, `Modifier.weight(1f)` o `Modifier.align(Alignment.CenterVertically)`).
 * - `-> Unit`: La función lambda no devuelve ningún valor explícito. Su propósito es emitir UI.
 * - `? = null`: Esto hace que el parámetro `content` sea opcional. Si no se proporciona un valor
 *              para `content` al llamar a `CustomOutlinedButton` (es decir, se deja como `null`,
 *              que es su valor por defecto), el botón utilizará un comportamiento predeterminado.
 * Cómo se usa en este Composable:
 * Dentro del bloque de contenido del `OutlinedButton` (el que está después de cerrar los paréntesis
 * de los parámetros), hay una lógica condicional:
 */

@Composable
fun CustomOutlinedButton(
    text: String = "Button",
    onClick: () -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth(),
    colors: ButtonColors = ButtonDefaults.outlinedButtonColors(
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.primary
    ),
    enabled: Boolean = true,
    border: BorderStroke = ButtonDefaults.outlinedButtonBorder().copy(
        brush = Brush.horizontalGradient(
            colors = listOf(
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.onPrimary
            )
        )
    ),
    shape: Shape = ButtonDefaults.outlinedShape,
    elevation: ButtonElevation? = null,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource? = null,
    content: (@Composable RowScope.() -> Unit)? = null
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        colors = colors,
        enabled = enabled,
        border = border,
        shape = shape,
        elevation = elevation,
        interactionSource = interactionSource,
        contentPadding = contentPadding
    ) {
        if (content == null) {
            Text(
                text,
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        } else content()
    }


}