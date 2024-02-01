package com.raiseralex.profilecardlayout

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.wear.compose.material3.ContentAlpha
import androidx.wear.compose.material3.LocalContentAlpha
import coil.transform.CircleCropTransformation
import com.google.accompanist.coil.rememberCoilPainter
import com.raiseralex.profilecardlayout.ui.theme.CustomShapes
import com.raiseralex.profilecardlayout.ui.theme.ProfileCardLayoutTheme
import com.raiseralex.profilecardlayout.ui.theme.lightGreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProfileCardLayoutTheme {
                UsersApplication()
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun UserListScreen(
    userProfiles: List<UserProfile> = userProfileList,
    navController: NavHostController?,
) {
    Scaffold(
        topBar = {
            AppBar(
                title = "Users list",
                icon = Icons.Default.Home,
            ) {
                navController?.navigateUp()
            }
        },
    ) { padding ->
        Surface(
            modifier =
                Modifier
                    .padding(padding)
                    .fillMaxSize(),
        ) {
            LazyColumn {
                items(userProfiles) { userProfiles ->
                    ProfileCard(userProfile = userProfiles) {
                        navController?.navigate("user_details/${userProfiles.id}")
                    }
                }
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun UserProfileDetailScreen(
    userId: Int,
    navController: NavHostController?,
) {
    val userProfile = userProfileList.first { userProfile -> userId == userProfile.id }
    Scaffold(topBar = {
        AppBar(
            title = "Users profile details",
            icon = Icons.AutoMirrored.Filled.ArrowBack,
        ) {
            navController?.navigateUp()
        }
    }) { padding ->
        Surface(
            modifier =
                Modifier
                    .padding(padding)
                    .fillMaxSize(),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
            ) {
                ProfilePicture(
                    userProfile.drawableId,
                    userProfile.status,
                    240.dp,
                )
                ProfileContent(
                    userProfile.name,
                    userProfile.status,
                    alignment = Alignment.CenterHorizontally,
                )
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun UsersApplication(userProfile: List<UserProfile> = userProfileList) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "users_list") {
        composable("users_list") {
            UserListScreen(userProfile, navController)
        }
        composable(
            route = "user_details/{userId}",
            arguments =
                listOf(
                    navArgument("userId") {
                        type = NavType.IntType
                    },
                ),
        ) { navBackStackEntry ->
            navBackStackEntry.arguments?.let {
                UserProfileDetailScreen(
                    it.getInt("userId"),
                    navController,
                )
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    title: String,
    icon: ImageVector,
    iconClickAction: () -> Unit,
) {
    TopAppBar(
        colors =
            TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            ),
        navigationIcon = {
            Icon(
                imageVector = icon,
                contentDescription = "back",
                Modifier.padding(horizontal = 12.dp).clickable { iconClickAction.invoke() },
            )
        },
        title = {
            Text(text = title)
        },
    )
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun ProfileCard(
    userProfile: UserProfile,
    clickAction: () -> Unit,
) {
    val cardElevation = CardDefaults.cardElevation(8.dp)

    Card(
        modifier =
            Modifier
                .padding(top = 16.dp, bottom = 4.dp, start = 16.dp, end = 16.dp)
                .fillMaxWidth()
                .wrapContentHeight(align = Alignment.Top)
                .clickable { clickAction.invoke() },
        elevation = cardElevation,
        colors =
            CardDefaults.cardColors(
                containerColor = Color.DarkGray,
            ),
        shape = CustomShapes.medium,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            ProfilePicture(
                userProfile.drawableId,
                userProfile.status,
                72.dp,
            )
            ProfileContent(
                userProfile.name,
                userProfile.status,
                Alignment.Start,
            )
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun ProfilePicture(
    drawableId: String,
    onlineStatus: Boolean,
    imageSize: Dp,
) {
    Card(
        shape = CircleShape,
        border =
            BorderStroke(
                width = 2.dp,
                color =
                    if (onlineStatus) {
                        lightGreen
                    } else {
                        Color.Red
                    },
            ),
        modifier = Modifier.padding(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        // AsyncImage(model = "https://picsum.photos/300/300", contentDescription = "Coil pictures..")
        Image(
            painter =
                rememberCoilPainter(
                    request = drawableId,
                    requestBuilder = {
                        transformations(CircleCropTransformation())
                    },
                ),
            modifier = Modifier.size(imageSize),
            contentDescription = "Profile picture description",
        )
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun ProfileContent(
    userName: String,
    onlineStatus: Boolean,
    alignment: Alignment.Horizontal,
) {
    Column(
        modifier =
            Modifier
                .padding(8.dp),
        horizontalAlignment = alignment,
    ) {
        Text(
            text = userName,
            style = MaterialTheme.typography.titleLarge,
        )
        CompositionLocalProvider(LocalContentAlpha provides (if (onlineStatus) 1f else ContentAlpha.medium)) {
            Text(text = if (onlineStatus) "Active now" else "Offline", style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun PreviewUser() {
    UserProfileDetailScreen(0,null)
}

/*var a: String? = "abc"
val example = "shoes"

fun main(args: Array<String>) {
    a = null
    print(a)
    var count = 0
    for (i in example) {
        if (i == 's') {
            count += 1
        }
    }
    println(count)

    val l =
        if (a != null) {
            a
        } else {
            -1
        }
    print(l)

}*/
