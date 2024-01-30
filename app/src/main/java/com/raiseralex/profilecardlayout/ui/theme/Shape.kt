package com.raiseralex.profilecardlayout.ui.theme

import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val CustomShapes =
    Shapes(
        small = RoundedCornerShape(4.dp),
        medium = CutCornerShape(topEnd = 24.dp),
        large = RoundedCornerShape(8.dp),
        extraLarge = RoundedCornerShape(10.dp),
    )
