package com.example.khizar_kotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.khizar_kotlin.features.direction.viewmodel.DirectionViewModel
import com.example.khizar_kotlin.ui.screens.DEFAULT_LOCATION
import com.example.khizar_kotlin.ui.screens.HomeScreen
import com.example.khizar_kotlin.ui.theme.Khizar_kotlinTheme
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val pointerState: MutableState<PointerState> =
        mutableStateOf(PointerState.ORIGIN(DEFAULT_LOCATION))
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            Khizar_kotlinTheme {
                val directionViewModel: DirectionViewModel = hiltViewModel()
                val directionInfoState = directionViewModel.directionInfoState.value
                HomeScreen(pointerState = pointerState,directionViewModel = directionViewModel)
            }
        }
    }
    override fun onBackPressed() {
        when (pointerState.value) {
            is PointerState.ORIGIN -> super.onBackPressed()
            is PointerState.DESTINATION, is PointerState.PICKED -> pointerState.value =
                PointerState.CLEAR(pointerState.value.initialLocation)
            is PointerState.CLEAR -> {
                //Do nothing for now

            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Khizar_kotlinTheme {
        Greeting("Android")
    }
}
/**
 * @property ORIGIN, when the user first starts to pick a location
 * @property DESTINATION, happens after Origin
 * @property PICKED, happens when the picking is finished and we go to calculation state.
 */
sealed class PointerState(var initialLocation: LatLng) {
    class ORIGIN(initialLocation: LatLng) : PointerState(initialLocation)
    class DESTINATION(initialLocation: LatLng) :
        PointerState(initialLocation)

    class PICKED(
        initialLocation: LatLng
    ) : PointerState(initialLocation)

    class CLEAR(initialLocation: LatLng) : PointerState(initialLocation = initialLocation)
}
enum class MapPointerMovingState {
    IDLE, DRAGGING
}
