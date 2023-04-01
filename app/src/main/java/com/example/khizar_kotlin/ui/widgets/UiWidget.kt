package com.example.khizar_kotlin.ui.widgets

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.khizar_kotlin.MapPointerMovingState
import com.example.khizar_kotlin.PointerState
import com.example.khizar_kotlin.ui.theme.Khizar_kotlinTheme
import com.google.android.gms.maps.model.LatLng
import com.example.khizar_kotlin.R


@Composable
@Preview
fun RideDetailWidgetPreview() {
    Khizar_kotlinTheme() {
        Surface {
            RideDetailWidget()
        }
    }
}


@Composable
@Preview
fun RideTypeWidgetPreview() {
    Khizar_kotlinTheme() {
        Surface {
            RideTypeWidget("Test item")
        }
    }
}

@Preview
@Composable
fun MapPointer(
    modifier: Modifier = Modifier,
    buttonMovingState: State<MapPointerMovingState> = mutableStateOf(MapPointerMovingState.IDLE),
    pointerState: State<PointerState> = mutableStateOf(
        PointerState.ORIGIN(
            LatLng(
                "35.6892".toDouble(),
                "51.3890".toDouble()
            )
        )
    ),
    onClick: () -> Unit = {}
) {
   val toState = if (buttonMovingState.value == MapPointerMovingState.IDLE) {
        MapPointerMovingState.DRAGGING
    } else {
        MapPointerMovingState.IDLE
    }


    MapPointer(
        modifier = modifier,
        pointerState = pointerState,

        ) {
        onClick()
    }
}

@Composable
fun MapPointer(
    modifier: Modifier = Modifier,
    //transitionState: TransitionState?,
    pointerState: State<PointerState> = mutableStateOf(
        PointerState.ORIGIN(
            LatLng(
                "35.6892".toDouble(),
                "51.3890".toDouble()
            )
        )
    ),
    onClick: () -> Unit
) {

   Box(
        modifier = modifier
            .wrapContentWidth(),
        contentAlignment = Alignment.TopStart
    ) {
        Box(
            modifier = Modifier
                //.padding(top = transitionState!![circlePaddingProp])
                //.size(transitionState[circleSizeProp])
                .align(Alignment.BottomCenter)
                .background(Color.Gray.copy(alpha = .3f), CircleShape)
                .wrapContentWidth()
        )
        Image(
            painter = painterResource(
                id = if (pointerState.value is PointerState.ORIGIN) R.drawable.ic_location_pointer_origin
                else R.drawable.ic_location_pointer_destination
            ),
            contentDescription =" ",
            modifier = Modifier
                //.padding(bottom = transitionState[pointerPaddingProp])
                .clickable(
                    onClick =
                    onClick
                )
                .size(32.dp, 64.dp),
        )
    }
}

@Composable
@Preview
fun PriceWidget(
    modifier: Modifier = Modifier,
    price: State<Int> = mutableStateOf(500)
) {
    Row(
        modifier = modifier
            .height(56.dp)
            .width(120.dp)
            .background(Color.White, CircleShape),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Rupees",
            style = MaterialTheme.typography.body2,
            color = Color.Black.copy(alpha = .7f)
        )
        Spacer(modifier = Modifier.padding(2.dp))
        Text(
            text = price.value.toString(),
            style = MaterialTheme.typography.h6
        )
    }
}

@Composable
fun IconButton(
    modifier: Modifier = Modifier,
    asset: Painter,
    shape: Shape = CircleShape,
    onClick: () -> Unit
) {
    Surface(
        elevation = 4.dp,
        modifier = modifier
            .size(56.dp)
            .clickable(
                onClick = onClick,
                //indication = RippleIndication(bounded = false, radius = 28.dp)
            ),
        shape = shape
    ) {
        Row(
            modifier = Modifier
                .background(colorResource(id = R.color.white)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                tint = colorResource(id = R.color.box_snapp_services_header_titles_text),
                painter = asset,
                contentDescription =" ",
                modifier = Modifier
                    .size(24.dp)
            )
        }
    }
}

@Composable
fun RideDetailWidget() {
    val list = listOf("Luxury", "Business", "Economy")
    Card {
        Column {
            LazyRow(content = {
                items(list){
                    RideTypeWidget(it)
                }
            })
            Spacer(modifier = Modifier.size(16.dp))
            Row {
                OutlinedButton(
                    shape = CircleShape.copy(all = CornerSize(0.dp)),
                    modifier = Modifier.fillMaxWidth(.5f),
                    onClick = {}) {
                    Text(
                        text = "Travel Options",
                        color = colorResource(R.color.box_colorAccent),

                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    Icon(
                        tint = colorResource(id = R.color.box_colorAccent),
                        painter = painterResource(id = R.drawable.ic_ride_options_enabled),
                        modifier = Modifier.size(16.dp),contentDescription =" ",
                    )
                }
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    shape = CircleShape.copy(all = CornerSize(0.dp)),
                    onClick = {}) {
                    Text(
                        text = "Discount Code",
                        color = colorResource(R.color.box_colorAccent),

                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    Icon(
                        tint = colorResource(id = R.color.box_colorAccent),
                        painter = painterResource(id = R.drawable.ic_ride_voucher_enabled),
                        modifier = Modifier.size(16.dp),
                                contentDescription =" ",
                    )
                }
            }
        }
    }
}

@Composable
fun RideTypeWidget(title: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(top = 8.dp)
    ) {
        Image(
            modifier = Modifier.size(68.dp),
            contentDescription =" ",
            painter = painterResource(id = R.drawable.ic_ride_for_friend_service)
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            modifier = Modifier.width(100.dp),
            softWrap = true,
            textAlign = TextAlign.Center,
            text = title,
            style = MaterialTheme.typography.caption.copy(fontSize = 11.sp),
            maxLines = 2,
            color = colorResource(R.color.box_snapp_services_header_titles_text)
        )
    }
}
