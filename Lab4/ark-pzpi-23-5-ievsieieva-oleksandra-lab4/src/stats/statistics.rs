use esp_idf_sys::esp_timer_get_time;
use esp_idf_sys::*;
use serde::Serialize;

#[derive(Serialize)]
pub struct DeviceStats {
    pub uptime_ms: u64,
    pub last_reset_reason: ResetReason,
    pub temperature_c: Option<f32>,
    pub watchdog_resets: u32,
    pub energy_today_mwh: u32,
}

pub fn uptime_ms() -> u64 {
    unsafe { esp_timer_get_time() as u64 / 1_000 }
}


#[derive(Debug, Clone, Copy, Serialize)]
pub enum ResetReason {
    PowerOn,
    Software,
    Watchdog,
    Panic,
    Brownout,
    DeepSleep,
    Unknown,
}

pub fn last_reset_reason() -> ResetReason {
    match unsafe { esp_reset_reason() } {
        esp_reset_reason_t_ESP_RST_POWERON => ResetReason::PowerOn,
        esp_reset_reason_t_ESP_RST_SW => ResetReason::Software,
        esp_reset_reason_t_ESP_RST_WDT |
        esp_reset_reason_t_ESP_RST_TASK_WDT |
        esp_reset_reason_t_ESP_RST_INT_WDT => ResetReason::Watchdog,
        esp_reset_reason_t_ESP_RST_PANIC => ResetReason::Panic,
        esp_reset_reason_t_ESP_RST_BROWNOUT => ResetReason::Brownout,
        esp_reset_reason_t_ESP_RST_DEEPSLEEP => ResetReason::DeepSleep,
        _ => ResetReason::Unknown,
    }
}



use esp_idf_sys::{
    temperature_sensor_handle_t,
    temperature_sensor_config_t,
    soc_periph_temperature_sensor_clk_src_t_TEMPERATURE_SENSOR_CLK_SRC_DEFAULT,
    temperature_sensor_install,
    temperature_sensor_enable,
    temperature_sensor_get_celsius,
    ESP_OK,
};


static mut TEMP_SENSOR: temperature_sensor_handle_t =
    core::ptr::null_mut();

pub fn init_temperature_sensor() -> Result<(), i32> {
    unsafe {
        let cfg = temperature_sensor_config_t {
            range_min: -10,
            range_max: 80,
            clk_src: soc_periph_temperature_sensor_clk_src_t_TEMPERATURE_SENSOR_CLK_SRC_DEFAULT,
        };

        let err = temperature_sensor_install(&cfg, &mut TEMP_SENSOR);
        if err != ESP_OK {
            return Err(err);
        }

        let err = temperature_sensor_enable(TEMP_SENSOR);
        if err != ESP_OK {
            return Err(err);
        }

        Ok(())
    }
}

pub fn temperature_celsius() -> Option<f32> {
    unsafe {
        let mut temp: f32 = 0.0;
        if temperature_sensor_get_celsius(TEMP_SENSOR, &mut temp) == ESP_OK {
            Some(temp)
        } else {
            None
        }
    }
}

