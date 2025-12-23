use serde::Serialize;

#[derive(Debug, Serialize)]
pub struct EnergyTracker {
    last_update_ms: u64,
    energy_today_mwh: f32,
}


const IDLE_POWER_MW: f32 = 300.0;        // ESP32 + WiFi
const MAX_LED_POWER_MW: f32 = 900.0;    // LED @ 100%
impl EnergyTracker {
    pub fn new(now_ms: u64) -> Self {
        Self {
            last_update_ms: now_ms,
            energy_today_mwh: 0.0,
        }
    }

    pub fn update(&mut self, now_ms: u64, brightness: u8, is_on: bool) {
        let dt = now_ms - self.last_update_ms;
        if is_on {
            self.energy_today_mwh += self.energy_today_mwh * dt as f32;
        }
        self.last_update_ms = now_ms;

        let brightness_ratio = brightness as f32 / 255.0;
        let power_mw =
            IDLE_POWER_MW + MAX_LED_POWER_MW * brightness_ratio;

        self.energy_today_mwh += power_mw * (dt as f32 / 3_600_000.0);
    }

    pub fn reset_today(&mut self) {
        self.energy_today_mwh = 0.0;
    }

    pub fn value(&self) -> u32 {
        self.energy_today_mwh as u32
    }
}