package com.example.khizar_kotlin.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.example.khizar_kotlin.MapPointerMovingState
import com.example.khizar_kotlin.PointerState
import com.example.khizar_kotlin.util.MapViewLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.*
import kotlin.random.Random
import com.example.khizar_kotlin.R
import com.example.khizar_kotlin.features.direction.viewmodel.DirectionViewModel
import com.example.khizar_kotlin.ui.widgets.MapPointer
import com.example.khizar_kotlin.ui.widgets.IconButton
import com.example.khizar_kotlin.ui.widgets.PriceWidget
import com.example.khizar_kotlin.ui.widgets.RideDetailWidget

val DEFAULT_LOCATION = LatLng(28.4213195, 70.3230145)
@Composable
fun HomeScreen(pointerState: MutableState<PointerState> ,directionViewModel: DirectionViewModel) {
    val map = MapViewLifecycle()
    val buttonState = remember { mutableStateOf(MapPointerMovingState.DRAGGING) }
    val zoomLevel = remember { 17f }
    val context = LocalContext.current
     val pickup = remember {
         mutableStateOf(DEFAULT_LOCATION)
     }
     val dropoff=remember {
         mutableStateOf(DEFAULT_LOCATION)
     }
    val onPointClick = {
        map.getMapAsync {
            val target = it.cameraPosition.target
            pointerState.value.initialLocation = target
            if(pointerState.value is PointerState.ORIGIN)
                pickup.value=target
            else if(pointerState.value is PointerState.DESTINATION)
                dropoff.value=target
            it.addMarker(
                MarkerOptions().position(target)
                    .icon(
                        BitmapDescriptorFactory
                            .fromResource(
                                if (pointerState.value is PointerState.ORIGIN) R.drawable.ic_location_pointer_origin
                                else R.drawable.ic_location_pointer_destination
                            )
                    )
            )
            pointerState.value =
                when (pointerState.value) {
                    is PointerState.ORIGIN -> PointerState.DESTINATION(
                        (pointerState.value as PointerState.ORIGIN).initialLocation
                    )
                    is PointerState.DESTINATION -> PointerState.PICKED(
                        pointerState.value.initialLocation
                    )
                    else -> PointerState.ORIGIN(pointerState.value.initialLocation)
                }
            if (pointerState.value !is PointerState.PICKED) {
                val rand = Random.nextBoolean()
                val xRand = Random.nextInt(150, 300).toFloat()
                val yRand = Random.nextInt(150, 300).toFloat()
                it.moveCamera(
                    CameraUpdateFactory.scrollBy(
                        if (rand) xRand * 1f else xRand * -1f,
                        if (!rand) yRand * 1f else yRand * -1f
                    )
                )
            }
            if(pointerState.value is PointerState.PICKED && pickup.value!= DEFAULT_LOCATION){
                directionViewModel.getDirection(
                    origin = "${pickup.value.latitude}, ${pickup.value.longitude}",
                    destination = "${dropoff.value.latitude}, ${dropoff.value.longitude}",
                    key = context.getString(R.string.MAPS_API_KEY)
                )
            }
            it.animateCamera(CameraUpdateFactory.zoomBy(-0.5f))
        }
    }

    Box {
        //First child of the box because content must be drawn below everything
        HomeContent(
            context,
            pointerModifier = Modifier
                .align(Alignment.Center)
                .padding(bottom = 52.dp),
            pointerState = pointerState,
            buttonState = buttonState,
            zoomLevel = zoomLevel,
            map = map,
            directionViewModel = directionViewModel,
            onClick = onPointClick
        )
        HomeHeader(context, pointerState)
        HomeFooter(
            Modifier.align(Alignment.BottomCenter),
            pointerState,
            onPointClick
        )
    }

    if (pointerState.value is PointerState.CLEAR) {
        map.getMapAsync { it.clear() }
        directionViewModel.clearPolyLine()
        pointerState.value = PointerState.ORIGIN(pointerState.value.initialLocation)
    }
}


@Composable
fun HomeContent(
    context: Context = LocalContext.current,
    pointerState: State<PointerState>,
    buttonState: MutableState<MapPointerMovingState>,
    zoomLevel: Float,
    map: MapView,
    pointerModifier: Modifier,
    directionViewModel: DirectionViewModel,
    onClick: () -> Unit
) {
    AndroidView({ map }) { mapView ->
        mapView.getMapAsync {
            if (pointerState.value !is PointerState.PICKED) {

                it.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        pointerState.value.initialLocation, zoomLevel
                    )
                )
                it.setOnCameraMoveStartedListener {
                    buttonState.value = MapPointerMovingState.IDLE
                }
                it.setOnCameraIdleListener {
                    buttonState.value = MapPointerMovingState.DRAGGING
                }
            }
            it.addPolyline(
                PolylineOptions().addAll(directionViewModel.polyLinesPoints.value)
                    .width // below line is use to specify the width of poly line.
                        (15f) // below line is use to add color to our poly line.
                    .color(ContextCompat.getColor(context,R.color.purple_200)) // below line is to make our poly line geodesic.
                    .geodesic(true)
            )
            /*if(directionViewModel.polyLinesPoints.value.size>2 && directionViewModel.polyLinesPoints.value.first().latitude.compareTo(directionViewModel.polyLinesPoints.value.last().latitude)<0){
                    it.animateCamera(CameraUpdateFactory.newLatLngBounds(
                        LatLngBounds(directionViewModel.polyLinesPoints.value.first(),directionViewModel.polyLinesPoints.value.last())
                        , 20
                    ))
                }
            if(directionViewModel.polyLinesPoints.value.size>2 && directionViewModel.polyLinesPoints.value.first().latitude.compareTo(directionViewModel.polyLinesPoints.value.last().latitude)>0){
                it.animateCamera(CameraUpdateFactory.newLatLngBounds(
                    LatLngBounds(directionViewModel.polyLinesPoints.value.last(),directionViewModel.polyLinesPoints.value.first())
                    , 20
                ))
            }*/
        }
    }
    if (pointerState.value !is PointerState.PICKED) {
        MapPointer(
            modifier = pointerModifier,
            buttonState,
            pointerState = pointerState,
            onClick = onClick
        )
    }
}

@Composable
@Preview
fun HomeHeader(
    context: Context = LocalContext.current,
    pointerState: State<PointerState> = mutableStateOf(
        PointerState.PICKED(DEFAULT_LOCATION)
    )
) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        IconButton(
            Modifier
                .padding(start = 16.dp)
                .align(Alignment.TopStart),
                    painterResource(id = R.drawable.ic_flight_user)
        ) {
            Toast.makeText(context, "Not implemented", Toast.LENGTH_LONG)
                .show()
        }
        if (pointerState.value is PointerState.PICKED) {
            PriceWidget(Modifier.align(Alignment.Center))
        }
    }
}

@Composable
fun HomeFooter(
    modifier: Modifier = Modifier,
    pointerState: State<PointerState>,
    onClick: () -> Unit
) {
    Column(
        modifier.padding(
            start = 16.dp,
            end = 16.dp,
            bottom = 16.dp
        )
    ) {
        if (pointerState.value is PointerState.PICKED) {
            RideDetailWidget()
            Spacer(modifier = Modifier.size(16.dp))
        }
        Button(
            onClick = {
                if (pointerState.value !is PointerState.PICKED) onClick()
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.box_colorAccent)),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(
                text = if (pointerState.value is PointerState.ORIGIN || pointerState.value is PointerState.CLEAR) "Confirm Pickup"
                else if (pointerState.value is PointerState.DESTINATION) "Confirm DropOff"
                else "Request",
                color = Color.White,

                )
        }
    }
}

