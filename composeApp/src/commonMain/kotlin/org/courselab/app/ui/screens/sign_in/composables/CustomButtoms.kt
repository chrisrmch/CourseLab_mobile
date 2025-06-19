package org.courselab.app.ui.screens.sign_in.composables

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.snap
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

object OutlinedWelcomeButtons {

    private const val TEXT_NOT_IMPLEMENTED_BY_DEFAULT = ""

    @Composable
    fun Primary(
        text: String = TEXT_NOT_IMPLEMENTED_BY_DEFAULT,
        onClick: () -> Unit,
        modifier: Modifier = Modifier.fillMaxWidth(),
        enabled: Boolean = false,
        shape: Shape = ButtonDefaults.outlinedShape,
        contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
        content: (@Composable RowScope.() -> Unit)? = null,
    ) {
        val owbpInteractionSOurce = remember { MutableInteractionSource() }

        OutlinedButton(
            interactionSource = owbpInteractionSOurce,
            modifier = modifier.then(
                elevatedShadow(
                    enabled = enabled,
                    shape = shape,
                    interactionSource = owbpInteractionSOurce
                )
            ),
            border = ButtonDefaults.outlinedButtonBorder().copy(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.onPrimary,
                        MaterialTheme.colorScheme.primary
                    )
                )
            ),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = MaterialTheme
                    .colorScheme
                    .secondary,

                contentColor = MaterialTheme
                    .colorScheme
                    .onSecondary,

                disabledContainerColor = MaterialTheme
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

    @Composable
    fun Secondary(
        text: String = TEXT_NOT_IMPLEMENTED_BY_DEFAULT,
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
            when {
                !enabled      -> disabledElevation
                isPressed     -> pressedElevation
                isHovered     -> hoveredElevation
                isFocused     -> focusedElevation
                else          -> defaultElevation
            }
        }

        val animatedElevation by animateDpAsState(
            targetValue = targetElevation,
            animationSpec = if(enabled){
                spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            } else {
                snap()
            }
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
