package com.example.heimdallgp_livedata.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.heimdallgp_livedata.BottomNavTab
import com.example.heimdallgp_livedata.CarState

@Composable
fun TopRaceBar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(top = 10.dp, start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(8.dp).background(Color.Red, CircleShape))
            Spacer(Modifier.width(6.dp))
            Text("LIVE", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
        Text("Monaco GP", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text("52/78", color = Color.White, fontWeight = FontWeight.Medium, fontSize = 14.sp)
    }
}

@Composable
fun DriverDetailsPanel(
    car: CarState,
    currentTab: BottomNavTab,
    onTabSelected: (BottomNavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF181818))
            .padding(16.dp)
            .navigationBarsPadding()
    ) {
        DriverHeader(car)

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = Color(0xFF333333))
        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier.height(120.dp)) {
            when(currentTab) {
                BottomNavTab.LEADERBOARD -> LeaderboardView(car)
                BottomNavTab.TEAM_RADIO -> TeamRadioView(car)
                BottomNavTab.RACE_DATA -> RaceDataView(car)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        BottomNavBar(currentTab, onTabSelected)
    }
}

@Composable
fun DriverHeader(car: CarState) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier.size(50.dp).clip(CircleShape).background(car.driver.teamColor),
            contentAlignment = Alignment.Center
        ) {
            Text(car.driver.shortName, color = Color.White, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(car.driver.name, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(car.driver.team, color = Color.Gray, fontSize = 14.sp)
        }
    }
}


@Composable
fun LeaderboardView(car: CarState) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.weight(1f)) {
            StatItem("Position", car.positionText)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Tire", color = Color.Gray, fontSize = 12.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(8.dp).background(if(car.tireCompound=="Soft") Color.Red else Color.Yellow, CircleShape))
                Spacer(Modifier.width(6.dp))
                Text(car.tireCompound, color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            StatItem("Last Lap", car.lapTime)
            Spacer(modifier = Modifier.height(16.dp))
            StatItem("Best Lap", car.bestLap)
        }
    }
}

@Composable
fun TeamRadioView(car: CarState) {
    Column {
        Text("Latest Transcript:", color = Color.Gray, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Box box, box box. Stay out.", color = Color(0xFFFFC107), fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF333333)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(8.dp))
            Text("Replay Audio")
        }
    }
}

@Composable
fun RaceDataView(car: CarState) {
    Column {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            StatItem("Speed", "${car.telemetry.speedKmh} km/h")
            StatItem("Gear", "${car.telemetry.gear}")
            StatItem("RPM", "${car.telemetry.rpm}")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Throttle: ${(car.telemetry.throttle * 100).toInt()}%", color = Color.Green, fontSize = 12.sp)
        LinearProgressIndicator(progress = { car.telemetry.throttle }, modifier = Modifier.fillMaxWidth(), color = Color.Green, trackColor = Color.DarkGray)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Brake: ${(car.telemetry.brake * 100).toInt()}%", color = Color.Red, fontSize = 12.sp)
        LinearProgressIndicator(progress = { car.telemetry.brake }, modifier = Modifier.fillMaxWidth(), color = Color.Red, trackColor = Color.DarkGray)
    }
}

@Composable
fun StatItem(label: String, value: String) {
    Column {
        Text(label, color = Color.Gray, fontSize = 12.sp)
        Text(value, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
    }
}

@Composable
fun BottomNavBar(currentTab: BottomNavTab, onTabSelected: (BottomNavTab) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        NavItem(Icons.Default.List, "Leaderboard", currentTab == BottomNavTab.LEADERBOARD) { onTabSelected(BottomNavTab.LEADERBOARD) }
        NavItem(Icons.Default.Phone, "Team Radio", currentTab == BottomNavTab.TEAM_RADIO) { onTabSelected(BottomNavTab.TEAM_RADIO) }
        NavItem(Icons.Default.Info, "Race Data", currentTab == BottomNavTab.RACE_DATA) { onTabSelected(BottomNavTab.RACE_DATA) }
    }
}

@Composable
fun NavItem(icon: ImageVector, label: String, isActive: Boolean, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick).padding(8.dp)
    ) {
        Icon(icon, null, tint = if(isActive) Color(0xFFFF5722) else Color.Gray)
        Spacer(Modifier.height(4.dp))
        Text(label, color = if(isActive) Color(0xFFFF5722) else Color.Gray, fontSize = 10.sp)
    }
}