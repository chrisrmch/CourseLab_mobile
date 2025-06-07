package org.courselab.app.ui.screens.sign_in.composables

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


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

object OutlinedWelcomeButtons {

    @Composable
    fun Primary(
        text: String = "Button",
        onClick: () -> Unit,
        modifier: Modifier = Modifier.fillMaxWidth(),
        enabled: Boolean = true,
        shape: Shape = ButtonDefaults.outlinedShape,
        contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
        content: (@Composable RowScope.() -> Unit)? = null
    ) {
        val owbpInteractionSOurce = remember { MutableInteractionSource() }

        OutlinedButton(
            interactionSource = owbpInteractionSOurce,
            modifier = modifier.then(elevatedShadow(
                enabled = enabled,
                shape = shape,
                interactionSource = owbpInteractionSOurce
            )),
            border = ButtonDefaults.outlinedButtonBorder().copy(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.onPrimary,
                        MaterialTheme.colorScheme.primary
                    )
                )
            ),
            colors =  ButtonDefaults.outlinedButtonColors(
                containerColor = MaterialTheme
                    .colorScheme
                    .secondary,

                contentColor = MaterialTheme
                    .colorScheme
                    .onSecondary,

                disabledContainerColor =  MaterialTheme
                    .colorScheme
                    .outlineVariant.copy(alpha = 0.50f),
                disabledContentColor = MaterialTheme
                    .colorScheme.surfaceVariant
            ),
            onClick = onClick,
            enabled = enabled,
            shape = shape,
            contentPadding = contentPadding
        ) {
            if (content == null) {
                Text(
                    text,
                    color = MaterialTheme.colorScheme.onSecondary,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            } else content()
        }

    }


    /**
     * ¿Haber puesto `MutableInteractionSource()` aquí ejecuta algún efecto?
     * Sí, crear una instancia de `MutableInteractionSource` aquí tiene efectos,
     * aunque no sean visibles inmediatamente o no estés interactuando activamente con ellos.
     * 1.  **Habilitación del sistema de interacciones:** Al proporcionar un `MutableInteractionSource`,
     *     el `OutlinedButton` internamente comenzará a observar y emitir eventos de interacción
     *     (como `PressInteraction.Press` y `PressInteraction.Release`) a esta fuente.
     *     Aunque no estés recolectando estos eventos explícitamente en *este* Composable,
     *     el mecanismo está activo.
     * 2.  **Efectos visuales predeterminados:** Algunos componentes de Material Design (como `Button`)
     *     utilizan el `InteractionSource` para aplicar efectos visuales predeterminados en respuesta
     *     a interacciones. Por ejemplo, el efecto de "ripple" (ondulación) al presionar un botón
     *     está típicamente vinculado al `InteractionSource`. Si no proporcionaras uno, o si el
     *     componente no creara uno internamente por defecto (lo cual suelen hacer), podrías perder
     *     estos efectos visuales. En el caso de `OutlinedButton`, es muy probable que utilice el
     *     `interactionSource` para gestionar el ripple.
     * 3.  **Posibilidad de personalización futura:** Aunque no lo uses ahora, tenerlo aquí significa
     *     que si en el futuro decides reaccionar a los estados de interacción (por ejemplo, cambiar
     *     la apariencia del botón cuando está presionado), ya tienes la fuente lista.
     * **En resumen:** Aunque no estés usando `interactionSource` explícitamente en tu código
     * para cambiar la lógica o la apariencia de forma personalizada, el simple hecho de
     * proporcionarlo habilita el sistema de interacciones subyacente del botón, lo que
     * permite que funcionen los efectos visuales estándar (como el ripple) y prepara
     * el terreno para personalizaciones basadas en interacciones si las necesitas más adelante.
     * Si *no* proporcionaras un `MutableInteractionSource` y el `OutlinedButton` no crease
     * uno por defecto internamente, los efectos visuales de interacción podrían no funcionar.
     * Sin embargo, la mayoría de los componentes Material suelen crear uno si no se les pasa.
     * La ventaja de pasarlo explícitamente (incluso uno nuevo como aquí) es que te da control
     * si quisieras observar esas interacciones desde fuera o compartir la misma fuente entre
     * múltiples componentes.
     */

    @Composable
    fun Secondary(
        text: String = "Button",
        onClick: () -> Unit,
        enabled: Boolean = true,
        modifier: Modifier = Modifier.fillMaxWidth(),
        shape: Shape = ButtonDefaults.outlinedShape,
        content: (@Composable RowScope.() -> Unit)? = null
    ) {
        val owbsInteractionSource = remember { MutableInteractionSource() }

        OutlinedButton(
            modifier = modifier.then(elevatedShadow(enabled, shape, owbsInteractionSource)),
            colors = ButtonDefaults.elevatedButtonColors().copy(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            ),
            interactionSource = owbsInteractionSource,
            border = ButtonDefaults.outlinedButtonBorder().copy(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.onPrimary,
                        MaterialTheme.colorScheme.primary
                    )
                )
            ),
            enabled = enabled,
            shape = shape,
            onClick = onClick
        ) {
            if (content == null) {
                Text(
                    text,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyLarge,
                )
            } else content()
        }
    }


    @Composable
    private fun elevatedShadow(
        enabled: Boolean,
        shape: Shape,
        interactionSource: InteractionSource
    ): Modifier {

        val isPressed by interactionSource.collectIsPressedAsState()
        val isHovered by interactionSource.collectIsHoveredAsState()
        val isFocused by interactionSource.collectIsFocusedAsState()

        val defaultElevation = 13.dp
        val pressedElevation = 5.dp
        val hoveredElevation = 10.dp
        val focusedElevation = 8.dp
        val disabledElevation = 0.dp

        val targetElevation: Dp = remember(enabled, isPressed, isHovered, isFocused) {
            if (!enabled) {
                disabledElevation
            } else if (isPressed) {
                pressedElevation
            } else if (isHovered) {
                hoveredElevation
            } else if (isFocused) {
                focusedElevation
            } else {
                defaultElevation
            }
        }

        val animatedElevation by animateDpAsState(
            targetValue = targetElevation,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )

        val shadowColor = MaterialTheme.colorScheme.inverseSurface.copy(alpha = 0.30f)

        return Modifier
            .shadow(
                elevation = animatedElevation,
                shape = shape,
                ambientColor = shadowColor,
                spotColor = shadowColor
            );
    }
}
